# Description

This project is about Spring Websockets. There only exists a dummy Web layer with dummy functionality. 
The Web layer provides a websocket endpoint, which accepts messages and sends them to a queue on a certain topic.
Listeners on this queue-topic then get the message.

This project was copied from https://github.com/lynas/Spring4withWebsocket in order to have a working setup for integration testing and adapted a little bit.
Many thx for that! :-)

# Purpose

* Getting to know websockets on server side
* Getting to know websocket clients by creating Integration Tests

# Requirements

* none

# What can it do?

* start the websocket server via the maven jetty plugin, e.g. `mvn jetty:run`
* go to `http://localhost:8080/test.html`, connect and send greetings :-)
* execute the `com.skronawi.spring.examples.websockets.communication.WebsocketIntegrationTest`. debug in the callback-methods!
* for the Integration Tests i use Spring Boot in order to initialize a real in-memory web app as MVCMock does not support websockets