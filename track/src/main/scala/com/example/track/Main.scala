package com.example.track

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

object Main extends App {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val timeout: Timeout = Timeout(config.getInt("http.timeout").seconds)

  implicit val system: ActorSystem = ActorSystem("TrackSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val tracker: ActorRef = system.actorOf(Props[Tracker], "tracker")
  val api = new RestApi(tracker)

  val binding = Http().bindAndHandle(api.route, host, port)
}
