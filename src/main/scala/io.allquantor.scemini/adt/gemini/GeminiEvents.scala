
/**
  * Gemini Data Model of Public Market Data
  * https://docs.gemini.com/websocket-api/#market-data
  *
  * @author Ivan Morozov (allquantor)
  *
  *
  */


package io.allquantor.scemini.adt.gemini

import io.allquantor.scemini.adt.MarketEvent
import io.allquantor.scemini.adt.gemini.GeminiConstants.CurrencyPairs.CurrencyPair
import io.allquantor.scemini.adt.gemini.GeminiConstants.GeminiAuctionResults.GeminiAuctionResult
import io.allquantor.scemini.adt.gemini.GeminiConstants.GeminiEventReasons.GeminiEventReason
import io.allquantor.scemini.adt.gemini.GeminiConstants.GeminiEventTypes
import io.allquantor.scemini.adt.gemini.GeminiConstants.GeminiEventTypes.GeminiEventType
import io.allquantor.scemini.adt.gemini.GeminiConstants.GeminiSides.GeminiSide


object GeminiEvents {

  case class GeminiEvent(eventId: Long, events: Seq[GeminiEventDelta],
                         currencyPair: Option[CurrencyPair] = None
                        ) extends MarketEvent

  sealed trait GeminiEventDelta {
    val eventType: GeminiEventType
  }

  case class ChangeEvent(
                          delta: BigDecimal,
                          price: BigDecimal,
                          reason: GeminiEventReason,
                          remaining: BigDecimal,
                          side: GeminiSide
                        ) extends GeminiEventDelta {
    override val eventType: GeminiEventType = GeminiEventTypes.change
  }

  case class TradeEvent(
                         amount: BigDecimal,
                         makerSide: GeminiSide,
                         price: BigDecimal,
                         tid: Long
                       ) extends GeminiEventDelta {
    override val eventType: GeminiEventType = GeminiEventTypes.trade
  }

  case class AuctionResult(
                            auction_price: BigDecimal,
                            auction_quantity: BigDecimal,
                            eid: Long,
                            highest_bid_price: BigDecimal,
                            lowest_ask_price: BigDecimal,
                            result: GeminiAuctionResult,
                            time_ms: Long) extends GeminiEventDelta {
    override val eventType: GeminiEventType = GeminiEventTypes.auction_result
  }

  case class AuctionOpen(
                          auction_open_ms: Long,
                          auction_time_ms: Long,
                          first_indicative_ms: Long,
                          last_cancel_time_ms: Long
                        ) extends GeminiEventDelta {
    override val eventType: GeminiEventType = GeminiEventTypes.auction_open
  }

  case class AuctionIndicative(
                                eid: Long,
                                highest_bid_price: BigDecimal,
                                indicative_price: BigDecimal,
                                indicative_quantity: BigDecimal,
                                lowest_ask_price: BigDecimal,
                                result: GeminiAuctionResult,
                                time_ms: Long) extends GeminiEventDelta {
    override val eventType: GeminiEventType = GeminiEventTypes.auction_indicative
  }

}
