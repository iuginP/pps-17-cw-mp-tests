package it.cwmp.services.authentication

import io.vertx.scala.ext.web.client.WebClientOptions
import it.cwmp.services.authentication.ServerParameters._
import it.cwmp.services.testing.authentication.AuthenticationWebServiceTesting
import it.cwmp.testing.{FutureMatchers, HttpMatchers}
import it.cwmp.utils.VertxClient

class AuthenticationServiceVerticleTest extends AuthenticationWebServiceTesting
  with HttpMatchers with FutureMatchers with VertxClient {

  override protected val clientOptions: WebClientOptions = WebClientOptions()
    .setDefaultHost("localhost")
    .setDefaultPort(DEFAULT_PORT)
    .setKeepAlive(false)

  override protected def singupTests(): Unit = {
    it("when right should succed") {
      val username = nextUsername
      val password = nextPassword

      client.post(API_SIGNUP)
        .addAuthentication(username, password)
        .sendFuture()
        .shouldAnswerWith(201, _.exists(body => body.nonEmpty))
    }

    it("when empty header should fail") {
      client.post(API_SIGNUP)
        .sendFuture()
        .shouldAnswerWith(400)
    }

    it("when invalid header should fail") {
      val token = invalidToken
      client.post(API_SIGNUP)
        .addAuthentication(token)
        .sendFuture()
        .shouldAnswerWith(400)
    }

    it("when username already exist should fail") {
      val username = nextUsername
      val password = nextPassword

      client.post(API_SIGNUP)
        .addAuthentication(username, password)
        .sendFuture()
        .flatMap(_ =>
          client.post(API_SIGNUP)
            .addAuthentication(username, password)
            .sendFuture())
        .shouldAnswerWith(400)
    }
  }

  override protected def signoutTests(): Unit = {
    // TODO implement
  }

  override protected def loginTests(): Unit = {
    it("when right should succed") {
      val username = nextUsername
      val password = nextPassword

      client.post(API_SIGNUP)
        .addAuthentication(username, password)
        .sendFuture()
        .flatMap(_ =>
          client.get(API_LOGIN)
            .addAuthentication(username, password)
            .sendFuture())
        .shouldAnswerWith(200, _.exists(body => body.nonEmpty))
    }

    it("when empty header should fail") {
      client.get(API_LOGIN)
        .sendFuture()
        .shouldAnswerWith(400)
    }

    it("when invalid header should fail") {
      val token = invalidToken
      client.get(API_LOGIN)
        .addAuthentication(token)
        .sendFuture()
        .shouldAnswerWith(400)
    }

    it("when user does not exists should fail") {
      val username = nextUsername
      val password = nextPassword

      client.get(API_LOGIN)
        .addAuthentication(username, password)
        .sendFuture()
        .shouldAnswerWith(401)
    }

    it("when password is wrong should fail") {
      val username = nextUsername
      val password = nextPassword
      val passwordWrong = nextPassword

      client.post(API_SIGNUP)
        .addAuthentication(username, password)
        .sendFuture()
        .flatMap(_ =>
          client.get(API_LOGIN)
            .addAuthentication(username, passwordWrong)
            .sendFuture())
        .shouldAnswerWith(401)
    }
  }

  override protected def validationTests(): Unit = {
    it("when right should succed") {
      val username = nextUsername
      val password = nextPassword

      client.post(API_SIGNUP)
        .addAuthentication(username, password)
        .sendFuture()
        .flatMap(response =>
          client.get(API_VALIDATE)
            .addAuthentication(response.bodyAsString().get)
            .sendFuture())
        .shouldAnswerWith(200, _.exists(body => body.nonEmpty))
    }

    it("when missing token should fail") {
      client.get(API_VALIDATE)
        .sendFuture()
        .map(res => res statusCode() should equal(400))
    }

    it("when invalid token should fail") {
      val myToken = invalidToken

      client.get(API_VALIDATE)
        .addAuthentication(myToken)
        .sendFuture()
        .shouldAnswerWith(400)
    }

    it("when unauthorized token should fail") {
      val myToken = nextToken

      client.get(API_VALIDATE)
        .addAuthentication(myToken)
        .sendFuture()
        .shouldAnswerWith(401)
    }
  }
}