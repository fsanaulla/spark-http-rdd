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

import com.dimafeng.testcontainers.{
  DockerComposeContainer,
  ExposedService,
  ForAllTestContainer,
  ServiceLogConsumer
}
import org.scalatest.Suite
import org.slf4j.LoggerFactory
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait

import java.io.File

trait DockerizedMockServer extends ForAllTestContainer { self: Suite =>
  def dockerComposeName: String = "docker-compose.yml"
  def mockServerName            = "mockserver_1"
  def mockServerPort            = 1080

  override val container: DockerComposeContainer = {
    DockerComposeContainer(
      new File(
        getClass.getClassLoader.getResource(dockerComposeName).getPath
      ),
      exposedServices = ExposedService(
        mockServerName,
        1080,
        waitStrategy = Wait.forLogMessage(".*started on port: 1080.*\n", 1)
      ) :: Nil,
      logConsumers = ServiceLogConsumer(
        mockServerName,
        new Slf4jLogConsumer(LoggerFactory.getLogger(getClass.getName))
      ) :: Nil
    )
  }

  def mockServerMappedHost: String =
    container.getServiceHost(mockServerName, mockServerPort)
  def mockServerMappedPort: Int =
    container.getServicePort(mockServerName, mockServerPort)
  def mockServerUrl: String =
    "http://" + mockServerMappedHost + ":" + mockServerMappedPort

}
