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
    "Authorization" -> "Bearer eyJraWQiOiIyMTQiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiVXVRVkVxOWVyTU9PNWRHZXRaVEdLdyIsInN1YiI6Imh0dHBzOi8vdGVzdC5zYWxlc2ZvcmNlLmNvbS9pZC8wMEQweDAwMDAwMDBOMnNFQUUvMDA1MHgwMDAwMDBtRmR5QUFFIiwiYXVkIjoiM01WRzlZYjVJZ3Fua0I0cUlCVUc3bHQ4RnEuS0FaVS5oX2hSWDlTR2t6eUZhb0tzbTdKTFouUFZLbnByOFNib2VEWGNLeVFvRzBVandCdklZQ1hEZyIsImlzcyI6Imh0dHBzOi8vcWEtcGhpbGFudGhyb3B5Y2xvdWQuY3M5NS5mb3JjZS5jb20iLCJleHAiOjE1MjYzMTYzNTYsImlhdCI6MTUyNjMxNjIzNiwibm9uY2UiOiIxNTI2MzE2MTU2MTEyMDAiLCJjdXN0b21fYXR0cmlidXRlcyI6eyJVc2VybmFtZSI6InRlc3R1c2VyQHNhbGVzZm9yY2UuY29tLnFhIn19.luJXakN2DA5G80ALQgHYAyNOM39plYlXkxHi7A6m3_eRtFdMOnxyQ094tyJxLI75DxOozCtjaa0HQ8uRUChxfp8sIPtHhXM7P8v4cpvxSMtxAz0Q-1dNi0GoXccdZhAlHj6vg6-yR1vjZxQoDZZC0gsucqdvWXygJNWbcSokrvAWl3rVVRe2SZqlMP-Q7qQQE_Y-uLU8r0j6TwCM3rXntqEtpFTw-1sOQWoaRp59v8612067lxX4ZL0wm6bFGIYenjNQtc2RLvjfc88fz8ssMrYkA8qBV06CYtiETy0EbWXg1ekLqq-HsqZpjmBQicvtZ1Z4d1-ODmY9Wr0QJ3WUgcVw-XrGrVCSU_iurY70uIRhGmuFZ_p7tHSzuWC-EiX7CdbbHsSeM2j6mqoLqXylizZYIH7se_xzWiBblg6qHr_lKklO8jLFCxfHN1IW0YwDQe6ypCyJjzD8iUdVgC-enTvMrzdvFOadrg5jxORJcAuad3kstShuIqPL_Y5gfT7QHM8j_8rScAi7AN6pBwUkMA26AIXgihLuljTHo4hXTs258cZ7eKo3TBdG1WclHpbbGy0D5IPkBp7KyhTJOhJU8bs8QKO-Q17tilNNOPTEsc34pWnP8d_DXWLFlFfwd78KbhyvJFbfk_BC8zYE8eJY9-SMHTrzd6Ig2yAnsZYotf8"
  )

  val dataFeeder = csv("src/test/resources/performance/data/organization2.csv").random

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
          ))

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
      .exec(
        http("GetOauthURL")
          .get("""/oauth_url/2171""")
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
          .get("""/creditcard/createTestCard/testuser@salesforce.com.qa""")
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
      .exec(
        http("CreateDonation")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"mutation{\\n createDonation(\\n   dInput: {\\n     contentId: 1344150\\n     workplaceId: 2261\\n     currency: \\\"USD\\\"\\n     paymentMethodDetailInput: {\\n       type: ONE_TIME_CREDIT_CARD\\n       oneTimeCreditCardToken: \\\"tok_bypassPending\\\"\\n     }\\n     designations: [\\n       {\\n         designeeId: 1344149 \\n         amount: 2500\\n       }\\n       {\\n         designeeId: 1344148 \\n         amount: 2500\\n       }\\n     ]      \\n   }\\n ){\\n   createdTime,\\n   currency,\\n   paymentMethod,\\n   designations{\\n     id,\\n     name,\\n     status,\\n     paidAmount,\\n     channelFee,\\n     plegedAmount,\\n     paidUSDAmount,\\n     plegedUSDAmount,\\n     channelTransactionId,\\n     applicationFee      \\n   }\\n }\\n}\"\n}"
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
          .queryParam("workplaceId","2171")
          .queryParam("personId","1024")
          .queryParam("startTime","20180405")
          .queryParam("endTime","20180605")
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