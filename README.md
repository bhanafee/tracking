# Cloud experimentation

* **Web** is a typical front end. It is a Java application that runs on **SpringMVC** and **Tomcat**. It makes use of the other services in this project.
* **Basics** is a simple query service. It is a **Reactive Spring** service written in Java using **WebFlux**. The data it returns is based on Java internal libraries.
* **Geolocation** contains two modules, as follows:
** **Queries** is a service that calculates which geographic areas cover a coordinate. It is built on the **Play Framework** and accesses a PostGIS database.
** **Administration** loads data to the geolocation database. It is a Java application built on **Spring REST** and **Tomcat**.
* **Track** tracks geographic observations over time. It is written in Scala and built on **Akka HTTP**.
* **Weather** proxies an external service to get weather reports for specific locations. It is written in F# and runs on **.NET Core**
