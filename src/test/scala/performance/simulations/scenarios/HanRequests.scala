package performance.simulations.scenarios

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.check._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._

/**
  * Created by Tarun Kale
  */

class HanRequests extends Simulation {

  val headers_common = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json",
    "Authorization" -> "Bearer eyJraWQiOiIyMTQiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiOWdfelhFOElPSUVvTHlpMTlxU3p0QSIsInN1YiI6Imh0dHBzOi8vdGVzdC5zYWxlc2ZvcmNlLmNvbS9pZC8wMEQweDAwMDAwMDBOMnNFQUUvMDA1MHgwMDAwMDB5dG9DQUFRIiwiYXVkIjoiM01WRzlZYjVJZ3Fua0I0cUlCVUc3bHQ4RnEuS0FaVS5oX2hSWDlTR2t6eUZhb0tzbTdKTFouUFZLbnByOFNib2VEWGNLeVFvRzBVandCdklZQ1hEZyIsImlzcyI6Imh0dHBzOi8vcWEtcGhpbGFudGhyb3B5Y2xvdWQuY3M5NS5mb3JjZS5jb20iLCJleHAiOjE1Mjk2NTUzNzksImlhdCI6MTUyOTY1NTI1OSwibm9uY2UiOiIxNTI5NjU1MjMzMjA3MDAiLCJjdXN0b21fYXR0cmlidXRlcyI6eyJVc2VybmFtZSI6InRrYWxlK3Rlc3QxQHNhbGVzZm9yY2UuY29tIn19.n2FlK7RvKO-uc_VeVEuKdklOObxvDaz3MjVlknlMzuAqqBP3zbErL9xjaEuHAGKohS4t-8-bFk1-uZ4lFzBF3aKrfRqF4SgHFb_woiuRm-o_DgOPwoz9bUaZp1G8-iYAM3nMhjiDwzu3nHWSU66ykg6AGko0kLlI3eNpoP8Q4A_PcBh-POm74TC-lbintTf3Nef52AUjVwbqb-LO6yQT1Vp5ErRCkAr1d6FqbMXY2IDsAECkgLXhBekq23c56cSYbO29Y4fz7kS29u6WQEgHAvD-4sRggr33jTKNmTnY08htupBCWtgLRC-Jbt_uwnnMDlxp3qwaYoZZfVUvo0uWpxaP0iFWowSTSqwbe6JaAuMggosVmTlXSdojqH75wikF60r53kc_e5cM1OnvTDH-sjQzc2QLal4QtEgohHSKwVGX969XgYNWsqK1CA0e0pT5sv5caVqF7LBmGCYvLSlt8P0cvFzZ_Qmgnh2e9DFv18EtmGTKuqe9QI6-_Xpz9tyGwGroSStwipiP_eOIzJ-cD_4xc6L8pscu3ms-IbHtapWxSgi8q2DYbpNQj4tGD7KHF3EpYIadux2kP4gkC6mNC5KjB5nQLctle29P4oo5qJ5Zm6hIo4xD-B7rMoyV3I1WqDV9FL5_-wdG2OoLr5lrV4ziaMK7a6sa0zuBNrFmdUU"
  )

  val dataFeeder = csv("src/test/resources/performance/data/organization2.csv").random
  val processorFeeder = csv("src/test/resources/performance/data/processor.csv").random
  val campaignFeeder = csv("src/test/resources/performance/data/campaign.csv").random



  def isNull[T] = new Validator[T] {
    val name = "isNull"

    def apply(actual: Option[T]): Validation[Option[T]] = {
      actual match {
        case Some(null) => Success(actual)
        case _ => Failure("Value is not Null")
      }
    }
  }


  /* Load Tests on Charge Simple Controller */
  val grpCSController = "ChargeSimpleController"
  val scnChargeSimple = scenario("ChargeSimple").group(grpCSController) {
    feed(timestampFeeder)
      .exec(
        http("ChargeSimple")
          .post("""/chargesimple""")
          .queryParam("description","Load Tests")
          .queryParam("amount",42)
          .queryParam("currency","USD")
          .queryParam("stripeEmail","testuser@salesforce.com.qa")
          .queryParam("stripeToken","testuser@salesforce.com.qa")
          .headers(headers_common)
          .check(status.is(200))
          )

  }

  /* Load Tests on Checkout Controller */
  val grpCheckoutController = "CheckoutController"
  val scnCreateCheckout = scenario("CreateCheckout").group(grpCheckoutController) {
    feed(timestampFeeder)
      .exec(
        http("CreateCheckout")
          .post("""/checkout""")
          .check(status.is(200))
          )
  }

  val scnGetCheckout = scenario("GetCheckout").group(grpCheckoutController) {
    feed(timestampFeeder)
      .exec(
        http("GetCheckout")
          .get("""/checkout""")
          .check(status.is(200))
      )
  }

  val scnUpdateCheckout = scenario("UpdateCheckout").group(grpCheckoutController) {
    feed(timestampFeeder)
      .exec(
        http("UpdateCheckout")
          .put("""/checkout""")
          .check(status.is(200))
      )
  }


  /* Load Tests on Connect User Controller */
  val grpConnectUserController = "ConnectUserController"
  val scnConnectUser = scenario("ConnectUser").group(grpConnectUserController) {
    feed(timestampFeeder)
      .exec(
        http("ConnectUser")
          .post("""/checkout""")
          .queryParam("code","42")
          .check(status.is(200))
      )
  }

  val scnGetOauthURL = scenario("GetOauthURL").group(grpConnectUserController) {
    feed(timestampFeeder)
      .feed(processorFeeder)
      .exec(
        http("GetOauthURL")
          .get("""/oauth_url/${processor_id}""")
          .headers(headers_common)
          .check(status.is(200))
      )
  }


  /* Load Tests on Credit card Controller */
  val grpCreditCardController = "CreditCardController"
  val scnAddCreditCard = scenario("AddCreditCard").group(grpCreditCardController) {
    feed(timestampFeeder)
      .exec(
        http("AddCreditCard")
          .post("""/creditcard""")
          .queryParam("cardToken","42")
          .queryParam("customerToken","42")
          .queryParam("email","testuser@salesforce.com.qa")
          .check(status.is(200))
      )
  }


  val scnGetCreditCard = scenario("GetCreditCard").group(grpCreditCardController) {
    feed(timestampFeeder)
      .exec(
        http("GetCreditCard")
          .get("""/creditcard/createTestCard/tkale+test1@salesforce.com""")
          .headers(headers_common)
          .check(status.is(200))
      )
  }


  /* Load Tests on Customer Controller */
  val grpCustomerController = "CustomerController"
  val scnCreateCustomer = scenario("CreateCustomer").group(grpCustomerController) {
    feed(timestampFeeder)
      .exec(
        http("CreateCustomer")
          .post("""/customer""")
          .queryParam("cardToken","42")
          .queryParam("description","Load Tests")
          .queryParam("email","testuser@salesforce.com.qa")
          .queryParam("firstName","Perf")
          .queryParam("lastName","User")
          .queryParam("personId","1024")
          .check(status.is(200))
      )
  }

  val scnGetCustomer = scenario("GetCustomer").group(grpCustomerController) {
    feed(timestampFeeder)
      .exec(
        http("GetCustomer")
          .get("""/customer/customerId""")
          .check(status.is(200))
      )
  }



  /* Load Tests on Donation Controller */
  val grpDonationController = "DonationController"

  val scnCreateDonation = scenario("CreateDonation").group(grpDonationController) {
    feed(timestampFeeder)
      .feed(campaignFeeder)
      .exec(
        http("CreateDonation")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            """{
                "query": "mutation{\n createDonation(\n   dInput: {\n     contentId: ${id}\n     workplaceId: 2169\n     currency: \"USD\"\n     paymentMethodDetailInput: {\n       type: ONE_TIME_CREDIT_CARD\n       oneTimeCreditCardToken: \"tok_bypassPending\"\n     }\n     designations: [\n       {\n         designeeId: 922138 #NPO page\n         amount: 2500\n      },\n      {\n         designeeId: 922142 #NPO page\n         amount: 2500\n      },\n      {\n         designeeId: 2636608 #NPO page\n         amount: 2500\n      },{\n         designeeId: 2636602 #NPO page\n         amount: 2500\n      }\n      \n     ]      \n   }\n ){\n   id\n   createdTime,\n   currency,\n   paymentMethod,\n   designations{\n     id,\n     name,\n     status,\n     paidAmount,\n     channelFee,\n     plegedAmount,\n     paidUSDAmount,\n     plegedUSDAmount,\n     channelTransactionId,\n     applicationFee      \n   }\n }\n}"
              }"""


          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String]))
      )
  }

  val scnCreateDonationStripe = scenario("CreateDonationStripe").group(grpDonationController) {
    feed(timestampFeeder)
      .exec(
        http("CreateDonationStripe")
          .post("""/donation/stripe""")
          .queryParam("subscription.id","42")
          .queryParam("subscription.recurrenceCode","42")
          .queryParam("subscription.current","100")
          .queryParam("subscription.ofTotal","200")
          .queryParam("workplace.id","2171")
          .queryParam("workplace.name","UVW Processor")
          .queryParam("workplace.annualPayPeriods","12")
          .queryParam("workplace.defaultCurrency","USD")
          .queryParam("person.id","1024")
          .queryParam("person.firstName","Perf")
          .queryParam("person.lastName","User")
          .queryParam("person.employeeId","1000")
          .queryParam("person.workEmail","testuser@salesforce.com.qa")
          .queryParam("person.managerId","1024")
          .queryParam("person.department","Sales")
          .queryParam("person.costCenter","2142")
          .queryParam("person.lineOfBusiness","Sales")
          .queryParam("person.region","US")
          .queryParam("wamp.id","1024")
          .queryParam("wamp.name","Per")
          .queryParam("contentTree[0].childrenIds","1024")
          .queryParam("contentTree[0].causes","1024")
          .queryParam("contentTree[0].uniqueId","1024")
          .queryParam("contentTree[0].id","1024")
          .queryParam("contentTree[0].name","1024")
          .queryParam("contentTree[0].parentId","1024")
          .queryParam("contentTree[0].contentType","1024")
          .queryParam("contentTree[0].sdg","1024")
          .queryParam("contentTree[0].processorId","1024")
          .queryParam("contentTree[0].dest","1024")
          .queryParam("contentTree[0].pledgedAmountLocal","2000")
          .queryParam("paymentMethod","CREDIT_CARD")
          .queryParam("currency","1024")
          .queryParam("externalId","1024")
          .queryParam("channelName","1024")
          .queryParam("source","1024")
          .queryParam("customerId","1024")
          .queryParam("createdDate","1024")
          .check(status.is(200))
      )
  }


  val scnCreateDonationOffline = scenario("CreateDonationOffline").group(grpDonationController) {
    feed(timestampFeeder)
      .exec(
        http("CreateDonationOffline")
          .post("""/donation/offline""")
          .queryParam("subscription.id","42")
          .queryParam("subscription.recurrenceCode","42")
          .queryParam("subscription.current","100")
          .queryParam("subscription.ofTotal","200")
          .queryParam("workplace.id","2171")
          .queryParam("workplace.name","UVW Processor")
          .queryParam("workplace.annualPayPeriods","12")
          .queryParam("workplace.defaultCurrency","USD")
          .queryParam("person.id","1024")
          .queryParam("person.firstName","Perf")
          .queryParam("person.lastName","User")
          .queryParam("person.employeeId","1000")
          .queryParam("person.workEmail","testuser@salesforce.com.qa")
          .queryParam("person.managerId","1024")
          .queryParam("person.department","Sales")
          .queryParam("person.costCenter","2142")
          .queryParam("person.lineOfBusiness","Sales")
          .queryParam("person.region","US")
          .queryParam("wamp.id","1024")
          .queryParam("wamp.name","Per")
          .queryParam("contentTree[0].childrenIds","1024")
          .queryParam("contentTree[0].causes","1024")
          .queryParam("contentTree[0].uniqueId","1024")
          .queryParam("contentTree[0].id","1024")
          .queryParam("contentTree[0].name","1024")
          .queryParam("contentTree[0].parentId","1024")
          .queryParam("contentTree[0].contentType","1024")
          .queryParam("contentTree[0].sdg","1024")
          .queryParam("contentTree[0].processorId","1024")
          .queryParam("contentTree[0].dest","1024")
          .queryParam("contentTree[0].pledgedAmountLocal","2000")
          .queryParam("paymentMethod","CREDIT_CARD")
          .queryParam("currency","1024")
          .queryParam("externalId","1024")
          .queryParam("channelName","1024")
          .queryParam("source","1024")
          .queryParam("customerId","1024")
          .queryParam("createdDate","1024")
          .check(status.is(200))
      )
  }

  val scnGetDonation = scenario("GetDonation").group(grpDonationController) {
    feed(timestampFeeder)
      .exec(
        http("GetDonation")
          .get("""/donation""")
          .queryParam("personId","1024")
          .queryParam("workplaceId","2171")
          .queryParam("rowkey","4")
          .check(status.is(200))
      )
  }


  /* Load Tests on Hbase Service Controller */
  val grpHbaseServiceController = "HbaseServiceController"
  val scnHbaseFindBy = scenario("HbaseFindBy").group(grpHbaseServiceController) {
    feed(timestampFeeder)
      .exec(
        http("HbaseFindBy")
          .get("""/hbase-service/findBy""")
          .headers(headers_common)
          .queryParam("workplaceId","2169")
          .queryParam("personId","1024")
          .queryParam("startTime","1529103066635")
          .queryParam("endTime","1529657832319")
          .check(status.is(200))
      )
  }

  /* Load Tests on Stripe Event Controller */
  val grpStripeEventController = "StripeEventController"
  val scnReceiveEvent = scenario("ReceiveEvent").group(grpStripeEventController) {
    feed(timestampFeeder)
      .exec(
        http("ReceiveEvent")
          .post("""/events""")
          .body(StringBody(
            """{
               "event" : "CREATE"
            }"""
          )).asJSON
          .check(status.is(201))
      )
  }

}