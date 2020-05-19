package performance.simulations.scenarios

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.check._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._



class DynoTestRequests extends Simulation {

  val headers_common = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json"  )


  def isNotNull[T] = new Validator[T] {
    val name = "isNotNull"

    def apply(actual: Option[T]): Validation[Option[T]] = {
      actual match {
        case Some(null) => Failure("Value is not Null")
        case _ => Success(actual)
      }
    }
  }

  /* Load Tests to understand Dyno throughput*/

  val grpDyno = "Dyno"

  val scnQueryHealth = scenario("QueryHealth").group(grpDyno) {
    feed(timestampFeeder)
      .exec(
        http("QueryHealth")
          .get("""/health""")
          .headers(headers_common)
          .check(status.is(200))
          .check(jsonPath("$.status").validate(isNotNull[String])
          ))
  }

  val scnUpdateLog = scenario("UpdateLog").group(grpDyno) {
    feed(timestampFeeder)
      .exec(
        http("UpdateLog")
          .post("""/loggers/com.sfdo.ngp""")
          .headers(headers_common)
          .body(StringBody(
            """{
                "configuredLevel": "INFO"
              }""")).asJSON
          .check(status.is(200))
          )
  }

}
