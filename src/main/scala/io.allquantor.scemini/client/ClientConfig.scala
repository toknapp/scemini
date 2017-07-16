package io.allquantor.scemini.client

import com.typesafe.config.ConfigFactory


object ClientConfig {

  import scala.collection.JavaConverters._
  private val config = ConfigFactory.load()

  trait GeminiConfig {
    final val GeminiSocketApi: String = config.getString("gemini.socket.api.address")
    final val GeminiPublicMarketUri: String = config.getString("gemini.makretdata.url")
    final val GeminiCurrencyPairs : List[String] = config.getStringList("gemini.currencypairs").asScala.toList
  }
}
