package io.allquantor.scemini.client.gemini

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, _}
import akka.stream._
import akka.stream.scaladsl._
import io.allquantor.scemini.adt.gemini.GeminiConstants.CurrencyPairs.CurrencyPair
import io.allquantor.scemini.adt.gemini.GeminiEvents.GeminiEvent
import io.allquantor.scemini.client.ExchangePlatformClient
import io.allquantor.scemini.materialization.GeminiMarketReads
import io.circe.parser._

import scala.concurrent.Future
import scala.concurrent.duration._


class GeminiMarkets(currencyPairs: Seq[CurrencyPair],
                    uri: String)(implicit system: ActorSystem)
  extends ExchangePlatformClient with GeminiMarketReads {

  // Requests for given currency pairs.
  private val requests = currencyPairs.map(pair =>
    (Http().webSocketClientFlow(WebSocketRequest(s"$uri/$pair")), pair))

  private val lifting = {

    implicit class Parser(s: String) {
      def transform(implicit c: CurrencyPair) = parse(s)
        .flatMap(_.as[GeminiEvent])
        .map(_.copy(currencyPair = Some(c)))
    }

    Flow[(Message, CurrencyPair)].collect {
      case (elem@(m: Message, c: CurrencyPair)) =>
        implicit val currencyPair: CurrencyPair = c
        (m: @unchecked) match {
          case TextMessage.Strict(msg) => Future(msg.transform)
          case TextMessage.Streamed(stream) => stream.limit(100)
            .completionTimeout(5000.millis)
            .runFold("")(_ + _).map(_.transform)
        }
    }.mapAsync(4)(identity)
  }

  lazy val source = Source.fromGraph(GraphDSL.create() { implicit b =>

    import GraphDSL.Implicits._
    // For infinite incoming stream.
    val source = Source.maybe[Message]

    // create a merge shape for all currency pairs
    val merge = b.add(Merge[(Message, CurrencyPair)](requests.size))

    // merge
    requests.zipWithIndex.foreach { case ((flow, c), i) =>
      source ~> flow.map((_, c)) ~> merge.in(i)
    }

    SourceShape(merge.out.via(lifting).outlet)
  })
}


