package com.example.track

import java.time.Instant

import io.opentracing.{References, Span, SpanContext, Tracer}
import io.opentracing.contrib.akka.{Spanned, Spanning, TextMapCarrier, TracingReceive}
import io.opentracing.contrib.akka.TextMapCarrier.{Payload, Traceable}

object Tracker {
  type Longitude = Double
  type Latitude = Double
  type Meters = Float

  case class Tag(key: String, value: Option[String])

  case class Position(longitude: Longitude, latitude: Latitude, elevation: Option[Meters]) {
    require(longitude >= -180.0 && longitude <= 180.0, "Longitude out of range")
    require(latitude >= -90.0 && latitude <= 90.0, "Latitude out of range")
    // Shore of Dead Sea is -413m and top of Everest is 8848m
    require(elevation.forall(e => e >= -500.0 && e <= 9000.0), "Elevation out of range")
  }

  case class Waypoint(id: String, timestamp: Instant, position: Option[Position], tags: List[Tag]) {
    require(id.nonEmpty)
  }

  object Waypoint {
    def apply(id: String, timestamp: Instant, position: Position) = new Waypoint(id, timestamp, Some(position), List())

    def apply(id: String, timestamp: Instant, tags: List[Tag]) = new Waypoint(id, timestamp, None, tags)
  }

  case class Query(id: Option[String], since: Option[Instant], tags: List[Tag])

  object Query {
    def apply(id: String, since: Instant) = new Query(Some(id), Some(since), List())

    def apply(id: String) = new Query(Some(id), None, List())

    def apply(since: Instant, tags: List[Tag]) = new Query(None, Some(since), tags)

    def apply(since: Instant) = new Query(None, Some(since), List())
  }

  case class Track(waypoints: Waypoint*)
                  (val trace: Payload) extends Traceable

}

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingReceive
import Spanned.Modifier
import Tracker.{Waypoint, Query, Track, Tag}

class Tracker(val tracer: Tracer) extends Actor with ActorLogging with Spanned {
  override def operation(): String = self.path.name

  implicit val spanContext2payload: SpanContext => Payload = TextMapCarrier.inject(tracer)

  var waypoints: Seq[Waypoint] = Seq.empty

  override def receive = LoggingReceive {
    TracingReceive(this, self) {
      case Waypoint(_, _, None, List()) =>
        log.warning("Ignoring empty waypoint")
      case w: Waypoint =>
        waypoints = waypoints match {
          case Seq() =>
            log.debug(s"Accepting first waypoint $w")
            Seq(w)
          case Seq(head, _*) if head.timestamp.isBefore(w.timestamp) =>
            log.debug(s"Accepting additional waypoint $w")
            w +: waypoints
          case ws =>
            log.debug(s"Accepting out of order waypoint $w")
            // TODO: partition and insert waypoints received out of time sequence
            ???
        }

      case Query(qid, None, tags) =>
        log.debug(s"Querying latest by id $qid and tags $tags")
        val latestMatch = waypoints
          .filter(idMatch(qid))
          .find(tagMatch(tags))
          .toSeq
        sendTrack(latestMatch)

      case Query(qid, Some(since), tags) =>
        log.debug(s"Querying by id $qid and tags $tags since $since")
        val matches = waypoints
          .filter(idMatch(qid))
          .takeWhile(_.timestamp.isAfter(since))
          .filter(tagMatch(tags))
        sendTrack(matches)
    }
  }

  private def idMatch(qid: Option[String]): Waypoint => Boolean = qid match {
    case Some(id) => _.id == id
    case None => _ => true
  }

  // TODO: write tagMatch function
  private def tagMatch(tags: Seq[Tag]): Waypoint => Boolean = _ => true

  val sendModifiers: Seq[Modifier] = Seq(
    Spanned.tagAkkaComponent,
    Spanned.tagProducer,
    Spanned.timestamp()
  )
  private def sendTrack(waypoints: Seq[Waypoint]): Unit = {
    // TODO: Write a cleaner abstraction
    val parent: Modifier = _.addReference(References.FOLLOWS_FROM, span.context())
    val modifiers = sendModifiers :+ parent
    val child: Span = modifiers.foldLeft(tracer.buildSpan("track"))((sb, m) => m(sb)).start()
    sender() ! Track(waypoints: _*)(child.context())
    child.finish()
  }
}
