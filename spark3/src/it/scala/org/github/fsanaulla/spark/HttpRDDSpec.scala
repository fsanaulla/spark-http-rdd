package org.github.fsanaulla.spark

import org.github.fsanaulla.spark.http.HttpRDD
import org.github.fsanaulla.spark.http.core.URIModifier
import org.github.fsanaulla.spark.http.testing.DockerizedMockServer
import org.github.fsanaulla.spark.http.testing.UriSyntax._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import java.io.File
import java.net.URI

class HttpRDDSpec
    extends AnyFreeSpec
    with DockerizedMockServer
    with Matchers
    with SparkBase {

  "HTTP RDD" - {

    "should be able to fetch data" in {
      val uriPartitioner: Array[URIModifier] = Array(
        URIModifier.fromFunction((uri: URI) => uri.addPath("/data/1")),
        URIModifier.fromFunction(
          _.addPath("/data/2")
        )
      )

      val rdd =
        HttpRDD.create(
          sc,
          URI.create(s"http://$mockServerMappedHost:$mockServerMappedPort"),
          uriPartitioner,
          identity
        )

      rdd.collect().toList shouldEqual List("{\"text\":1}", "{\"text\":2}")
    }
  }
}
