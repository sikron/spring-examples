# Description

This project is about Spring Websockets. There exist 3 sub projects

* `lowlevel`
The websockets are realized by Handlers. The browser standard socket implementation is used.
* `sockjs`
Basically the same as 'lowlevel', but as socket implementation SockJS is used. 
'lowlevel' and 'sockjs' were adapted from https://github.com/habuma/SpringInActionExamples/tree/master/Chapter_18
Many thx for that! :-)
* `broker`
Basically the same as 'sockjs', but for sending messages out, a in-memory broker is used. Also the STOMP protocol is used
here. The Web layer provides a websocket endpoint, which accepts messages and sends them to a queue on a certain topic. 
Listeners on this queue-topic then get the message.
The 'broker' project was copied from https://github.com/lynas/Spring4withWebsocket in order to have a working setup for integration testing and adapted a little bit.
Many thx for that! :-)

# Purpose

* Getting to know websockets on server side and websocket clients by creating Integration Tests

# Requirements

* none

# What can it do?

* lowlevel
    * start the server via the maven jetty plugin, e.g. `mvn jetty:run`
    * go to `http://localhost:8080/test.html`
    * see the ping pong messages in the browser console and jetty output
* sockjs
    * same as 'lowlevel'
* broker
    * start the websocket server in the `communication` module 
    * go to `http://localhost:8080/test.html`, connect and send greetings :-)
    or
    * just execute the `com.skronawi.spring.examples.websockets.communication.WebsocketIntegrationTest` and debug in the callback-methods to see what's going on
    * for the Integration Tests i use Spring Boot in order to initialize a real in-memory web app as MVCMock does not support websockets