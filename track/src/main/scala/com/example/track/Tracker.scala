package com.example.track

import java.time.Instant

import io.opentracing.contrib.akka.ActorTracing
import io.opentracing.contrib.akka.TextMapCarrier.{Payload, Traceable}

object Tracker {
  type Longitude = Double
  type Latitude = Double
  type Meters = Float

  /** Used with Waypoint */
  case class Tag(key: String, value: Option[String])

  /** Used with Waypoint */
  case class Position(longitude: Longitude, latitude: Latitude, elevation: Option[Meters]) {
    require(longitude >= -180.0 && longitude <= 180.0, "Longitude out of range")
    require(latitude >= -90.0 && latitude <= 90.0, "Latitude out of range")
    // Shore of Dead Sea is -413m and top of Everest is 8848m
    require(elevation.forall(e ⇒ e >= -500.0 && e <= 9000.0), "Elevation out of range")
  }

  /** Identifies the location of an identified object at a point in time. */
  case class Waypoint(id: String, timestamp: Instant, position: Option[Position], tags: List[Tag])
                     (val trace: Payload) extends Traceable {
    require(id.nonEmpty)
  }

  /** Convenience constructors */
  object Waypoint {
    def apply(id: String, timestamp: Instant, position: Position)(trace: Payload) =
      new Waypoint(id, timestamp, Some(position), List())(trace)

    def apply(id: String, timestamp: Instant, tags: List[Tag])(trace: Payload) =
      new Waypoint(id, timestamp, None, tags)(trace)
  }

  /** Inquiry message carrying optional query parameters. */
  case class Query(id: Option[String], since: Option[Instant], tags: List[Tag])
                  (val trace: Payload) extends Traceable

  /** Convenience constructors */
  object Query {
    def apply(id: String, since: Instant)(trace: Payload) = new Query(Some(id), Some(since), List())(trace)

    def apply(id: String)(trace: Payload) = new Query(Some(id), None, List())(trace)

    def apply(since: Instant, tags: List[Tag])(trace: Payload) = new Query(None, Some(since), tags)(trace)

    def apply(since: Instant)(trace: Payload) = new Query(None, Some(since), List())(trace)
  }

  /** Message containing a sequence of waypoints */
  case class Track(waypoints: Waypoint*)
                  (val trace: Payload) extends Traceable

}

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingReceive
import com.example.track.Tracker.{Query, Tag, Track, Waypoint}
import io.opentracing.Tracer
import io.opentracing.contrib.akka.TracingReceive

class Tracker(val tracer: Tracer) extends Actor with ActorLogging with ActorTracing {

  var waypoints: Seq[Waypoint] = Seq.empty

  override def receive = LoggingReceive {
    TracingReceive(this) {
      case Waypoint(_, _, None, List()) ⇒
        log.warning("Ignoring empty waypoint")
      case w: Waypoint ⇒
        waypoints = waypoints match {
          case Seq() ⇒
            log.debug(s"Accepting first waypoint $w")
            Seq(w)
          case Seq(head, _*) if head.timestamp.isBefore(w.timestamp) ⇒
            log.debug(s"Accepting additional waypoint $w")
            w +: waypoints
          case ws ⇒
            log.debug(s"Accepting out of order waypoint $w")
            // TODO: partition and insert waypoints received out of time sequence
            ???
        }

      case Query(qid, None, tags) ⇒
        log.debug(s"Querying latest by id $qid and tags $tags")
        val latestMatch = waypoints
          .filter(idMatch(qid))
          .find(tagMatch(tags))
          .toSeq
        trace("track") { child ⇒
          sender() ! Track(waypoints: _*)(child)
        }

      case Query(qid, Some(since), tags) ⇒
        log.debug(s"Querying by id $qid and tags $tags since $since")
        val matches = waypoints
          .filter(idMatch(qid))
          .takeWhile(_.timestamp.isAfter(since))
          .filter(tagMatch(tags))
        trace("track") { child ⇒
          sender() ! Track(matches: _*)(child)
        }
    }
  }

  /**
    * Returns whether a waypoint id matches a given id.
    * @param qid the id to match. `None` always returns true``
    */
  private def idMatch(qid: Option[String]): Waypoint ⇒ Boolean = qid match {
    case Some(id) ⇒ _.id == id
    case None ⇒ _ ⇒ true
  }

  // TODO: write tagMatch function
  private def tagMatch(tags: Seq[Tag]): Waypoint ⇒ Boolean = _ ⇒ true

}
