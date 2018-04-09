package performance.simulations.scenarios

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.check._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._
import performance.simulations.lib.JenkinsParam._

/**
  * Created by Tarun Kale
  */

class PostRequests extends Simulation {

  val headers_common = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json",
    "Authorization" -> "Bearer eyJraWQiOiIyMTIiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiVnhNbUlvelNqVU5FcjFoQmwyMVVSUSIsInN1YiI6Imh0dHBzOi8vdGVzdC5zYWxlc2ZvcmNlLmNvbS9pZC8wMEQweDAwMDAwMDBOMnNFQUUvMDA1MHgwMDAwMDBtRmR5QUFFIiwiYXVkIjoiM01WRzlZYjVJZ3Fua0I0cUlCVUc3bHQ4RnEuS0FaVS5oX2hSWDlTR2t6eUZhb0tzbTdKTFouUFZLbnByOFNib2VEWGNLeVFvRzBVandCdklZQ1hEZyIsImlzcyI6Imh0dHBzOi8vcWEtcGhpbGFudGhyb3B5Y2xvdWQuY3M5NS5mb3JjZS5jb20vIiwiZXhwIjoxNTIzMzAzNzY5LCJpYXQiOjE1MjMzMDM2NDksIm5vbmNlIjoiMTUyMzMwMzYwNzA5OTAwIiwiY3VzdG9tX2F0dHJpYnV0ZXMiOnsiVXNlcm5hbWUiOiJ0ZXN0dXNlckBzYWxlc2ZvcmNlLmNvbS5xYSJ9fQ.KRzG6X9HTyVuP6RWshPZCb7PMe5VebvM0BLGTkp1VCpvO-Qhy2zWHRO6fcD4iMPmZCD_60CnZgkcLZEZ-LLbXu-bJmWnk7Zwt8_faecyRBTy1amu_bzePmEdsC5JhqdLb-_rk91iQeHGZJ4EyBVIj5ZFSOq7r170xzvjQLhJ6Y0EdPXsQo79-nwH74aOi3vUC8yFeq_V9pUECj2ar0wR8DvXN5mO5l16Imap_DLrYdq4XUQTl7lMJ1JH09FqCGvBQTgFQVv1fzxYOWzMEN-cGbGwjA2B4YMFciaVAqaeCToyJB5JNjbMMLhvuYFOWIDyWwi_KKa49nVneHGMvKbMJsIpvj7Ejsd8A829_5MpZcSOTJfltX0TW8UM9ZtSbeREGItHa5Nu0mpXPtHdfAj5c13Md4RKa03Qc5diFwhyaRoCC7lzbisy2IVUY2J_qMPIW87IJQVqeVTM-FAfYJuI98HLkKfCBBsBjYEGqpq036VF8zh4sHjBXCdSxYMJE9fhblMG6ddLHA18SwN82jnOk7TjMXaRtMskDBnnFphbU0lpSTBJeE_SlwW1-1W1QZbEEQzFkybAYcU489__Et-qZfTi0dX9JZ_9BLJ8gMIrW53oSdmvM6HQWGp_h-NAt_OuRaslbDAmmrw6bmRC2z3o4GjeFitjOxhK3JlFxrJTLrY"
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

  val grpProfile = "UserProfile"

  // Query User Profile

  val scnQueryUserProfile = scenario("UserProfile").group(grpProfile) {
    feed(timestampFeeder)
      .exec(
        http("QueryUserProfile")
          .post("""/graphql""")
          .headers(headers_common)
          .body(StringBody(
            """{
              "query": "{\n  me {\n    person {\n      id\n      firstName\n      lastName\n      jobTitle\n      email\n     organizations {\n        id\n        name\n        __typename\n      }\n  }\n    __typename\n  }\n}\n"
            }""")).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String])
      ))
  }

  /* Load Tests on Person usecases*/

  val grpPerson = "Person"

  // Is Person Active

  val scnIsPersonActive = scenario("IsPersonActive").group(grpPerson) {
    feed(timestampFeeder)
      .exec(
        http("IsPersonActive")
          .post("""/graphql""")
          .headers(headers_common)
          .body(StringBody(
            """{
                "query": "query{\n  isPersonActive(\n    email: \"testuser@salesforce.com.qa2\"\n    \n  ){\n    email\n    pcActive\n    sfidActive\n  }\n    \n  \n}"
              }""")).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String])
      ))
  }


  /* Load Tests on Causes usecases */

  val grpCauses = "Causes"

  // List Causes
  val scnListCauses = scenario("ListCauses").group(grpCauses) {
    feed(timestampFeeder)
      .exec(
        http("ListCauses")
          .post("""/graphql""")
          .headers(headers_common)
          .body(StringBody(
            """{
                "query": "{\n  causes {\n    id\n    name\n    primaryImageCrid\n    primaryImageAlt\n    excerpt\n __typename\n  }\n}\n"
              }""")).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String])
      ))
  }

  // Update Pref Causes
  val scnUpdatePrefCauses = scenario("UpdatePrefCauses").group(grpCauses) {
    feed(timestampFeeder)
      .exec(
        http("UpdatePrefCauses")
          .post("""/graphql""")
          .headers(headers_common)
          .body(StringBody(
            """{
                "query": "mutation {\n        test: updatePrefCauses(\n          causes: {\n            personId: 1000,\n            causeIds: [11001, 11002, 11003]\n          }\n        ) {\n          person {\n            causes {\n              id\n            }\n          }\n        }\n      }"
              }""")).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String])
      ))
  }


  /* Load Tests on Sustainable Development Goals usecases */

  val grpSDG = "SDG"

  // List SDG
  val scnListSDG = scenario("ListSDG").group(grpSDG) {
    feed(timestampFeeder)
      .exec(
        http("ListSDG")
          .post("""/graphql""")
          .headers(headers_common)
          .body(StringBody(
            """{
                "query": "{\n  sustainableDevelopmentGoals {\n    id\n    name\n    primaryImageCrid\n    primaryImageAlt\n    excerpt\n __typename\n  }\n}\n"
              }""")).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String])
      ))
  }


  /* Load Tests on Story usecases */

  val grpStory = "Story"

  // Create Story
  val scnCreateStory = scenario("CreateStory").group(grpStory) {
    feed(timestampFeeder)
      .exec(
        http("CreateStory")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"mutation {\\n        test: story(orgId: 1000, input:{\\n          publishDate: \\\"2018-03-19T19:15:34Z\\\"\\n          publishState:\\\"DRAFT\\\"\\n          expirationDate: \\\"2019-03-19T19:15:34Z\\\"\\n          tags: {\\n            causeIds: [2004, 2002, 2003, 2001]\\n            sdgIds: [1007, 1005, 1006, 1004]\\n          }\\n          relatedContent: []\\n          localized: {\\n            locale: \\\"en_US\\\"\\n            name: \\\"Story 1\\\"\\n            subheading: \\\"Story 1 SH\\\"\\n            excerpt: \\\"Story 1 Excerpt\\\"\\n            featuredVideo: \\\"Blah Blah\\\"\\n            description: \\\"Blah Blah Blah\\\"\\n            primaryImageCrid: \\\"TestId\\\"\\n            primaryImageAlt: \\\"TestId\\\"\\n            ctaText: \\\"Read\\\"\\n          }\\n        }) {\\n          id\\n          publishDate\\n          publishState\\n          expirationDate\\n          name\\n          subheading\\n          description\\n          primaryImageCrid\\n          primaryImageAlt\\n          ctaText\\n          causes {\\n            id\\n            name\\n          }\\n          sdgs {\\n            id\\n            name\\n          }\\n          relatedContent {\\n            ...ContentCard\\n            __typename\\n          }\\n          localized {\\n            locale\\n            name\\n            subheading\\n            excerpt\\n            featuredVideo\\n            description\\n            primaryImageCrid\\n            primaryImageAlt\\n            ctaText\\n          }\\n        }\\n      }\\n\\n      fragment ContentCard on Content {\\n        ... on Story {\\n          ...StoryCard\\n          __typename\\n        }\\n        ... on ImpactFund {\\n          ...ImpactFundCard\\n          __typename\\n        }\\n        ... on Campaign {\\n          ...CampaignCard\\n          __typename\\n        }\\n        ... on NpoPage {\\n          ...NonprofitCard\\n          __typename\\n        }\\n        __typename\\n      }\\n\\n      fragment StoryCard on Story {\\n        id\\n        name\\n        __typename\\n      }\\n\\n      fragment ImpactFundCard on ImpactFund {\\n        id\\n        name\\n        __typename\\n      }\\n\\n      fragment CampaignCard on Campaign {\\n        id\\n        name\\n        __typename\\n      }\\n\\n      fragment NonprofitCard on NpoPage {\\n        id\\n        name\\n        __typename\\n      }\"\n}"
          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String])
      ))
  }


  // Get Story
  val scnGetStory = scenario("GetStory").group(grpStory) {
    feed(timestampFeeder)
      .exec(
        http("GetStory")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n\t\"query\": \"query {\\n      story(id: 98) {\\n        id\\n        publishDate\\n        publishState\\n        expirationDate\\n        name\\n        subheading\\n        excerpt\\n        featuredVideo\\n        description\\n        primaryImageCrid\\n        primaryImageAlt\\n        ctaText\\n        causes {\\n          id\\n        }\\n        sdgs {\\n          id\\n        }\\n        localized {\\n          locale\\n          name\\n          subheading\\n          excerpt\\n          featuredVideo\\n          description\\n          primaryImageCrid\\n          primaryImageAlt\\n          ctaText\\n        }\\n        relatedContent {\\n          ...ContentCard\\n          __typename\\n        }\\n      }\\n    }\\n\\n    fragment ContentCard on Content {\\n      ... on Story {\\n        ...StoryCard\\n        __typename\\n      }\\n      ... on ImpactFund {\\n        ...ImpactFundCard\\n        __typename\\n      }\\n      ... on Campaign {\\n        ...CampaignCard\\n        __typename\\n      }\\n      ... on NpoPage {\\n        ...NonprofitCard\\n        __typename\\n      }\\n      __typename\\n    }\\n\\n    fragment StoryCard on Story {\\n      id\\n      name\\n      __typename\\n    }\\n\\n    fragment ImpactFundCard on ImpactFund {\\n      id\\n      name\\n      __typename\\n    }\\n\\n    fragment CampaignCard on Campaign {\\n      id\\n      name\\n      __typename\\n    }\\n\\n    fragment NonprofitCard on NpoPage {\\n      id\\n      name\\n      __typename\\n    }\"\n}"

          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String])
      ))
  }


  /* Load Tests on Compaign usecases */

  val grpCompaign = "Compaign"

  // Create Compaign
  val scnCreateCompaign = scenario("CreateCompaign").group(grpCompaign) {
    feed(timestampFeeder)
      .exec(
        http("CreateCompaign")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"mutation {\\n        test: campaign(orgId: 1000, input:{\\n          publishDate: \\\"2018-03-19T19:15:34Z\\\"\\n          publishState:\\\"DRAFT\\\"\\n          expirationDate: \\\"2019-03-19T19:15:34Z\\\"\\n          tags: {\\n            causeIds: [2004, 2002, 2003, 2001]\\n            sdgIds: [1007, 1005, 1006, 1004]\\n          }\\n         oneTimeAskLadder:100\\n         recurringTimeAskLadder:101\\n         fundraisingGoal:10000\\n         currency:\\\"USD\\\"\\n\\n          localized: {\\n            locale: \\\"en_US\\\"\\n            name: \\\"<campaignName>\\\"\\n            subheading: \\\"Campaign SH\\\"\\n            excerpt: \\\"Campaign Excerpt\\\"\\n            featuredVideo: \\\"Blah Blah\\\"\\n            description: \\\"Blah Blah Blah\\\"\\n            primaryImageCrid: \\\"TestId\\\"\\n            primaryImageAlt: \\\"TestId\\\"\\n            ctaText: \\\"Read\\\"\\n          }\\n          allowLocalizedFunds:true\\n          allowNpoDesignation: true\\n          allowSpecifiedFunds: true\\n        }) {\\n          id\\n          publishDate\\n          publishState\\n          expirationDate\\n          name\\n          subheading\\n\\n\\n          causes {\\n            id\\n            name\\n          }\\n\\n          localized {\\n            locale\\n            name\\n            subheading\\n            excerpt\\n            featuredVideo\\n            description\\n            primaryImageCrid\\n            primaryImageAlt\\n            ctaText\\n          }\\n        }\\n      }\"\n}"

          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String])
      ))
  }

  // Get Compaign
  val scnGetCompaign = scenario("GetCompaign").group(grpCompaign) {
    feed(timestampFeeder)
      .exec(
        http("GetCompaign")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"query {\\n           organization(\\n              id: 1000\\n             )\\n          {\\n          id,\\n            selectableContent(q:\\\"<campaignName>\\\"){\\n          __typename ... on Campaign {\\n          id,name\\n          }\\n          }\\n        }\\n      }\"\n}"

          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String])
      ))
  }


  /* Load Tests on Impact Fund usecases */

  val grpImpactFund = "ImpactFund"

  // Create Impact Fund
  val scnCreateImpactFund = scenario("CreateImpactFund").group(grpImpactFund) {
    feed(timestampFeeder)
      .exec(
        http("CreateImpactFund")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"mutation {\\n        test: impactFund(orgId: 1000, input:{\\n          publishDate: \\\"2018-03-19T19:15:34Z\\\"\\n          publishState:\\\"DRAFT\\\"\\n          expirationDate: \\\"2019-03-19T19:15:34Z\\\"\\n          tags: {\\n            causeIds: [2004, 2002, 2003, 2001]\\n            sdgIds: [1007, 1005, 1006, 1004]\\n          }\\n         accountingCode:\\\"Accounting Code\\\"\\n\\n          localized: {\\n            locale: \\\"en_US\\\"\\n            name: \\\"<impactFundName>\\\"\\n            subheading: \\\"Impact Fund SH\\\"\\n            excerpt: \\\"Impact Fund Excerpt\\\"\\n            featuredVideo: \\\"Blah Blah\\\"\\n            description: \\\"Blah Blah Blah\\\"\\n            primaryImageCrid: \\\"TestId\\\"\\n            primaryImageAlt: \\\"TestId\\\"\\n            ctaText: \\\"Read\\\"\\n          }\\n        }) {\\n          id\\n          publishDate\\n          publishState\\n          expirationDate\\n          name\\n          subheading\\n          causes {\\n            id\\n            name\\n          }\\n          localized {\\n            locale\\n            name\\n            subheading\\n            excerpt\\n            featuredVideo\\n            description\\n            primaryImageCrid\\n            primaryImageAlt\\n            ctaText\\n          }\\n        }\\n      }\"\n}"

          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String]))
      )
  }


  // Get Impact Fund
  val scnGetImpactFund = scenario("GetImpactFund").group(grpImpactFund) {
    feed(timestampFeeder)
      .exec(
        http("GetImpactFund")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"query {\\n           organization(\\n              id: 1000\\n             )\\n          {\\n          id,\\n            selectableContent(q:\\\"<impactFundName>\\\"){\\n          __typename ... on ImpactFund {\\n          id,name\\n          }\\n          }\\n        }\\n      }\"\n}"

          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String]))
      )
  }


  /* Load Tests on Organization usecases */

  val grpOrganization = "Organization"

  // Get Organization
  val scnGetOrganization = scenario("GetOrganization").group(grpOrganization) {
    feed(timestampFeeder)
      .exec(
        http("GetOrganization")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"{\\n  test: organization(id: 1000) {\\n    id\\n    type\\n    roles\\n    name\\n    dba\\n    externalId\\n    street\\n    street2\\n    city\\n    state\\n    postalCode\\n    postalCodeExt\\n    country\\n    phone\\n    website\\n    defaultLanguage\\n    defaultCurrency\\n    timeZone\\n    brandSettings {\\n      id\\n      communityLogo\\n      communityLogoCrid\\n      primaryColor\\n      secondaryColor\\n      footerContent1\\n      footerContent2\\n      footerContent3\\n    }\\n    homepageSettings {\\n      id\\n      organizationMessage {\\n        isHidden\\n        message\\n        metrics {\\n          label\\n          metric\\n        }\\n      }\\n      givingBaseline\\n      heroes {\\n        __typename\\n        ... on Story {\\n          id\\n          createdDate\\n          modifiedDate\\n          publishDate\\n          publishState\\n          expirationDate\\n          author {\\n            id\\n            firstName\\n            lastName\\n            email\\n            city\\n            gender\\n          }\\n        }\\n        __typename\\n        ... on Campaign {\\n          id\\n          createdDate\\n          modifiedDate\\n          publishDate\\n          publishState\\n          expirationDate\\n          author {\\n            id\\n            firstName\\n            lastName\\n            email\\n            city\\n            gender\\n          }\\n        }\\n        __typename\\n        ... on ImpactFund {\\n          id\\n          createdDate\\n          modifiedDate\\n          publishDate\\n          publishState\\n          expirationDate\\n          author {\\n            id\\n            firstName\\n            lastName\\n            email\\n            city\\n            gender\\n          }\\n        }\\n        __typename\\n        ... on NpoPage {\\n          id\\n        }\\n      }\\n    }\\n  }\\n}\\n\"\n}"

          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String]))
      )
  }


  /* Load Tests on Settings usecases */

  val grpSettings = "Settings"

  // Save Home Page Settings
  val scnHomePageSettings = scenario("HomePageSettings").group(grpSettings) {
    feed(timestampFeeder)
      .exec(
        http("HomePageSettings")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"mutation {\\n      test2: saveHomepageSettings(\\n        orgId: 1000\\n        homePageSettings: {\\n          featuredCards: {\\n            isHidden: false\\n            cards: [\\n              {\\n                id: 2\\n              }\\n              {\\n                id: 3\\n              }\\n            ]\\n          }\\n          heroes:[\\n            {\\n              id: 3\\n            }\\n            {\\n              id: 4\\n            }\\n            {\\n              id: 5\\n            }\\n          ]\\n          organizationMessage: {\\n            isHidden: true\\n              metrics: [\\n              {\\n                label: \\\"energy usage from renewable sources 2\\\"\\n                metric: \\\"14%\\\"\\n              }\\n              {\\n                label: \\\"cans of disaster relief drinking water donated 2\\\"\\n                metric: \\\"18M\\\"\\n              }\\n            ]\\n            message: \\\"Homepage Message Goes Here 2\\\"\\n          }\\n          givingBaseline: 456789\\n        }\\n      )\\n      {\\n        id\\n        name\\n        homepageSettings {\\n          heroes {\\n            __typename\\n          }\\n          featuredCards {\\n            isHidden\\n            cards {\\n              __typename\\n            }\\n          }\\n          organizationMessage {\\n            isHidden\\n            message\\n            metrics {\\n              label\\n              metric\\n            }\\n          }\\n          givingBaseline\\n        }\\n      }\\n    }\"\n}"

          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String]))
      )
  }


  // Save Brand Settings
  val scnBrandSettings = scenario("BrandSettings").group(grpSettings) {
    feed(timestampFeeder)
      .exec(
        http("BrandSettings")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"mutation {\\n        test2: saveBrandSettings(\\n          orgId: 1000\\n          brandSettings: {\\n            communityLogo: \\\"blah\\\"\\n            primaryColor: \\\"#455667\\\"\\n            secondaryColor: \\\"#566778\\\"\\n            footerContent1: \\\"Rich Text Content 1\\\"\\n            footerContent2: \\\"Rich Text Content 2\\\"\\n            footerContent3: \\\"Rich Text Content 3\\\"\\n          }\\n        )\\n        {\\n          id\\n          name\\n          dba\\n          brandSettings {\\n            communityLogo\\n            primaryColor\\n            secondaryColor\\n            footerContent1\\n            footerContent2\\n            footerContent3\\n          }\\n        }\\n      }\"\n}"
          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String]))
      )
  }


  /* Load Tests on Npo Page usecases */

  val grpNpoPage = "NpoPage"

  // Create NPO page
  val scnCreateNpoPage = scenario("CreateNpoPage").group(grpNpoPage) {
    feed(timestampFeeder)
      .exec(
        http("CreateNpoPage")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"mutation {\\n test: createNpoPage\\n (orgId: 1000,\\n   input:{\\n         localized: {\\n           locale: \\\"en_US\\\"\\n           name: \\\"NPOPage 1\\\"\\n           subheading: \\\"Campaign SH\\\"\\n           description: \\\"Blah Blah Blah\\\"\\n           primaryImageCrid: \\\"TestId\\\"\\n           primaryImageAlt: \\\"TestId\\\"\\n         }\\n   }) {\\n   id\\n   name\\n   description\\n   primaryImageAlt\\n   primaryImageCrid\\n\\n } \\n}\"\n}"
          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String]))
      )
  }

}
