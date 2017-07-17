
/**
  * Gemini Data Model - JsonRead of Public Market Data
  * https://docs.gemini.com/websocket-api/#market-data
  *
  * @author Ivan Morozov (allquantor)
  *
  *
  */

package io.allquantor.scemini.materialization

import io.allquantor.scemini.adt.gemini.GeminiEvents._
import io.allquantor.scemini.adt.gemini.GeminiConstants.{GeminiEventReasons, GeminiAuctionResults, GeminiSides, GeminiEventTypes}
import io.circe.{ACursor, Decoder, DecodingFailure, HCursor}
import io.allquantor.scemini.adt.utils.Stringable


trait GeminiMarketReads {

  // Parsing recursion entry point.
  implicit val eventRead: Decoder[GeminiEvent] = (c: HCursor) => for {
    id <- c.downField("eventId").as[Long]
    events <- c.downField("events").as[Seq[GeminiEventDelta]]
  } yield GeminiEvent(id, events)

  // Identify the event type and run the appropriate reader.
  implicit val eventDeltaRead: Decoder[GeminiEventDelta] = (c: HCursor) => {

    implicit val cursor = c
    val eventType = c.downField("type").to(GeminiEventTypes.fromString)

    eventType.flatMap {
      case GeminiEventTypes.change => changeEventRead
      case GeminiEventTypes.trade => tradeEventRead
      case GeminiEventTypes.auction_open => auctionOpenRead
      case GeminiEventTypes.auction_indicative => auctionIndicativeRead
      case GeminiEventTypes.auction_result => auctionResultRead
      case _ => Decoder.failedWithMessage("Event type is unknown, json could not be decoded").apply(c)
    }
  }

  /**

    * !spec ChangeEvent

    * "delta": "20914.3",
    * "price": "0.01",
    * "reason": "initial",
    * "remaining": "20914.3",
    * "side": "bid",
    * "type": "change"

    */

  private def changeEventRead(implicit c: HCursor) = for {
    delta <- c.downField("delta").as[BigDecimal]
    price <- c.downField("price").as[BigDecimal]
    reason <- c.downField("reason").to(GeminiEventReasons.fromString)
    remaining <- c.downField("remaining").as[BigDecimal]
    side <- c.downField("side").to(GeminiSides.fromString)
  } yield ChangeEvent(delta, price, reason, remaining, side)


  /**
    * !spec TradeEvent
    *
    * "amount": "0.03194276",
    * "makerSide": "ask",
    * "price": "1050.98",
    * "tid": 371465341,
    * "type": "trade"
    *
    */

  private def tradeEventRead(implicit c: HCursor) = for {
    amount <- c.downField("amount").as[BigDecimal]
    markerSide <- c.downField("makerSide").to(GeminiSides.fromString)
    price <- c.downField("price").as[BigDecimal]
    tid <- c.downField("tid").as[Long]
  } yield TradeEvent(amount, markerSide, price, tid)


  /**
    *
    * !spec @AuctionIndicative
    *
    * "eid": 371467219,
    * "highest_bid_price": "1049.07",
    * "indicative_price": "1048.6",
    * "indicative_quantity": "1744.28868927",
    * "lowest_ask_price": "1050.98",
    * "result": "success",
    * "time_ms": 1486500600000,
    * "type": "auction_indicative"
    *
    */

  private def auctionIndicativeRead(implicit c: HCursor) = for {
    eid <- c.downField("eid").as[Long]
    highest_bid_price <- c.downField("highest_bid_price").as[BigDecimal]
    indicative_price <- c.downField("indicative_price").as[BigDecimal]
    indicative_quantity <- c.downField("indicative_quantity").as[BigDecimal]
    lowest_ask_price <- c.downField("lowest_ask_price").as[BigDecimal]
    result <- c.downField("result").to(GeminiAuctionResults.fromString)
    time_ms <- c.downField("time_ms").as[Long]
  } yield AuctionIndicative(eid, highest_bid_price, indicative_price,
    indicative_quantity, lowest_ask_price, result, time_ms)


  /**
    *
    * !spec @AuctionOpen
    *
    * "auction_open_ms": 1486591200000,
    * "auction_time_ms": 1486674000000,
    * "first_indicative_ms": 1486673400000,
    * "last_cancel_time_ms": 1486673985000,
    * "type": "auction_open"
    *
    */

  private def auctionOpenRead(implicit c: HCursor) = for {
    auction_open_ms <- c.downField("auction_open_ms").as[Long]
    auction_time_ms <- c.downField("auction_time_ms").as[Long]
    first_indicative_ms <- c.downField("first_indicative_ms").as[Long]
    last_cancel_time_ms <- c.downField("last_cancel_time_ms").as[Long]
  } yield AuctionOpen(auction_open_ms = auction_open_ms,
    auction_time_ms = auction_time_ms,
    first_indicative_ms = first_indicative_ms,
    last_cancel_time_ms = last_cancel_time_ms)


  /**
    *
    * !spec @AuctionResult
    *
    * "auction_price": "1048.75",
    * "auction_quantity": "1406",
    * "eid": 371469414,
    * "highest_bid_price": "1050.98",
    * "lowest_ask_price": "1050.99",
    * "result": "success",
    * "time_ms": 1486501200000,
    * "type": "auction_result"
    *
    */

  private def auctionResultRead(implicit c: HCursor) = for {
    auction_price <- c.downField("auction_price").as[BigDecimal]
    auction_quantity <- c.downField("auction_quantity").as[BigDecimal]
    eid <- c.downField("eid").as[Long]
    highest_bid_price <- c.downField("highest_bid_price").as[BigDecimal]
    lowest_ask_price <- c.downField("lowest_ask_price").as[BigDecimal]
    result <- c.downField("result").to(GeminiAuctionResults.fromString)
    time_ms <- c.downField("time_ms").as[Long]
  } yield AuctionResult(
    auction_price = auction_price, auction_quantity = auction_quantity,
    eid = eid, highest_bid_price = highest_bid_price,
    lowest_ask_price = lowest_ask_price, time_ms = time_ms, result = result)


  // Parse case objects from as Stringable
  implicit class SingletonReader[T <: Stringable, R](a: ACursor) {
    def to(f: String => R): Either[DecodingFailure, R] = a.as[String].map(f)
  }

}
