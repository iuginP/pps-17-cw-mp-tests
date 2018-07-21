package it.cwmp.testing

import io.vertx.lang.scala.ScalaVerticle
import org.scalatest.BeforeAndAfterEach

trait VerticleBeforeAndAfterEach extends VerticleTest with BeforeAndAfterEach {
  this: VertxTest =>

  /**
    * It contains the list of verticles to deploy before each test.
    * @return a [[Traversable]] containing all the verticles to deploy
    */
  protected def verticlesBeforeEach: Traversable[ScalaVerticle]

  override protected def beforeEach(): Unit =  {
    super.beforeEach()
    deployAll(verticlesBeforeEach)
  }

  override protected def afterEach(): Unit = {
    super.afterEach()
    undeployAll()
  }
}
