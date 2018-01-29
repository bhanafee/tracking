package com.example.track

import java.time.Instant

import akka.actor.ActorRef
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route, StandardRoute}
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import akka.pattern.ask
import akka.stream.Materializer
import akka.util.Timeout
import com.example.track.Tracker._
import io.opentracing.contrib.akka.TextMapCarrier.Payload

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class RestApi(tracker: ActorRef) extends Directives {

  case class Observation(longitude: Double, latitude: Double, elevation: Option[Float], id: Option[String], timestamp: Option[Instant], tags: List[Tag])

  // TODO: Provide unmarshaller for Observation
  implicit val oum: FromRequestUnmarshaller[Observation] = new FromRequestUnmarshaller[Observation]() {
    override def apply(value: HttpRequest)(implicit ec: ExecutionContext, materializer: Materializer): Future[Observation] =
      Future.successful(Observation(0.0, 0.0, None, Some("bogus"), None, List()))
  }

  implicit val trackerTimeout: Timeout = 1.seconds

  val NoTrace: Payload = Map.empty

  val route: Route = logRequest("tracker") {
    pathPrefix("track") {
      pathEndOrSingleSlash {
        post {
          entity(as[Observation])(handleObservation(_, None))
        } ~ get {
          onSuccess((tracker ? Tracker.Query(None, None, List())(NoTrace)).mapTo[Track]) { result ⇒
            // TODO: Marshall result
            complete(result.toString())
          }
        }
      } ~ {
        path(Segment) { id ⇒
          pathEnd {
            get {
              parameter('since) { since ⇒
                // TODO: Parse 'since parameter, return BadRequest if it doesn't parse
                onSuccess((tracker ? Tracker.Query(Some(id), None, List())(NoTrace)).mapTo[Track]) { result ⇒
                  // TODO: Marshall result
                  complete(result.toString())
                }
              } ~ get {
                onSuccess((tracker ? Tracker.Query(Some(id), None, List())(NoTrace)).mapTo[Track]) { result ⇒
                  // TODO: Marshall result
                  complete(result.toString())
                }
              }
            }
          } ~ post {
            entity(as[Observation])(handleObservation(_, Some(id)))
          }
        }
      }
    }
  }


  private def handleObservation(observation: Observation, path: Option[String]): StandardRoute = Try {
    val id: String = observation.id.getOrElse(path.get)
    val timestamp: Instant = observation.timestamp.getOrElse(Instant.now())
    val position = Position(observation.longitude, observation.latitude, observation.elevation)
    Waypoint(id, timestamp, Some(position), observation.tags)(NoTrace)
  } match {
    case Success(w) ⇒
      tracker ! w
      complete(StatusCodes.Accepted)
    case Failure(e) ⇒ complete(StatusCodes.BadRequest)
  }
}
