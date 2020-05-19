package performance.simulations.scenarios

import java.lang._

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.check._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._
import performance.simulations.lib.JenkinsParam._


class ExternalAPI extends Simulation {

  val headers_common = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json"  )

  val personFeeder = csv("src/test/resources/performance/data/person.csv").random


  // External API - Get Person
  val grpPerson = "Person"
  val scnGetPerson = scenario("GetPerson").group(grpPerson) {
    feed(timestampFeeder)
      .feed(personFeeder)
      .exec(
        http("GetPerson")
          .get("/api/partner/v1/person")
            .queryParam("key","id")
            .queryParam("operator","eq")
            .queryParam("values","${id}")
          .headers(headers_common)
          .check(status.is(200))
          .check(jsonPath("$.data").transform(_.size >2).is(true))



          )
  }
}



