package org.github.fsanaulla.spark

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, Suite}

trait SparkBase extends BeforeAndAfterAll { suite: Suite =>

  protected var sc: SparkContext = _

  override def beforeAll(): Unit = {
    val conf = new SparkConf().setAppName("appName").setMaster("local[*]")
    sc = new SparkContext(conf)
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    sc.stop()
    super.afterAll()
  }
}
