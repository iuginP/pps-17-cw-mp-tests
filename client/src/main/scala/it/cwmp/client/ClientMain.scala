package it.cwmp.client

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import it.cwmp.client.controller.ClientControllerActor

object ClientMain extends App {
  val APP_NAME = "ClientApp"

  private val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=0").withFallback(ConfigFactory.load())

  val system = ActorSystem(APP_NAME, config)

  val clientControllerActor = system.actorOf(Props(classOf[ClientControllerActor], system), ClientControllerActor.getClass.getName)
}
