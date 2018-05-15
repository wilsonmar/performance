package performance.simulations.scenarios

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.check._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._

/**
  * Created by Tarun Kale
  */

class DynoTestRequests extends Simulation {

  val headers_common = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json",
    "Authorization" -> "Bearer eyJraWQiOiIyMTQiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiVXVRVkVxOWVyTU9PNWRHZXRaVEdLdyIsInN1YiI6Imh0dHBzOi8vdGVzdC5zYWxlc2ZvcmNlLmNvbS9pZC8wMEQweDAwMDAwMDBOMnNFQUUvMDA1MHgwMDAwMDBtRmR5QUFFIiwiYXVkIjoiM01WRzlZYjVJZ3Fua0I0cUlCVUc3bHQ4RnEuS0FaVS5oX2hSWDlTR2t6eUZhb0tzbTdKTFouUFZLbnByOFNib2VEWGNLeVFvRzBVandCdklZQ1hEZyIsImlzcyI6Imh0dHBzOi8vcWEtcGhpbGFudGhyb3B5Y2xvdWQuY3M5NS5mb3JjZS5jb20iLCJleHAiOjE1MjYzMTYzNTYsImlhdCI6MTUyNjMxNjIzNiwibm9uY2UiOiIxNTI2MzE2MTU2MTEyMDAiLCJjdXN0b21fYXR0cmlidXRlcyI6eyJVc2VybmFtZSI6InRlc3R1c2VyQHNhbGVzZm9yY2UuY29tLnFhIn19.luJXakN2DA5G80ALQgHYAyNOM39plYlXkxHi7A6m3_eRtFdMOnxyQ094tyJxLI75DxOozCtjaa0HQ8uRUChxfp8sIPtHhXM7P8v4cpvxSMtxAz0Q-1dNi0GoXccdZhAlHj6vg6-yR1vjZxQoDZZC0gsucqdvWXygJNWbcSokrvAWl3rVVRe2SZqlMP-Q7qQQE_Y-uLU8r0j6TwCM3rXntqEtpFTw-1sOQWoaRp59v8612067lxX4ZL0wm6bFGIYenjNQtc2RLvjfc88fz8ssMrYkA8qBV06CYtiETy0EbWXg1ekLqq-HsqZpjmBQicvtZ1Z4d1-ODmY9Wr0QJ3WUgcVw-XrGrVCSU_iurY70uIRhGmuFZ_p7tHSzuWC-EiX7CdbbHsSeM2j6mqoLqXylizZYIH7se_xzWiBblg6qHr_lKklO8jLFCxfHN1IW0YwDQe6ypCyJjzD8iUdVgC-enTvMrzdvFOadrg5jxORJcAuad3kstShuIqPL_Y5gfT7QHM8j_8rScAi7AN6pBwUkMA26AIXgihLuljTHo4hXTs258cZ7eKo3TBdG1WclHpbbGy0D5IPkBp7KyhTJOhJU8bs8QKO-Q17tilNNOPTEsc34pWnP8d_DXWLFlFfwd78KbhyvJFbfk_BC8zYE8eJY9-SMHTrzd6Ig2yAnsZYotf8"
  )


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

}
