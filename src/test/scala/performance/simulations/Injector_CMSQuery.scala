package performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import performance.simulations.lib.JenkinsParam._
import performance.simulations.scenarios._

import scala.concurrent.duration._

/**
  * Created by Tarun Kale.
  */

class Injector_CMSQuery extends Simulation {

  val httpTEST = http
    .baseURL(TEST_URL)
    //.proxy(Proxy("proxy-src.research.ge.com",8080))
    .acceptHeader("text/html,application/xhtml+xml,application/xml:q=0.9,image/webp,*/*:q-0.8")
    .acceptLanguageHeader("en-US,en;q=0.8")
  //.connection("""keep-alive""")


  //Instantiating PostNotification
  val postRequest = new PostRequests

  before {
    println("Starting LOAD test, Targeting POSTS only at  "+ peakRPS_QueryCMS  + " rps." + "Steady state = " + steadyTime )
  }


  setUp(

    // Load Injection

    postRequest.scnContentListing.inject(rampUsersPerSec(1) to (peakRPS_QueryCMS) during (rampTime seconds), constantUsersPerSec(peakRPS_QueryCMS) during(steadyTime seconds)).protocols(httpTEST)
  ).assertions (

    details(postRequest.grpContentListing / "QueryContentListing" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpContentListing / "QueryContentListing" ).failedRequests.percent.lte( errorRate)
  )

  after {
    println("Completed Gatling test")
  }

}