package org.github.fsanaulla.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, Suite}

trait SparkBase extends BeforeAndAfterAll { suite: Suite =>

  protected var sc: SparkContext    = _
  protected var spark: SparkSession = _

  override def beforeAll(): Unit = {
    val conf = new SparkConf().setAppName("appName").setMaster("local[*]")
    spark = SparkSession.builder.config(conf).getOrCreate()
    sc = spark.sparkContext
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    sc.stop()
    super.afterAll()
  }
}
