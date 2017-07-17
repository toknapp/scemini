
/**
  *
  * Gemini Data Model of Public Market Data
  * https://docs.gemini.com/websocket-api/#market-data
  *
  * @author Ivan Morozov (allquantor)
  *
  */

package io.allquantor.scemini.adt.gemini

import io.allquantor.scemini.adt.utils.Stringable

object GeminiConstants {

  // Used as Path addition to construct queries for the WebSocket API.

  object CurrencyPairs extends Stringable {

    sealed trait CurrencyPair

    case object btcusd extends CurrencyPair

    case object ethusd extends CurrencyPair

    case object ethbtc extends CurrencyPair

    override type T = CurrencyPair

    case object currencyPairDefault extends CurrencyPair

    val values = Set(btcusd, ethusd, ethbtc)
    override val defaultValue: CurrencyPair = currencyPairDefault
  }

  // Contain all event types gemini market API provides.
  object GeminiEventTypes extends Stringable {

    sealed trait GeminiEventType

    case object trade extends GeminiEventType

    case object change extends GeminiEventType

    case object auction_indicative extends GeminiEventType

    case object auction_open extends GeminiEventType

    case object auction_result extends GeminiEventType

    val values = Set(trade, change, auction_indicative, auction_open, auction_result)

    case object geminiTypeDefault extends GeminiEventType

    override type T = GeminiEventType
    override val defaultValue: GeminiEventType = geminiTypeDefault
  }

  object GeminiEventReasons extends Stringable {

    sealed trait GeminiEventReason

    case object trade extends GeminiEventReason

    case object place extends GeminiEventReason

    case object cancel extends GeminiEventReason

    case object initial extends GeminiEventReason

    val values = Set(trade, initial, cancel, place)

    case object geminiReasonsDefault extends GeminiEventReason

    override type T = GeminiEventReason
    override val defaultValue: GeminiEventReason = geminiReasonsDefault
  }


  object GeminiSides extends Stringable {

    sealed trait GeminiSide

    case object ask extends GeminiSide

    case object bid extends GeminiSide

    case object auction extends GeminiSide

    val values = Set(ask, bid, auction)

    case object geminiSideDefault extends GeminiSide

    override type T = GeminiSide
    override val defaultValue: GeminiSide = geminiSideDefault
  }

  object GeminiAuctionResults extends Stringable {

    sealed trait GeminiAuctionResult

    case object success extends GeminiAuctionResult

    case object failure extends GeminiAuctionResult

    case object resultsDefault extends GeminiAuctionResult

    val values = Set(success, failure)

    override type T = GeminiAuctionResult

    override val defaultValue: GeminiAuctionResult = resultsDefault
  }

}
