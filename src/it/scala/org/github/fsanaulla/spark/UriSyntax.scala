package org.github.fsanaulla.spark

import java.net.URI

object UriSyntax {

  implicit final class UriOps(private val uri: URI) extends AnyVal {
    def addQueryParam(key: String, value: String): URI =
      if (uri.getQuery != null) URI.create(s"$uri&$key=$value")
      else URI.create(uri + "?" + key + "=" + value)

    def addQueryParams(params: List[(String, String)]): URI = {
      val paramsStr = params
        .map { case (k, v) => k + "=" + v }
        .mkString("&")

      if (uri.getQuery != null) URI.create(s"$uri&$paramsStr")
      else URI.create(uri.toString + "?" + paramsStr)
    }

    def addPath(path: String): URI =
      if (uri.toString.endsWith("/") && path.startsWith("/"))
        URI.create(uri.toString + path.drop(1))
      else if (uri.toString.endsWith("/") ^ path.startsWith("/"))
        URI.create(uri.toString + path)
      else if (!uri.toString.endsWith("/") && !path.startsWith("/"))
        URI.create(uri.toString + "/" + path)
      else throw new IllegalArgumentException(s"Invalid URI: $uri$path")
  }
}
