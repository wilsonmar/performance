package performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import performance.simulations.scenarios._
import scala.concurrent.duration._
import performance.simulations.lib.JenkinsParam._

/**
  * Created by Tarun Kale.
  */

class Injector extends Simulation {

  val httpTEST = http
    .baseURL(TEST_URL)
    //.proxy(Proxy("proxy-src.research.ge.com",8080))
    .acceptHeader("text/html,application/xhtml+xml,application/xml:q=0.9,image/webp,*/*:q-0.8")
    .acceptLanguageHeader("en-US,en;q=0.8")
    //.connection("""keep-alive""")


  //Instantiating PostNotification
  val postRequest = new PostRequests

  before {
    println("Starting LOAD test, Targeting POSTS only at  "+ peakRPS  + " rps." + "Steady state = " + steadyTime )
  }


  setUp(

    // Load Injection
    postRequest.scnQueryUserProfile.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    //postRequest.scnIsPersonActive.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnListCauses.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnListSDG.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnCreateStory.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnGetStory.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnCreateCampaign.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnGetCampaign.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnCreateImpactFund.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnGetImpactFund.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnGetOrganization.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnUpdatePrefCauses.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnHomePageSettings.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    postRequest.scnBrandSettings.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST)
    //postRequest.scnCreateNpoPage.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST)

  ).assertions (

    details(postRequest.grpProfile / "QueryUserProfile" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpProfile / "QueryUserProfile" ).failedRequests.percent.lte( errorRate),
    //details(postRequest.grpPerson / "IsPersonActive" ).responseTime.mean.lte( meanResponseTime),
    //details(postRequest.grpPerson / "IsPersonActive" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpCauses / "ListCauses" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpCauses / "ListCauses" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpSDG / "ListSDG" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpSDG / "ListSDG" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpStory / "CreateStory" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpStory / "CreateStory" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpStory / "GetStory" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpStory / "GetStory" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpCampaign / "CreateCampaign" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpCampaign / "CreateCampaign" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpCampaign / "GetCampaign" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpCampaign / "GetCampaign" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpImpactFund / "CreateImpactFund" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpImpactFund / "CreateImpactFund" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpImpactFund / "GetImpactFund" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpImpactFund / "GetImpactFund" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpOrganization / "GetOrganization" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpOrganization / "GetOrganization" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpCauses / "UpdatePrefCauses" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpCauses / "UpdatePrefCauses" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpSettings / "HomePageSettings" ).responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpSettings / "HomePageSettings" ).failedRequests.percent.lte( errorRate),
    details(postRequest.grpSettings / "BrandSettings").responseTime.mean.lte( meanResponseTime),
    details(postRequest.grpSettings / "BrandSettings").failedRequests.percent.lte( errorRate)
    //details(postRequest.grpNpoPage / "CreateNpoPage" ).responseTime.mean.lte( meanResponseTime),
    //details(postRequest.grpNpoPage / "CreateNpoPage" ).failedRequests.percent.lte( errorRate)

  )

  after {
    println("Completed Gatling test")
  }

}