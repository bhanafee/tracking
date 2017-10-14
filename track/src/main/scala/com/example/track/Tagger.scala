package com.example

import akka.actor.{ Actor, ActorLogging, ActorRef }
import akka.event.LoggingReceive

import java.net.URL

import com.example.track.Tracker.Observation

class Tagger(locator: URL, weather: URL, tracker: ActorRef) extends Actor with ActorLogging {

	// TODO: create materializer with timeout
	// TODO: Add OpenTracing
	override def receive = LoggingReceive {
		case o @ Observation(_, _, Some(position), existing) =>
			// concurrent query weather and locator
			// observations are one-way, so don't worry about tell vs forward
			// on success, augment existing tags and pass along to tracker
			// on failure, pass along to tracker as-is
		case  m => tracker forward m
	}
}