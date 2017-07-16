package io.allquantor.scemini.materialization

import io.allquantor.orders.adt.gemini.GeminiConstants.{GeminiAuctionResults, GeminiEventReasons, GeminiSides}
import io.allquantor.orders.adt.gemini.GeminiEvents._

trait GeminiTestData {

  val multipleChangeEvents =
    """{
      |    "eventId": 371464230,
      |    "events": [
      |        {
      |            "delta": "20914.3",
      |            "price": "0.01",
      |            "reason": "initial",
      |            "remaining": "20914.3",
      |            "side": "bid",
      |            "type": "change"
      |        },
      |        {
      |            "delta": "100",
      |            "price": "0.10",
      |            "reason": "initial",
      |            "remaining": "100",
      |            "side": "bid",
      |            "type": "change"
      |        },
      |        {
      |            "delta": "0.1496",
      |            "price": "999999.00",
      |            "reason": "initial",
      |            "remaining": "0.1496",
      |            "side": "ask",
      |            "type": "change"
      |        },
      |        {
      |            "delta": "0.001",
      |            "price": "1000000.00",
      |            "reason": "initial",
      |            "remaining": "0.001",
      |            "side": "ask",
      |            "type": "change"
      |        }
      |    ],
      |    "type": "update"
      |}        """.stripMargin

  val multipleChangeEventsExpectedResult = GeminiEvent(eventId = 371464230, Seq(
    ChangeEvent(
      delta = BigDecimal(20914.3),
      price = BigDecimal(0.01),
      reason = GeminiEventReasons.initial,
      remaining = BigDecimal(20914.3),
      side = GeminiSides.bid
    ),
    ChangeEvent(
      delta = BigDecimal(100),
      price = BigDecimal(0.10),
      reason = GeminiEventReasons.initial,
      remaining = BigDecimal(100),
      side = GeminiSides.bid
    ),
    ChangeEvent(
      delta = BigDecimal(0.1496),
      price = BigDecimal(999999.00),
      reason = GeminiEventReasons.initial,
      remaining = BigDecimal(0.1496),
      side = GeminiSides.ask
    ),
    ChangeEvent(
      delta = BigDecimal(0.001),
      price = BigDecimal(1000000.00),
      reason = GeminiEventReasons.initial,
      remaining = BigDecimal(0.001),
      side = GeminiSides.ask
    )
  ))


  val singleChangeEvent =
    """{
      |    "eventId": 371468177,
      |    "events": [
      |        {
      |            "delta": "2.80081364",
      |            "price": "1052.77",
      |            "reason": "place",
      |            "remaining": "2.80081364",
      |            "side": "ask",
      |            "type": "change"
      |        }
      |    ],
      |    "type": "update"
      |}""".stripMargin


  val expectedSingleChangeResult = GeminiEvent(eventId = 371468177, Seq(ChangeEvent(
    delta = BigDecimal(2.80081364),
    price = BigDecimal(1052.77),
    reason = GeminiEventReasons.place,
    remaining = BigDecimal(2.80081364),
    side = GeminiSides.ask
  )))


  val tradeChangeEvent =
    """{
      |    "eventId": 371465341,
      |    "events": [
      |
      |
      |        {
      |            "amount": "0.03194276",
      |            "makerSide": "ask",
      |            "price": "1050.98",
      |            "tid": 371465341,
      |            "type": "trade"
      |        },
      |
      |
      |        {
      |            "delta": "-0.03194276",
      |            "price": "1050.98",
      |            "reason": "trade",
      |            "remaining": "14.57589724",
      |            "side": "ask",
      |            "type": "change"
      |        }
      |    ],
      |    "type": "update"
      |}""".stripMargin


  val expectedTradeChangeEventResult = GeminiEvent(eventId = 371465341,
    events = Seq(
      TradeEvent(
        amount = BigDecimal(0.03194276),
        makerSide = GeminiSides.ask,
        price = BigDecimal(1050.98),
        tid = 371465341
      ),
      ChangeEvent(
        delta = BigDecimal(-0.03194276),
        price = BigDecimal(1050.98),
        reason = GeminiEventReasons.trade,
        remaining = BigDecimal(14.57589724),
        side = GeminiSides.ask
      )
    )
  )


  val tradeAuctionEvent =
    """{
      |    "eventId": 371469414,
      |    "events": [
      |        {
      |            "amount": "1406",
      |            "makerSide": "auction",
      |            "price": "1048.75",
      |            "tid": 371469414,
      |            "type": "trade"
      |        },
      |        {
      |            "auction_price": "1048.75",
      |            "auction_quantity": "1406",
      |            "eid": 371469414,
      |            "highest_bid_price": "1050.98",
      |            "lowest_ask_price": "1050.99",
      |            "result": "success",
      |            "time_ms": 1486501200000,
      |            "type": "auction_result"
      |        }
      |    ],
      |    "type": "update"
      |}""".stripMargin

  val expectedTradeAuctionEventResult = GeminiEvent(eventId = 371469414,
    events = Seq(
      TradeEvent(
        amount = BigDecimal(1406),
        makerSide = GeminiSides.auction,
        price = BigDecimal(1048.75),
        tid = 371469414
      ),
      AuctionResult(
        auction_price = BigDecimal(1048.75),
        auction_quantity = BigDecimal(1406),
        eid = 371469414,
        highest_bid_price = BigDecimal(1050.98),
        lowest_ask_price = BigDecimal(1050.99),
        result = GeminiAuctionResults.success,
        time_ms = 1486501200000l
      )
    )
  )


  val singleAuctionOpenEvent =
    """{
      |    "eventId": 372481811,
      |    "events": [
      |        {
      |            "auction_open_ms": 1486591200000,
      |            "auction_time_ms": 1486674000000,
      |            "first_indicative_ms": 1486673400000,
      |            "last_cancel_time_ms": 1486673985000,
      |            "type": "auction_open"
      |        }
      |    ],
      |    "type": "update"
      |}""".stripMargin

  val auctionOpenExpectedResult = GeminiEvent(eventId = 372481811, Seq(AuctionOpen(
    auction_open_ms = 1486591200000l,
    auction_time_ms = 1486674000000l,
    first_indicative_ms = 1486673400000l,
    last_cancel_time_ms = 1486673985000l
  )))


  val singleAuctionIndicativeEvent =
    """{
      |    "eventId": 371467219,
      |    "events": [
      |        {
      |            "eid": 371467219,
      |            "highest_bid_price": "1049.07",
      |            "indicative_price": "1048.6",
      |            "indicative_quantity": "1744.28868927",
      |            "lowest_ask_price": "1050.98",
      |            "result": "success",
      |            "time_ms": 1486500600000,
      |            "type": "auction_indicative"
      |        }
      |    ],
      |    "type": "update"
      |}""".stripMargin

  val auctionIndicativeExpectedResult = GeminiEvent(eventId = 371467219, Seq(AuctionIndicative(
    eid = 371467219,
    highest_bid_price = BigDecimal(1049.07),
    indicative_price = BigDecimal(1048.6),
    indicative_quantity = BigDecimal(1744.28868927),
    lowest_ask_price = BigDecimal(1050.98),
    result = GeminiAuctionResults.success,
    time_ms = 1486500600000l
  )))

}
