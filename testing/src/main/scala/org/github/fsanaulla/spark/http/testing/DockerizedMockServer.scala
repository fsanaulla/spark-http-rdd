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

import com.dimafeng.testcontainers.{ForAllTestContainer, GenericContainer}
import org.scalatest.Suite
import org.testcontainers.containers.output.OutputFrame.OutputType
import org.testcontainers.containers.output.ToStringConsumer
import org.testcontainers.containers.wait.strategy.Wait

import java.io.File

trait DockerizedMockServer extends ForAllTestContainer { self: Suite =>
  def mockServerPort = 1080

  override val container: GenericContainer = {
    GenericContainer(
      "faiaz/mockserver-testing:0.2.0",
      exposedPorts = Seq(mockServerPort),
      waitStrategy = Wait.forLogMessage(".*started on port: 1080.*\n", 1)
    )
  }

  override def afterStart(): Unit = {
    container.configure(_.followOutput(new ToStringConsumer, OutputType.STDOUT))
    super.afterStart()
  }

  def mockServerMappedHost: String =
    container.underlyingUnsafeContainer.getContainerIpAddress
  def mockServerMappedPort: Int =
    container.underlyingUnsafeContainer.getLivenessCheckPortNumbers.toArray.head
      .asInstanceOf[Int]
  def mockServerUrl: String =
    "http://" + mockServerMappedHost + ":" + mockServerMappedPort

}
