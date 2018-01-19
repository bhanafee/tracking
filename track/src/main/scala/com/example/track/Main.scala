package com.example.track

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import brave.opentracing.BraveTracer
import brave.Tracing
import com.typesafe.config.ConfigFactory
import zipkin2.reporter.AsyncReporter
import zipkin2.reporter.okhttp3.OkHttpSender

import scala.concurrent.duration._

object Main extends App {
  val config = ConfigFactory.load()

  /* Set up tracing */
  val zipkinHost = config.getString("zipkin.host")
  val zipkinPort = config.getInt("zipkin.port")
  val tracer = BraveTracer.create(
    Tracing.newBuilder
      .localServiceName("track")
      .spanReporter(
        AsyncReporter.create(
          OkHttpSender.create(s"http://$zipkinHost:$zipkinPort/api/v2/spans")
        ))
      .build
  )

  /* Prepare HTTP listener */
  implicit val timeout: Timeout = Timeout(config.getInt("http.timeout").seconds)

  /* Set up actor system */
  implicit val system: ActorSystem = ActorSystem("TrackSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val tracker: ActorRef = system.actorOf(Props(classOf[Tracker], tracer), "tracker")

  /* Set up API and start listening */
  val api = new RestApi(tracker)
  val host = config.getString("http.host")
  val port = config.getInt("http.port")
  val binding = Http().bindAndHandle(api.route, host, port)
}
