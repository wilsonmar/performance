package performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import performance.simulations.lib.JenkinsParam._
import performance.simulations.scenarios._

import scala.concurrent.duration._

/**
  * Created by Tarun Kale.
  */

class Injector_Dyno extends Simulation {

  val httpTEST = http
    .baseURL(TEST_URL)
    //.proxy(Proxy("proxy-src.research.ge.com",8080))
    .acceptHeader("text/html,application/xhtml+xml,application/xml:q=0.9,image/webp,*/*:q-0.8")
    .acceptLanguageHeader("en-US,en;q=0.8")
    //.connection("""keep-alive""")


  //Instantiating PostNotification
  val dynoRequests = new DynoTestRequests

  before {
    println("Starting LOAD test, Targeting POSTS only at  "+ peakRPS  + " rps." + "Steady state = " + steadyTime )
  }


  setUp(

    // Load Injection
    //dynoRequests.scnQueryHealth.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    dynoRequests.scnUpdateLog.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST)


  ).assertions (
    //details(dynoRequests.grpDyno / "QueryHealth" ).responseTime.mean.lte( meanResponseTime),
    //details(dynoRequests.grpDyno / "QueryHealth" ).failedRequests.percent.lte( errorRate),
    details(dynoRequests.grpDyno / "UpdateLog" ).responseTime.mean.lte( meanResponseTime),
    details(dynoRequests.grpDyno / "UpdateLog" ).failedRequests.percent.lte( errorRate)
  )

  after {
    println("Completed Gatling test")
  }

}