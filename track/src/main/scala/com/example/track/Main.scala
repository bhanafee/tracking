package com.example.track

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{Directives, StandardRoute}
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.example.track.Tracker._
import com.typesafe.config.ConfigFactory
import java.time.Instant
import java.time.format.DateTimeFormatter.ISO_INSTANT

import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

// TODO: Add OpenTracing
object Main extends App with Directives {
  implicit val system: ActorSystem = ActorSystem("TrackSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val timeout: Timeout = Timeout(config.getInt("http.timeout").seconds)

  val trackerProps = Props[Tracker]

  val tracker: ActorRef = system.actorOf(trackerProps, "tracker")

  case class Observation(longitude: Double, latitude: Double, elevation: Option[Float], id: Option[String], timestamp: Option[Instant], tags: List[Tag])

  // TODO: Provide unmarshaller for Observation
  implicit val oum: FromRequestUnmarshaller[Observation] = null

  val route =
    pathPrefix("track") {
      pathEndOrSingleSlash {
        post {
          entity(as[Observation])(handleObservation(_, None))
        } ~ get {
          handleQuery(None, None)
        }
      } ~ {
        path(Segment) { id =>
          pathEnd {
            get {
              parameter('since) { since => handleQuery(Some(id), Some(since))
              } ~ get {
                handleQuery(Some(id), None)
              }
            }
          } ~ post {
            entity(as[Observation])(handleObservation(_, Some(id)))
          }
        }
      }
    }

  val binding = Http().bindAndHandle(route, host, port)

  private def handleObservation(observation: Observation, path: Option[String]): StandardRoute = Try {
    val id: String = observation.id.getOrElse(path.get)
    val timestamp: Instant = observation.timestamp.getOrElse(Instant.now())
    val position = Position(observation.longitude, observation.latitude, observation.elevation)
    Waypoint(id, timestamp, Some(position), observation.tags)
  } match {
    case Success(w) =>
      tracker ! w
      complete(StatusCodes.Accepted)
    case Failure(e) => complete(StatusCodes.BadRequest)
  }

  private def handleQuery(id: Option[String], since: Option[String]): StandardRoute = Try {
    since.map(s => Instant.from(ISO_INSTANT.parse(s)))
  } match {
    case Success(s) => complete {
      // TODO: render as JSON rather than toString()
      (tracker ? Query(id=id, since = s, List())).mapTo[Track].map(_.toString)
    }
    case Failure(e) => complete(StatusCodes.BadRequest)
  }

}