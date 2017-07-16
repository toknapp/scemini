package io.allquantor.scemini.materialization

import io.allquantor.orders.adt.gemini.GeminiEvents.GeminiEvent
import io.allquantor.orders.materialization.GeminiMarketReads
import io.circe.parser._
import org.scalatest.{Matchers, WordSpec}


class GeminiMarketReadsTest extends WordSpec with Matchers with GeminiTestData with GeminiMarketReads {

  "Gemini Json Transformer" when {
    "applied on single change event" should {
      val json = parse(singleChangeEvent)
      val result = json.flatMap(_.as[GeminiEvent])
      "parse correct type" in {
        json.isRight shouldBe true
        result.isRight shouldBe true
        result.map(r => r shouldBe an[GeminiEvent])
      }
      "parse correct results" in {
        result.foreach { r =>
          r.eventId shouldEqual expectedSingleChangeResult.eventId
          r.events shouldEqual expectedSingleChangeResult.events
        }
      }
      "should work with multiple change events correctly" in {
        val multiple = parse(multipleChangeEvents)
        val mResult = multiple.flatMap(_.as[GeminiEvent])
        mResult.foreach { r =>
          r.events should contain theSameElementsInOrderAs multipleChangeEventsExpectedResult.events
          r.eventId shouldEqual multipleChangeEventsExpectedResult.eventId
        }
      }
    }
  }


  "A Gemini Json Transformer" when {
    "Applied on a trade,change event" should {
      val json = parse(tradeChangeEvent)
      json.left.foreach(println)
      val result = json.flatMap(_.as[GeminiEvent])
      result.left.foreach(println)
      "parse the correct type" in {
        json.isRight shouldBe true
        result.isRight shouldBe true
        result.map(r => r shouldBe an[GeminiEvent])
      }

      "parse the correct result" in {
        result.foreach { r =>
          r.events should contain theSameElementsInOrderAs expectedTradeChangeEventResult.events
          r.eventId shouldEqual expectedTradeChangeEventResult.eventId
        }
      }
    }
  }

  "A Gemini Json Transformer" when {
    "Applied on a auction_indicative event" should {
      val json = parse(singleAuctionIndicativeEvent)
      json.left.foreach(println)
      val result = json.flatMap(_.as[GeminiEvent])
      result.left.foreach(println)
      "parse the correct type" in {
        json.isRight shouldBe true
        result.isRight shouldBe true
        result.map(r => r shouldBe an[GeminiEvent])
      }

      "parse the correct result" in {
        result.foreach { r =>
          r.events should contain theSameElementsInOrderAs auctionIndicativeExpectedResult.events
          r.eventId shouldEqual auctionIndicativeExpectedResult.eventId
        }
      }
    }
  }

  "A Gemini Json Transformer" when {
    "Applied on a auction_open event" should {
      val json = parse(singleAuctionOpenEvent)
      json.left.foreach(println)
      val result = json.flatMap(_.as[GeminiEvent])
      result.left.foreach(println)
      "parse the correct type" in {
        json.isRight shouldBe true
        result.isRight shouldBe true
        result.map(r => r shouldBe an[GeminiEvent])
      }

      "parse the correct result" in {
        result.foreach { r =>
          r.events should contain theSameElementsInOrderAs auctionOpenExpectedResult.events
          r.eventId shouldEqual auctionOpenExpectedResult.eventId
        }
      }
    }
  }

  "A Gemini Json Transformer" when {
    "Applied on a trade-auction_result event" should {
      val json = parse(tradeAuctionEvent)
      json.left.foreach(println)
      val result = json.flatMap(_.as[GeminiEvent])
      result.left.foreach(println)
      "parse the correct type" in {
        json.isRight shouldBe true
        result.isRight shouldBe true
        result.map(r => r shouldBe an[GeminiEvent])
      }

      "parse the correct result" in {
        result.foreach { r =>
          r.events should contain theSameElementsInOrderAs expectedTradeAuctionEventResult.events
          r.eventId shouldEqual expectedTradeAuctionEventResult.eventId
        }
      }
    }
  }

}



