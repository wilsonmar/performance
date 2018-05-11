package performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import performance.simulations.lib.JenkinsParam._
import performance.simulations.scenarios._

import scala.concurrent.duration._

/**
  * Created by Tarun Kale.
  */

class Injector_QA extends Simulation {

  val httpTEST = http
    .baseURL(TEST_URL)
    //.proxy(Proxy("proxy-src.research.ge.com",8080))
    .acceptHeader("text/html,application/xhtml+xml,application/xml:q=0.9,image/webp,*/*:q-0.8")
    .acceptLanguageHeader("en-US,en;q=0.8")
    //.connection("""keep-alive""")


  //Instantiating PostNotification
  val qaRequest = new QARequests

  before {
    println("Starting LOAD test, Targeting POSTS only at  "+ peakRPS  + " rps." + "Steady state = " + steadyTime )
  }


  setUp(

    // Load Injection
    qaRequest.scnGlobalSearch.inject( rampUsersPerSec(1) to (1) during (1 seconds), constantUsersPerSec(1) during(1 seconds)).protocols(httpTEST)

  ).assertions (
    details(qaRequest.grpContent / "GlobalSearch" ).responseTime.mean.lte( meanResponseTime),
    details(qaRequest.grpContent / "GlobalSearch" ).failedRequests.percent.lte( errorRate)
  )

  after {
    println("Completed Gatling test")
  }

}