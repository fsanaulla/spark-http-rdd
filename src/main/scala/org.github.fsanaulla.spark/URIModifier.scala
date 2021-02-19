package com.datategy.spark.extensions

import java.net.URI

trait URIModifier extends Serializable {
  def modify(uri: URI): URI
}

object URIModifier {
  def fromFunction(f: URI => URI): URIModifier = new URIModifier {
    override def modify(uri: URI): URI = f(uri)
  }
}
