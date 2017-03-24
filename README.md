# cinnamon-slf4j-mdc-test
A small study project on lightbend cinnamon and slf4j MDC for futures and actors

## Introduction
Asynchronous boundaries are one of the primary challenges behind monitoring distributed systems. It is difficult to reason
about behavior when stuff does not happen in the order we think it should. To manage this, [Lightbend Monitoring](https://developer.lightbend.com/docs/monitoring/latest/home.html)
provides context propagation in the form of [Mapped Diagnostic Context (MDC)](https://developer.lightbend.com/docs/monitoring/latest/extensions/mdc.html)
and [Spans](https://developer.lightbend.com/docs/monitoring/latest/extensions/spans.html). You can think of them as buckets designed to capture data
of a particular type or path regardless of when or where it occurs.

## What is MDC?
The Mapped Diagnostic Context or MDC is used for adding extra identifiers to log messages. The Mapped Diagnostic Context (MDC) is a
thread-local key-value store that can be accessed from a Log4j conversion pattern. The Cinnamon agent fixes the thread-local aspect
of MDC and assures that the correct MDC will be mapped through the managed threads as used by Scala Futures and Akka dispatchers.

To use the MDC, you have to put a placeholder into the respective Log4j conversion pattern (%X) in your logback.xml:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration debug="false">

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%logger{15} - id=%X{id} - %message%n%xException{10}</pattern>
        </encoder>
    </appender>

    <logger name="play" level="WARN" />
    <logger name="akka" level="WARN" />
    <logger name="com.lightbend.lagom" level="WARN" />
    <logger name="org.apache.cassandra" level="ERROR" />
    <logger name="com.datastax.driver" level="ERROR" />
    <logger name="org.apache.kafka" level="WARN" />
    <logger name="com.github.dnvriend" level="DEBUG" />

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

## Resources
- [akka-cluster-cinnamon - Dave Sugden](https://github.com/dsugden/akka-cluster-cinnamon)
- [Cinnamon Futures - Lightbend](https://github.com/lightbend/cinnamon-futures)
- [Logging Contextual Info in an Asynchronous Scala Application](http://code.hootsuite.com/logging-contextual-info-in-an-asynchronous-scala-application/)

Have fun!