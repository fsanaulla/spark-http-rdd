# spark-http-rdd

[![Scala CI](https://github.com/fsanaulla/spark-http-rdd/actions/workflows/scala.yml/badge.svg)](https://github.com/fsanaulla/spark-http-rdd/actions/workflows/scala.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fsanaulla/spark3-http-rdd_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.fsanaulla/spark3-http-rdd_2.12)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

## Installation

Add it into your `build.sbt`

### Spark 3

Compiled for scala 2.12

```
libraryDependencies += "com.github.fsanaulla" %% "spark3-http-rdd" % <version>
```

### Spark 2

Cross-compiled for scala 2.11, 2.12

```
libraryDependencies += "com.github.fsanaulla" %% "spark2-http-rdd" % <version>
```

## Usage

Let's define our source URI:

```scala
val baseUri: URI = ???
```

We will build our partitions on top of it using array of `URIModifier` that looks like:

```scala
val uriPartitioner: Array[URIModifier] = Array(
  URIModifier.fromFunction { uri =>
    // uri modification logic, 
    // for example appending path, adding query params etc
  },
  ...
)
```

**Important**: Number of `URIModifier` should be equal to desired number of partitions. Each URI will be used as a
base URI for separate partition

Then we should define the way how we will work with http endpoint responses. By default it expect to receive line
separated number of rows where each row will be processed as separate entity during process of response mapping

```scala
val mapping: String => T = ??? 
```

And then you can create our RDD:

```scala
val rdd: RDD[T] =
  HttpRDD.create(
    sc,
    baseUri,
    uriPartitioner,
    mapping
  )
```

More details available in the source code. Also as an example you can use integration tests