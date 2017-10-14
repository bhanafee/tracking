package com.example.track

import java.time.Instant

object Tracker {
	type Longitude = Double
	type Latitude = Double
	type Meters = Float
	case class Tag (key: String, value: Option[String])

	case class Position (longitude: Longitude, latitude: Latitude, elevation: Option[Meters]) {
 		require(longitude >= -180.0 && longitude <= 180.0, "Longitude out of range")
 		require(latitude >= -90.0 && latitude <= 90.0, "Latitude out of range")
 		// Shore of Dead Sea is -413m and top of Everest is 8848m
 		require(elevation.forall(e => e >= -500.0 && e <= 9000.0), "Elevation out of range")
	}

	case class Observation (id: String, timestamp: Instant, position: Option[Position], tags: Tag*) {
		require (id.nonEmpty)
	}
	case class Query (id: Option[String], since: Option[Instant], tags: Tag*)

	case class Path (observations: Observation*)
}

import akka.actor.{ Actor, ActorLogging }
import akka.event.LoggingReceive
import Tracker.{Observation, Query, Path, Tag}

class Tracker extends Actor with ActorLogging {
	private var observations: Seq[Observation] = Seq.empty

	// TODO: Add OpenTracing
	override def receive = LoggingReceive {
		case Observation(id, timestamp, None, Seq()) =>
			log.warning("Ignoring empty observation")
		case o: Observation =>
			observations = observations match {
				case Seq() => Seq(o)
				case Seq(head, _*) if (head.timestamp.isBefore(o.timestamp)) => o +: observations
				// TODO: partition and insert observations received out of time sequence
				case os => ???
			}

		case Query(Some(id), None, Seq()) =>
			val latest = observations.find(_.id == id)
			sender() ! Path(latest.toSeq: _*)
		case Query(qid, None, tags @ _*) =>
			val latestMatch = observations
				.filter(idMatch(qid))
				.filter(tagMatch(tags))
				.headOption
			sender() ! Path(latestMatch.toSeq: _*)
		case Query(qid, Some(since), tags @_*) =>
			val matches = observations
				.filter(idMatch(qid))
				.takeWhile(_.timestamp.isAfter(since))
				.filter(tagMatch(tags))
			sender() ! Path(matches: _*)
	}

	def idMatch(qid: Option[String]): Observation => Boolean = qid match {
		case Some(id) => _.id == id
		case None => _ => true
	}

	// TODO: write tagMatch function
    def tagMatch(tags: Seq[Tag]): Observation => Boolean = _ => true
}