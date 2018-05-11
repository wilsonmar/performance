package performance.simulations.scenarios

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.check._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._

/**
  * Created by Tarun Kale
  */

class QARequests extends Simulation {

  val headers_common = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json",
    "Authorization" -> "Bearer eyJraWQiOiIyMTQiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiSGdrYV93OGpOUjI1R0ZPbXpyVWFDdyIsInN1YiI6Imh0dHBzOi8vdGVzdC5zYWxlc2ZvcmNlLmNvbS9pZC8wMEQweDAwMDAwMDBOMnNFQUUvMDA1MHgwMDAwMDB5dG9DQUFRIiwiYXVkIjoiM01WRzlZYjVJZ3Fua0I0cUlCVUc3bHQ4RnEuS0FaVS5oX2hSWDlTR2t6eUZhb0tzbTdKTFouUFZLbnByOFNib2VEWGNLeVFvRzBVandCdklZQ1hEZyIsImlzcyI6Imh0dHBzOi8vcWEtcGhpbGFudGhyb3B5Y2xvdWQuY3M5NS5mb3JjZS5jb20iLCJleHAiOjE1MjU4MDg1NjIsImlhdCI6MTUyNTgwODQ0Miwibm9uY2UiOiIxNTI1ODA4Mzc5MjgyMDAiLCJjdXN0b21fYXR0cmlidXRlcyI6eyJVc2VybmFtZSI6InRrYWxlK3Rlc3QxQHNhbGVzZm9yY2UuY29tIn19.tJSp8F2dB1zoj9Pz9DmOh7-TV4izCXAYMfaQK45UHoClwSo0wHdU1QvehTskMMSgv2Sb4Z5ke9S41kY9Gr8Awj9ZxmJSKVENmki-0HD2J55LlCMlUxRQcmPcdT5DdFt8XFzyuDM-npW62cZnBaOHcoiPVdVZR--gg5hszm1V-6uOD61LmuAYepNRu4yf7G4xWFOsoZjuWcSbILhNg5rfFJysfdHT-n0uyVy7flHUho9f3euGg6OkJngty-Rex0Wy0UHe3C9v1TITkAoQyQ0uBfhLL82GuzmJs8zN8eJG_Kxb99-kifBXnsJbnDR7A2xYg8PlizXHHudnwphlZ_-3ooS0PA6cGGfD5GawKqmSva6RrV7k3USXVpOYdn9DcUy9GVSpTbCMMl-MoqB89ys3H-kop352A6PDHNpu42Ri96OCgQk1XuVdwnUhG2tbVqt3hLwEzD-v4CJ88RMFWZSNxpdERf_ofqDxBseBN670w5x6ulTuSARFVNWIhCRagjuXU8FJyRAVuOQlgsCd1K03fhv932AFJZt4bJbqFm3lDGS3J_bWBsrb8NXwC00NtjnG5pbxQuUYbvs_y86YrcFlm5cUT2AYdEhss8AFq34pChzIlBJFampTjDfut6pym0yQOcWoc7mIhfm2luKDaI6ct1wp-EwHRY9Tz5GJFsqrnJQ"
  )

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
