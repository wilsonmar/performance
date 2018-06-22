package performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import performance.simulations.lib.JenkinsParam._
import performance.simulations.scenarios._

import scala.concurrent.duration._

/**
  * Created by Tarun Kale.
  */

class Injector_Han extends Simulation {

  val httpTEST_HAN = http
    .baseURL(TEST_HAN_URL)
    .acceptHeader("text/html,application/xhtml+xml,application/xml:q=0.9,image/webp,*/*:q-0.8")
    .acceptLanguageHeader("en-US,en;q=0.8")
    //.connection("""keep-alive""")

  val httpTEST = http
    .baseURL(TEST_ZUUL_URL)
    .acceptHeader("text/html,application/xhtml+xml,application/xml:q=0.9,image/webp,*/*:q-0.8")
    .acceptLanguageHeader("en-US,en;q=0.8")

  //Instantiating PostNotification
  val hanRequest = new HanRequests

  before {
    println("Starting LOAD test, Targeting POSTS only at  "+ peakRPS  + " rps." + "Steady state = " + steadyTime )
  }


  setUp(

    // Load Injection
    /*hanRequest.scnChargeSimple.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnCreateCheckout.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnGetCheckout.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnUpdateCheckout.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnConnectUser.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnGetOauthURL.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnAddCreditCard.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnGetCreditCard.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnCreateCustomer.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnGetCustomer.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnCreateDonation.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnCreateDonationStripe.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnCreateDonationOffline.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnGetDonation.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnHbaseFindBy.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnReceiveEvent.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST)
*/
    hanRequest.scnGetOauthURL.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST_HAN),
    hanRequest.scnCreateDonation.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST),
    hanRequest.scnGetCreditCard.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST_HAN),
    hanRequest.scnHbaseFindBy.inject( rampUsersPerSec(1) to (peakRPS) during (rampTime seconds), constantUsersPerSec(peakRPS) during(steadyTime seconds)).protocols(httpTEST_HAN)

  ).assertions (

    details(hanRequest.grpCSController / "ChargeSimple" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpCSController / "ChargeSimple" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpCheckoutController / "CreateCheckout" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpCheckoutController / "CreateCheckout" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpCheckoutController / "GetCheckout" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpCheckoutController / "GetCheckout" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpCheckoutController / "UpdateCheckout" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpCheckoutController / "UpdateCheckout" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpConnectUserController / "ConnectUser" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpConnectUserController / "ConnectUser" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpConnectUserController / "GetOauthURL" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpConnectUserController / "GetOauthURL" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpCreditCardController / "AddCreditCard" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpCreditCardController / "AddCreditCard" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpCreditCardController / "GetCreditCard" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpCreditCardController / "GetCreditCard" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpCustomerController / "CreateCustomer" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpCustomerController / "CreateCustomer" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpCustomerController / "GetCustomer" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpCustomerController / "GetCustomer" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpDonationController / "CreateDonation" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpDonationController / "CreateDonation" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpDonationController / "CreateDonationStripe" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpDonationController / "CreateDonationStripe" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpDonationController / "CreateDonationOffline" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpDonationController / "CreateDonationOffline" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpDonationController / "GetDonation").responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpDonationController / "GetDonation").failedRequests.percent.lte( errorRate),
    details(hanRequest.grpHbaseServiceController / "HbaseFindBy" ).responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpHbaseServiceController / "HbaseFindBy" ).failedRequests.percent.lte( errorRate),
    details(hanRequest.grpStripeEventController / "ReceiveEvent").responseTime.mean.lte( meanResponseTime),
    details(hanRequest.grpStripeEventController / "ReceiveEvent" ).failedRequests.percent.lte( errorRate)
  )

  after {
    println("Completed Gatling test")
  }

}