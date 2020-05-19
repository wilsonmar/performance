package performance.simulations.scenarios

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.check._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._


class QARequests extends Simulation {

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

  val grpContent = "Content"

  // Query User Profile

  val scnGlobalSearch = scenario("GlobalSearch").group(grpContent) {
    feed(timestampFeeder)
      .exec(
        http("GlobalSearch")
          .post("""/graphql""")
          .headers(headers_common)
          .body(StringBody(
            """{
                "query": "query {\n  organization(id: 2174) {\n    id\n    type\n    roles\n    name\n    dba\n    externalId\n    street\n    street2\n    city\n    state\n    postalCode\n    postalCodeExt\n    country\n    phone\n    website\n    defaultLanguage\n    defaultCurrency\n    timeZone\n    brandSettings {\n      id\n      communityLogo\n      communityLogoCrid\n      primaryColor\n      secondaryColor\n      footerContent1\n      footerContent2\n      footerContent3\n    }\n    homepageSettings {\n      id\n      organizationMessage {\n        isHidden\n        message\n        metrics {\n          label\n          metric\n        }\n      }\n      givingBaseline\n      heroes {\n        __typename\n        ... on Story {\n          id\n          createdDate\n          modifiedDate\n          publishDate\n          publishState\n          expirationDate\n          author {\n            id\n            firstName\n            lastName\n            email\n            city\n            gender\n          }\n        }\n        __typename\n        ... on Campaign {\n          id\n          createdDate\n          modifiedDate\n          publishDate\n          publishState\n          expirationDate\n          author {\n            id\n            firstName\n            lastName\n            email\n            city\n            gender\n          }\n        }\n        __typename\n        ... on ImpactFund {\n          id\n          createdDate\n          modifiedDate\n          publishDate\n          publishState\n          expirationDate\n          author {\n            id\n            firstName\n            lastName\n            email\n            city\n            gender\n          }\n        }\n        __typename\n        ... on NpoPage {\n          id\n        }\n      }\n    }\n    selectableContent(q: \"st\") {\n      __typename\n      ... on Campaign {\n        id\n        name\n        excerpt\n        primaryImageCrid\n        primaryImageAlt\n        ctaText\n      }\n      ... on ImpactFund {\n        id\n        name\n        excerpt\n        primaryImageCrid\n        primaryImageAlt\n        ctaText\n      }\n      ... on NpoPage {\n        id\n        name\n        excerpt\n        primaryImageCrid\n        primaryImageAlt\n        ctaText\n      }\n      ... on Story {\n        id\n        name\n        excerpt\n        primaryImageCrid\n        primaryImageAlt\n        ctaText\n      }\n    }\n    globalKeywordSearch(personId: \"1000\", query: {keywords: \"ca\", types: {campaign: true, impactFund: true, npoPage: true, story: true}, offset: 25, maxResults: 25}) {\n      results {\n        id\n        type\n        name\n        city\n        state\n        country\n        governmentIdentifier\n        primaryCause\n        date\n        processor\n      }\n    }\n  }\n}"
              }""")).asJSON
          .check(status.is(200))
          .check(jsonPath("$.data.organization.globalKeywordSearch").transform(_.size >1).is(true))
          .check(jsonPath("$.errors").validate(isNull[String]))
      )
  }


}
