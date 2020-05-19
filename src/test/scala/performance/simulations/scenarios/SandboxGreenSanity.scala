package performance.simulations.scenarios

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.check._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._



class SandboxGreenSanity extends Simulation {

  val headers_common = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json"  )

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
