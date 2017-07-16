package io.allquantor.scemini.client.gemini

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.testkit.scaladsl.TestSink
import io.allquantor.orders.adt.gemini.GeminiConstants.CurrencyPairs.CurrencyPair
import io.allquantor.orders.adt.gemini.GeminiEvents.GeminiEvent
import io.allquantor.scemini.client.ExchangePlatformClient
import org.scalatest.{FlatSpec, Matchers}

class GeminiMarketsTest extends FlatSpec with Matchers {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer.create(system)
  implicit val ec = system.dispatcher

  //implicit val timeout = 10.second

  "Gemini MarketClient" should "retrieve gemini sandbox market stream " in {
    val client = ExchangePlatformClient.asGeminiClient()
    val source = client.source

    type ResultType = Either[io.circe.Error, GeminiEvent]
    val testSink = TestSink.probe[ResultType](system)

    source.
      runWith(testSink)
      .ensureSubscription()
      .request(1)
      .expectNextChainingPF(
        { case Right(e:GeminiEvent) => e.currencyPair.get shouldBe an[CurrencyPair] })

  }
}
