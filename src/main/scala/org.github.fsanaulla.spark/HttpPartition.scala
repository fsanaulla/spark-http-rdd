package org.github.fsanaulla.spark

import org.apache.spark.Partition

import java.net.URI

final case class HttpPartition(index: Int, uri: URI) extends Partition
