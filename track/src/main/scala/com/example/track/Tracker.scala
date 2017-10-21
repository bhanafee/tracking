package com.example.track

import java.time.Instant

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

  case class Waypoint(id: String, timestamp: Instant, position: Option[Position], tags: Tag*) {
    require(id.nonEmpty)
  }

  case class Query(id: Option[String], since: Option[Instant], tags: Tag*)

  case class Track(waypoints: Waypoint*)
}

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingReceive
import Tracker.{Waypoint, Query, Track, Tag}

class Tracker extends Actor with ActorLogging {
  private var waypoints: Seq[Waypoint] = Seq.empty

  override def receive = LoggingReceive {
    case Waypoint(id, timestamp, None, Seq()) =>
      log.warning("Ignoring empty waypoint")
    case w: Waypoint =>
      waypoints = waypoints match {
        case Seq() => Seq(w)
        case Seq(head, _*) if head.timestamp.isBefore(w.timestamp) => w +: waypoints
        // TODO: partition and insert waypoints received out of time sequence
        case ws => ???
      }

    case Query(Some(id), None, Seq()) =>
      val latest = waypoints.find(_.id == id)
      sender() ! Track(latest.toSeq: _*)
    case Query(qid, None, tags@_*) =>
      val latestMatch = waypoints
        .filter(idMatch(qid))
        .find(tagMatch(tags))
      sender() ! Track(latestMatch.toSeq: _*)
    case Query(qid, Some(since), tags@_*) =>
      val matches = waypoints
        .filter(idMatch(qid))
        .takeWhile(_.timestamp.isAfter(since))
        .filter(tagMatch(tags))
      sender() ! Track(matches: _*)
  }

  def idMatch(qid: Option[String]): Waypoint => Boolean = qid match {
    case Some(id) => _.id == id
    case None => _ => true
  }

  // TODO: write tagMatch function
  def tagMatch(tags: Seq[Tag]): Waypoint => Boolean = _ => true
}