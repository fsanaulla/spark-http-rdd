/*
 * Copyright 2021 Faiaz Sanaulla
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.github.fsanaulla.spark.http.testing

import java.net.URI

object UriSyntax {
  implicit final class UriOps(private val uri: URI) extends AnyVal {
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
