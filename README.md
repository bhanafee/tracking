# Cloud experimentation

## [Web](web)
Typical web front end. It is a **Java 8** application that runs on **SpringMVC** and **Tomcat**. It makes use of the other services in this project.

## [Basics](basics)
Simple query service. It is a **Reactive Spring** service written in **Java 9** using **WebFlux**. The data it returns is based on Java internal libraries.

## Geolocation
Contains two modules:

### [Queries](geolocation/queries)
Service that calculates which geographic areas cover a coordinate. It is a **Kotlin** application built on **Spring REST**, and accesses a **PostGIS** database.

### [Administration](geolocation/administration)
Loads data to the geolocation database. It is a **Java 8** application built on the **Play Framework**.

## [Track](track)
Tracks geographic observations over time. It is written in **Scala** and built on **Akka HTTP**.

## [Weather](weather)
Proxies an external service to get weather reports for specific locations. It is written in **C#** and runs on **.NET Core**
