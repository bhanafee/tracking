package com.example.track

import akka.actor.ActorSystem

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

object Main extends App {
	implicit val system = ActorSystem("TrackSystem")
	implicit val materializer = ActorMaterializer() 

    // TODO: set up OpenTracing default NO-OP, configurable. Wrap every request as a span.

    // anything for logger configuration?
    // how to log access?
    
	// set up tracker(s) - by id? how to handle sharding?
	// set up tagger - if URL configured. taggers may be *configured* as pool

	val route =
		// TODO: parse JSON POSTed requests { id, lon, lat, time? }
    	pathPrefix("track") {
    		pathEndOrSingleSlash {
    			post {
    				// TODO: Add timestamp if needed and tell tracker
    				complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Can't parse JSON with id yet!</h1>"))
    			} ~
    			get {
    				// TODO: Ask tracker
    				complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Do you really want it all?</h1>"))
    			}
    		} ~
      		path (Segment) { id =>
      			pathEnd {
      				get {
      					// TODO: Ask tracker
 	     				parameter('since) { since =>
			        		complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>About $id since $since</h1>"))
      					} ~
			        	complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Latest for $id</h1>"))
     				} ~
 	     			post {
 	     				// TODO: Add timestamp if needed and tell tagger
	    				complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Can't parse JSON for $id yet!</h1>"))
	    			}
     			}
 	    	}
		}

    // TODO: make address configurable, default to localhost and 80
	val binding = Http().bindAndHandle(route, "localhost", 8080)
}