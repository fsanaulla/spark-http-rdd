package org.github.fsanaulla.spark.http

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partition, SparkContext, TaskContext}
import org.github.fsanaulla.spark.http.core.{NextIterator, URIModifier}

import java.io.{BufferedReader, InputStream, InputStreamReader}
import java.net.URI
import scala.collection.JavaConverters.asScalaIteratorConverter
import scala.reflect.ClassTag

/**
  * RDD that fetches data from HTTP endpoints. we may opensource it in the future
  */
final class HttpRDD[T: ClassTag](
    sc: SparkContext,
    uri: URI,
    partitioning: Array[URIModifier],
    iterable: InputStream => Iterator[T],
    requestModifier: HttpGet => HttpGet,
    httpClientBuilder: () => CloseableHttpClient
) extends RDD[T](sc, Nil) {
  override def compute(split: Partition, context: TaskContext): Iterator[T] = {
    new NextIterator[T] {
      context.addTaskCompletionListener[Unit](_ => closeIfNeeded())

      logInfo("Creating HTTP client")

      val client: CloseableHttpClient = httpClientBuilder()
      val part: HttpPartition         = split.asInstanceOf[HttpPartition]
      val uri: URI                    = part.uri

      logInfo(s"Loading data from: $uri")

      // embed partition params that will build partitioned http request
      val request: HttpGet                = requestModifier(new HttpGet(uri))
      val response: CloseableHttpResponse = client.execute(request)

      val is: InputStream       = response.getEntity.getContent
      val iterator: Iterator[T] = iterable(is)

      override def getNext: T = {
        if (iterator.hasNext) iterator.next()
        else {
          finished = true
          null.asInstanceOf[T]
        }
      }

      override protected def close(): Unit = {
        logInfo(s"Closing response. Partition: ${part.index}")
        try {
          if (response != null) response.close()
        } catch {
          case e: Exception => logWarning("Unable to close response", e)
        }

        logInfo(s"Closing client. Partition: ${part.index}")
        try {
          if (client != null) client.close()
        } catch {
          case e: Exception => logWarning("Unable to close client", e)
        }
      }
    }
  }

  override protected def getPartitions: Array[Partition] =
    partitioning.zipWithIndex
      .map { case (mod, index) => HttpPartition(index, mod.modify(uri)) }

}

object HttpRDD {

  /**
    * Base ctor that provide possibility to customize you rdd
    *
    * @param sc                - spark context
    * @param uri               - request uri
    * @param partitioning      - uri partitioner, define separate partitioning uri from the root uri
    * @param requestModifier   - transformation that will be applied to http request before execution
    * @param httpClientBuilder - http client builder
    * @param iterable          - http response content => iterable transformation
    *
    * @return Spark RDD
    */
  def create[T: ClassTag](
      sc: SparkContext,
      uri: URI,
      partitioning: Array[URIModifier],
      requestModifier: HttpGet => HttpGet,
      httpClientBuilder: () => CloseableHttpClient,
      iterable: InputStream => Iterator[T]
  ): RDD[T] =
    new HttpRDD[T](
      sc = sc,
      uri = uri,
      partitioning = partitioning,
      requestModifier = requestModifier,
      iterable = iterable,
      httpClientBuilder = httpClientBuilder
    )

  /**
    * Ctor with predefined amount of functionality, creates RDD from HTTP endpoint
    *
    * @param sc           - spark context
    * @param uri          - request uri
    * @param partitioning - uri partitioner, define separate partitioning uri from the root uri
    * @param mapper       - transformer from String to T
    *
    * By default it expect to receive record per new line with default http client without any kind of request modifications
    *
    * @return Spark RDD
    */
  def create[T: ClassTag](
      sc: SparkContext,
      uri: URI,
      partitioning: Array[URIModifier],
      mapper: String => T
  ): RDD[T] = {
    val requestModifier: HttpGet => HttpGet = identity
    val httpClientBuilder                   = () => HttpClients.createDefault()
    val iteratorBuilder: InputStream => Iterator[T] = { is =>
      new BufferedReader(new InputStreamReader(is))
        .lines()
        .iterator()
        .asScala
        .map(mapper)
    }

    create(
      sc = sc,
      uri = uri,
      partitioning = partitioning,
      requestModifier = requestModifier,
      httpClientBuilder = httpClientBuilder,
      iterable = iteratorBuilder
    )
  }
}
