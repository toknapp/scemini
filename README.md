## Scemini

### Overview

Scemini is an [Akka-Streams](http://akka.io/) client for [Gemini](https://gemini.com) digital asset platform written in Scala.

Scemini is currently available for Scala 2.12

### Getting Started

Create a new GeminiMarkets client

```scala
         val currencyPairs = Seq(CurrencyPairs.btcusd,CurrencyPairs.ethbtc)
         val client = ExchangePlatformClient.asGeminiClient(currencyPairs)
         val source = client.source
     
         source.collect {
           case Right(e:GeminiEvent) => e.events.filter {
             case c:ChangeEvent => c.price > 100 || c.reason == GeminiEventReasons.trade
           }
         }.runWith(Sink.foreach(println))
```

Use the expressivenes of Scala and the Flow Control provided by Akka Streams to utilize useful crypto trades!
 
### Copyright and License

All code is available to you under the MIT license, available at
http://opensource.org/licenses/mit-license.php and also in the
[LICENSE](LICENSE) file.
Copyright [Ivan Morozov], 2017.
