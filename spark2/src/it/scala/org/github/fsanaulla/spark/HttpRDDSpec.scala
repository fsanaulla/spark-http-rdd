package org.github.fsanaulla.spark

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.github.fsanaulla.spark.http.HttpRDD
import org.github.fsanaulla.spark.http.core.URIModifier
import org.github.fsanaulla.spark.http.testing.MockedHttpServer
import org.github.fsanaulla.spark.http.testing.UriSyntax._
import org.mockserver.integration.ClientAndServer
import org.mockserver.mock.Expectation
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import java.net.URI

class HttpRDDSpec extends AnyFreeSpec with Matchers with MockedHttpServer with SparkBase {
  override def mockServerPort: Int = 9998
  override def expectations: ClientAndServer => Array[Expectation] = { cs =>
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

      val rd: String => Row = Row(_)
      val schema: StructType =
        StructType(StructField("result", StringType, nullable = false) :: Nil)
      val rdd =
        HttpRDD.create(
          sc,
          URI.create(s"http://localhost:$mockServerPort"),
          uriPartitioner,
          rd
        )

      val httpRdd = spark.createDataFrame(rdd, schema)

      httpRdd.collect().toList shouldEqual List(Row("{\"text\":1}"), Row("{\"text\":2}"))
    }
  }
}
