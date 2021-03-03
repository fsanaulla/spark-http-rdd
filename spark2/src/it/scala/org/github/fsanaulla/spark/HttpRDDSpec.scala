package org.github.fsanaulla.spark

import org.github.fsanaulla.spark.http.HttpRDD
import org.github.fsanaulla.spark.http.core.URIModifier
import org.github.fsanaulla.spark.http.testing.MockedHttpServer
import org.github.fsanaulla.spark.http.testing.MockedHttpServer.SetRoutes
import org.github.fsanaulla.spark.http.testing.UriSyntax._
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import java.net.URI
import scala.util.Random

class HttpRDDSpec extends AnyFreeSpec with Matchers with MockedHttpServer with SparkBase {
  override def mockServerPort: Int = Random.nextInt(4)
  override def expectations: SetRoutes = { cs =>
    cs.when(
        request()
          .withMethod("GET")
          .withPath("/partition")
          .withQueryStringParameter("date", "1")
      )
      .respond(
        response()
          .withBody("{\"text\":1}")
      )

    cs.when(
        request()
          .withMethod("GET")
          .withPath("/partition")
          .withQueryStringParameter("date", "2")
      )
      .respond(
        response()
          .withBody("{\"text\":2}")
      )
  }

  "HTTP RDD" - {

    "should be able to fetch data" in {
      val uriPartitioner: Array[URIModifier] = Array(
        URIModifier.fromFunction(
          (uri: URI) => uri.addPath("/partition?date=1")
        ),
        URIModifier.fromFunction(
          _.addPath("/partition?date=2")
        )
      )

      val rdd =
        HttpRDD.create(
          sc,
          URI.create(s"http://localhost:$mockServerPort"),
          uriPartitioner,
          identity
        )

      rdd.collect().toList shouldEqual List("{\"text\":1}", "{\"text\":2}")
    }
  }
}
