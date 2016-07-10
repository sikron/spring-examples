# Description

This project is about the integration of AMQP into Spring. AMQP ("Advanced Message Queue Protocol")
is a protocol for exchanging messages. It is supported e.g. in RabbitMQ

# Purpose

* Getting to know AMQP
* Understanding the difference of talking to RabbitMQ directly and talking to the generic AMQP provider
* Getting to know, how different queues can be managed

# Requirements

* a running instance of RabbitMQ, e.g. a docker container from `https://hub.docker.com/_/rabbitmq/`
  * `sudo docker run -d --hostname my-rabbit --name some-rabbit rabbitmq:3-management` to get the image with management plugin
  * `sudo docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management` to run
  it with the management plugin accessible in the browser via `http://localhost:15672` and with port 5672 reachable by
  producers and consumers (credentials are always guest/guest)

# Sub Projects

* `simple` is just a hello-world-like starter project to get a first little working example
  * start the rabbitMq, start `Consumer.main()`, then start `Producer.main()`
  * see the consumer getting the messages
  * also first the Producer can be started, the Consumer later then fetches all accumulated messages
  * also several Consumers can be started, the messages then get distributed via Round-Robin
* `javaconfig` is the same as `simple`, only with a java configuration instead of xml
* `deadletter` is an example, how to configure a dead-letter queue for a working queue, e.g. in case of time-to-live timeout
* `multi-queue-client` is a client for using 3 different queues, one working queue, one retry-queue in case of
client-processing-failure and one dead-letter queue for final gathering of undeliverable messages