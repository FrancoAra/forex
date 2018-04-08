Forex
=====

Reverse proxy to rate exchange apis. Some apis will limit the
amount of requests and it might be the case that you want to have faster
local queries for a micro service ecosystem, Forex cache requests to exchange
apis so that the foreign api doesn't get overloaded.

Currently OneForge is the only api supported.

### Server API

endpoint: `GET` `http://localhost:8888/?from=JPY&to=EUR`

response:

```json
{
    "from": "JPY",
    "to":"EUR",
    "price":0.00761454,
    "timestamp":1523221819
}
```

### Setup

To run the code `sbt run`

To test the code `sbt test`

To run the integration test with One Forge `sbt it:test`

### Server configuration

When requested, besides responding with the rate, the server will cache it.
Every certain amount of seconds it will check for old rates which must be flushed.

In the configuration file you may change the following settings:

```
  one-forge {
    key = "<your-api-key>"
    key = ${?ONEFORGE_KEY}
  }
  rate-cache {
    tick = 60
    mortality = 300
  }
```

`one-forge.key` can be picked up from the environment
variable ONEFORGE_KEY, this is the api key needed to request to One Forge.

`rate-cache.tick` is the amount of seconds the server will take to check
again for old rates to flush. (default: 1min)

`rate-cache.mortality` is the amount of seconds the server will let a rate
live before flushing it from the cache. (default: 5min)

### Design Decisions

* Simple polymorphic `F[_]`s and typeclasses of the cats effect hierarchy
have been preferred against the `Eff[S, _]` monad for now, for simplicity
reasons. As soon as combining effects is required it could be reimplemented.
* The cache storage and provider were decoupled on purpose to easily experiment
with different methods of storing or with several api rate providers.
* A simple fs2 stream is scheduling the flushing of the cache, this could
easily be improved into a streaming api for the clients.

### Improvements todo

* A lot of error handling has to be done, including the strategy for when the
requests limit to the provider has been reached.
* Rates aging can be calculated from the timestamps instead of the current counter.
* To decrease even more the amount of requests done to the rate providers, packaging
several incoming requests in 1 api requests could be done (One Forge accepts
several currency pairs per request).
* Several providers could be combined, making a semigroup that takes the
smallest rate.
* The fs2 stream could easily be evolved into a streaming api for the clients.
* Akka http could be replaced by http4s, which has arguably several advantages.
* Some dead code needs to be cleaned.
* Monitoring.
