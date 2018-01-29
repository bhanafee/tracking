package com.example.track

import java.time.Instant

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import com.example.track.Tracker.{Position, Tag, Track, Waypoint}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import io.opentracing.contrib.akka.TextMapCarrier.Payload

import scala.concurrent.duration._

class TestTracker extends TestKit(ActorSystem("TestTracker")) with WordSpecLike with Matchers with BeforeAndAfterAll {
  implicit val timeout: Timeout = 2.seconds

  val Somewhere: Option[Position] = Some(Position(0.0, 0.0, None))
  val No_Tags: List[Tag] = List()

  val DummyPayload: Payload = Map.empty

  "A Tracker" should {
    "start empty" in {
      val test = TestActorRef[Tracker]
      assert(test.underlyingActor.waypoints.isEmpty)
    }
    "when sent a waypoint" should {
      "ignore a waypoint with no tags or position" in {
        val test = TestActorRef[Tracker]
        test ! Waypoint("empty", Instant.now(), None, No_Tags)(DummyPayload)
        assert(test.underlyingActor.waypoints.isEmpty)
      }
      "accept a valid waypoint" in {
        val test = TestActorRef[Tracker]
        test ! Waypoint("id", Instant.now(), Somewhere, No_Tags)(DummyPayload)
        val result = test.underlyingActor.waypoints
        result.length should be(1)
        result.head.id should be("id")
      }
      "accept multiple waypoints" in {
        val test = TestActorRef[Tracker]
        test ! Waypoint("id1", Instant.now(), Somewhere, No_Tags)(DummyPayload)
        test ! Waypoint("id2", Instant.now(), Somewhere, No_Tags)(DummyPayload)
        test ! Waypoint("id3", Instant.now(), Somewhere, No_Tags)(DummyPayload)
        test ! Waypoint("id4", Instant.now(), Somewhere, No_Tags)(DummyPayload)
        val result = test.underlyingActor.waypoints
        result.length should be(4)
        result.head.id should be("id4")
      }
      "reorder out-of-sequence waypoints" ignore {

      }
    }
    "when queried" should {
      "send the most recent by id" in {
        val test = TestActorRef[Tracker]
        test ! Waypoint("id", Instant.now(), Somewhere, No_Tags)(DummyPayload)
        val future = test ? com.example.track.Tracker.Query("id")(DummyPayload)
        val result = future.value.get.get.asInstanceOf[Track]
        result.waypoints.length should be(1)
        result.waypoints.head.id should be("id")
      }
      "respond empty when there are no matches" in {
        val test = TestActorRef[Tracker]
        test ! Waypoint("id", Instant.now(), Somewhere, No_Tags)(DummyPayload)
        val future = test ? Tracker.Query("NOT_A_MATCH")(DummyPayload)
        val result = future.value.get.get.asInstanceOf[Track]
        result.waypoints.isEmpty should be(true)
      }
      "respond with latest by tags" ignore {}
      "respond with latest by id and tags" ignore {}
      "respond empty when there are no waypoints since a timestamp" in {
        val test = TestActorRef[Tracker]
        val now = Instant.now()
        test ! Waypoint("id", now.minusSeconds(100L), Somewhere, No_Tags)(DummyPayload)
        test ! Waypoint("id", now.minusSeconds(90L), Somewhere, No_Tags)(DummyPayload)
        test ! Waypoint("id", now.minusSeconds(80L), Somewhere, No_Tags)(DummyPayload)
        val future = test ? Tracker.Query("id", now.minusSeconds(10))(DummyPayload)
        val result = future.value.get.get.asInstanceOf[Track]
        result.waypoints.isEmpty should be (true)
      }
      "respond with all since a time" in {
        val test = TestActorRef[Tracker]
        val now = Instant.now()
        test ! Waypoint("id", now.minusSeconds(100L), Somewhere, No_Tags)(DummyPayload)
        test ! Waypoint("id", now.minusSeconds(90L), Somewhere, No_Tags)(DummyPayload)
        test ! Waypoint("id", now.minusSeconds(80L), Somewhere, No_Tags)(DummyPayload)
        val future = test ? Tracker.Query("id", now.minusSeconds(95))(DummyPayload)
        val result = future.value.get.get.asInstanceOf[Track]
        result.waypoints.length should be (2)
      }
      "respond with all since a time by id" in {
        val test = TestActorRef[Tracker]
        val now = Instant.now()
        test ! Waypoint("id", now.minusSeconds(100L), Somewhere, No_Tags)(DummyPayload)
        test ! Waypoint("id", now.minusSeconds(90L), Somewhere, No_Tags)(DummyPayload)
        test ! Waypoint("NO_MATCH", now.minusSeconds(85L), Somewhere, No_Tags)(DummyPayload)
        test ! Waypoint("id", now.minusSeconds(80L), Somewhere, No_Tags)(DummyPayload)
        val future = test ? Tracker.Query("id", now.minusSeconds(95))(DummyPayload)
        val result = future.value.get.get.asInstanceOf[Track]
        result.waypoints.length should be (2)
      }
      "respond with all since a time by tags" ignore {}
      "respond with all since a time by id and tags" ignore {}
    }
  }

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }
}
