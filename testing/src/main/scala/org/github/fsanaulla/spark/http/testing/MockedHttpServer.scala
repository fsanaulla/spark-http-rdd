package org.github.fsanaulla.spark.http.testing

import org.github.fsanaulla.spark.http.testing.MockedHttpServer.SetRoutes
import org.mockserver.integration.ClientAndServer
import org.mockserver.mock.Expectation
import org.scalatest.{BeforeAndAfterAll, Suite}

import scala.util.Random

/**
  * Simple HTTP web server mock
  */
trait MockedHttpServer extends BeforeAndAfterAll { self: Suite =>

  /* port that should be used during test execution */
  def mockServerPort: Int

  // Example
  //
  // new ClientAndServer(1080)
  //    .when(
  //        request()
  //            .withMethod("GET")
  //            .withPath("/view/cart")
  //            .withCookies(
  //                cookie("session", "4930456C-C718-476F-971F-CB8E047AB349")
  //            )
  //            .withQueryStringParameters(
  //                param("cartId", "055CA455-1DF7-45BB-8535-4F83E7266092")
  //            )
  //    )
  //    .respond(
  //        response()
  //            .withBody("some_response_body")
  //    );
  def expectations: SetRoutes

  // lazy because, we should init it in beforeAll
  private lazy val server: ClientAndServer =
    new ClientAndServer(mockServerPort)

  override def beforeAll(): Unit = {
    expectations(server)
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    server.stop()
    checkIfRunning()
    super.afterAll()
  }

  private def checkIfRunning(): Unit = {
    if (!server.isRunning) ()
    else {
      Thread.sleep(1000)
      checkIfRunning()
    }
  }
}

object MockedHttpServer {
  type SetRoutes = ClientAndServer => Array[Expectation]
}
