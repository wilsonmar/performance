package performance.simulations.scenarios

import java.lang._

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.check._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._
import performance.simulations.lib.JenkinsParam._

/**
  * Created by Tarun Kale
  */

class ExternalAPI extends Simulation {

  val headers_common = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json",
    "Authorization" -> "Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlckBzYWxlc2ZvcmNlLmNvbS5xYSIsImF1ZCI6Imh0dHBzOlwvXC90ZXN0LnNhbGVzZm9yY2UuY29tXC9pZFwvMDBEMHQwMDAwMDA4Zng5RUFBXC8wMDUxSTAwMDAwMVB3cXFRQUMiLCJpc3MiOiJodHRwczpcL1wvcWEtcGhpbGFudGhyb3B5Y2xvdWQuY3M5NS5mb3JjZS5jb20iLCJleHAiOjE1MjY5NjE5MzEsImN1c3RvbV9hdHRyaWJ1dGVzIjp7IlVzZXJuYW1lIjoidGVzdHVzZXJAc2FsZXNmb3JjZS5jb20ucWEifX0.L1bKOrZs--Uz3u7OLKc_MZGymnsqglGeABG6ETT5Q6EKqgUOnRjincOJ5HUWQ8wHnkLQjpsDFI0IwYNtSdqPTXAQoLN66iYw3q5iqAuHMvHrO71IeJWH5MRQGieCrT-aYguRtvyA_EyeGrm9GnW_Es8G1lIIhyJbhhHCVb5ltec"
  )

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



