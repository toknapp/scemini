/**
  *
  * @author Ivan Morozov (allquantor)
  *
  */

package io.allquantor.scemini.client

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import io.allquantor.scemini.adt.gemini.GeminiConstants.CurrencyPairs
import io.allquantor.scemini.adt.gemini.GeminiConstants.CurrencyPairs.CurrencyPair
import io.allquantor.scemini.client.gemini.GeminiMarkets

import scala.concurrent.ExecutionContext


abstract class ExchangePlatformClient(implicit system: ActorSystem) {

  protected[this] implicit val actorSystem: ActorSystem = system
  protected[this] implicit val ec: ExecutionContext = actorSystem.dispatcher
  protected[this] implicit val mat: Materializer =  ActorMaterializer.create(system)
  protected [this] val CPUec = ExchangePlatformClient.CPUec
}

object ExchangePlatformClient extends ClientConfig.GeminiConfig {

  private lazy val defaultCurrencyPairs = GeminiCurrencyPairs.map(CurrencyPairs.fromString)

  private def apply(currencyPairs: Seq[CurrencyPair])(implicit actorSystem: ActorSystem): GeminiMarkets = {
    new GeminiMarkets(currencyPairs, s"$GeminiSocketApi/$GeminiPublicMarketUri")
  }

  def asGeminiClient(currencyPairs: Seq[CurrencyPair] = defaultCurrencyPairs)(implicit actorSystem: ActorSystem): GeminiMarkets = {
    this.apply(currencyPairs)
  }

  val CPUec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))
}


