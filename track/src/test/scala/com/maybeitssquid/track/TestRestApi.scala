package com.maybeitssquid.track

import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import io.opentracing.contrib.akka.TextMapCarrier.Payload
import com.maybeitssquid.track.Tracker.{Query, Waypoint}

import scala.concurrent.duration._

class TestRestApi extends WordSpec with Matchers with BeforeAndAfterAll with ScalatestRouteTest {
  val DummyPayload: Payload = Map.empty

  private val Timeout = 500.milliseconds

  private val probe = TestProbe("tracker")
  private val api = new RestApi(probe.ref)
  private val route = Route.seal(api.route)

  "The API" should {
    "reject a bad path" in {
      Get("/bad") ~> route ~> check {
        status should be(StatusCodes.NotFound)
      }
    }
    "handle a global query" in {
      Get("/track") ~> route ~> check {
        probe.expectMsg(Query(None, None, List())(DummyPayload))
        probe.reply(Tracker.Track()(Map.empty))
        status should be(StatusCodes.OK)
      }
    }
//    "handle a query by id" ignore {
//      Get("/track/testid") ~> route ~> check {
//        System.out.println("Expecting")
//        probe.expectMsg(Tracker.Query(Some("testid"), None, List()))
//        System.out.println("Replying")
//        probe.reply(Tracker.Track())
//        System.out.println("Continuing")
//
//        System.out.println(s"Response: $responseEntity")
//        status should be(StatusCodes.OK)
//      }
//    }
//    "handle a query with 'since parameter" ignore {
//      Get("/track/testid?since=XXXXX") ~> route ~> check {
//        // TODO: check in probe
//        status should be(StatusCodes.OK)
//      }
//    }
//    "accept a complete global observation" ignore {
//      // TODO: POST observation, probe and status
//    }
//    "add a missing timestamp to a global observation" ignore {
//      // TODO: POST observation, probe and status
//    }
//    "reject an incomplete global observation" ignore {
//      // TODO: POST observation, probe and status
//    }
    "accept a complete observation by path" in {
      Post("/track/testid") ~> route ~> check {
        // TODO: Send a real entity and match to waypoint
        probe.expectMsgType[Waypoint]
        status should be(StatusCodes.Accepted)
      }
    }
//    "add a missing timestamp to an observation by path" ignore {
//      // TODO: POST observation, probe and status
//    }
//    "reject an incomplete observation by path" ignore {
//      // TODO: POST observation, probe and status
//    }
  }

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }
}
