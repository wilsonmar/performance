package performance.simulations.scenarios

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.check._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._

/**
  * Created by Tarun Kale
  */

class SandboxGreenSanity extends Simulation {

  val headers_common = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json",
    "Authorization" -> "Bearer eyJraWQiOiIyMTIiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiaWxoZzhobi1NdlQwNXhURXhONUp4ZyIsInN1YiI6Imh0dHBzOi8vdGVzdC5zYWxlc2ZvcmNlLmNvbS9pZC8wMEQwdDAwMDAwMDhmeDlFQUEvMDA1MHQwMDAwMDBYN2JHQUFTIiwiYXVkIjoiM01WRzlpZm1BS0NISVNiYVR6Q3JuQWx3SVhmTERNNGV2UUxmcTZDVkpIVnd2ZzdrOHR3VGVReHJzYWszMGhERHo0VHppNzFncC56bUZLbXVfRWxnciIsImlzcyI6Imh0dHBzOi8vcWEyLXBoaWxhbnRocm9weWNsb3VkLmNzNzcuZm9yY2UuY29tLyIsImV4cCI6MTUyMzQ4NDM1NCwiaWF0IjoxNTIzNDg0MjM0LCJub25jZSI6IjE1MjM0ODQxMTQ3MTUwMCIsImN1c3RvbV9hdHRyaWJ1dGVzIjp7IlVzZXJuYW1lIjoidGVzdHVzZXJAc2FsZXNmb3JjZS5jb20ucWEyIn19.d-G-VDvCgdA3mv8aYYnAQFWWVWVNIjjCMQgbFvloA1N9w3_KX4ISPXdi5a5A0FOqwMR9j5hbxA5Wf71OLkDn_DJ3LxzVAJulMYmfGZMGyhOO8L1BoRxOnAzZAOJ4iG7c0gtMfGpxC86x2t-0q38ibkvox7yYPyb_5azpSLTA9b01FX7Sjap9txSJB3ZZevuwJfvu2_-xshGSDo_3pHfwtgY9bGfoteATR_Er__mrt7GGmAoHaz4IXi6wCQa4ygOY5axxOPkJ5H18Y11My2wZ37Jv7PMYWGoJYr3lFmWg5hgns5SL2CTSCJTqq5nUwEFfoDhJ4wkgP4vX1rCkYist8M0oS8DJGnncna_pGzFD3w31RIhPysY2RBKyx8Qli_4loKKNiZQ4C2foKvWlIDG6O6tQ2WpdFf5CK8JUDTHeFCh4BHFoXKCUtF2jsmWcNxVDmLY9VoFJYBV8LUqZvIXzLnrTZTmy6Gov59AUweD2OykTsWtM_5PtJTctK5dDdAkF6f6oUlnSvdbPjNtG8HXZjT1fYWgq-wZnWzQMWbG6XBSK0vhOruHn07fPQY3ulZffS2njGnxhmVY9A38fAynRSaS3U7lw-EsWrbUVN1xCt3UykH_lxZD4610Z5LdhB78UpBL87_zjXo4DVG3vw3qXlnNR1Fi7oe38AgCOumZhKeQ"
  )

  def isNull[T] = new Validator[T] {
    val name = "isNull"
    def apply(actual : Option[T]) : Validation[Option[T]] = {
      actual match {
        case Some(null) => Success(actual)
        case _          => Failure("Value is not Null")
      }
    }
  }

  /* Load Tests on User Profile usecases*/

  val grpProfile = "UserProfile"

  // Query User Profile

  val scnQueryUserProfile = scenario("UserProfile").group(grpProfile) {
    feed(timestampFeeder)
      .exec(
        http("QueryUserProfile")
          .post("""/graphql""")
          .headers(headers_common)
          .body(StringBody(
            """{
              "query": "{\n  me {\n    person {\n      id\n      firstName\n      lastName\n      jobTitle\n      email\n     organizations {\n        id\n        name\n        __typename\n      }\n  }\n    __typename\n  }\n}\n"
            }""")).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String])
      ))
  }


}
