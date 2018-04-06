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
    "Authorization" -> "Bearer eyJraWQiOiIyMTIiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoicWMxbGdhbHdfSjE3LUkybklSUHg1ZyIsInN1YiI6Imh0dHBzOi8vdGVzdC5zYWxlc2ZvcmNlLmNvbS9pZC8wMEQwdDAwMDAwMDhmeDlFQUEvMDA1MHQwMDAwMDBYN2JHQUFTIiwiYXVkIjoiM01WRzlpZm1BS0NISVNiYVR6Q3JuQWx3SVhmTERNNGV2UUxmcTZDVkpIVnd2ZzdrOHR3VGVReHJzYWszMGhERHo0VHppNzFncC56bUZLbXVfRWxnciIsImlzcyI6Imh0dHBzOi8vcWEyLXBoaWxhbnRocm9weWNsb3VkLmNzNzcuZm9yY2UuY29tLyIsImV4cCI6MTUyMzAwMTcyOCwiaWF0IjoxNTIzMDAxNjA4LCJub25jZSI6IjE1MjMwMDE1NzI3MzQwMCIsImN1c3RvbV9hdHRyaWJ1dGVzIjp7IlVzZXJuYW1lIjoidGVzdHVzZXJAc2FsZXNmb3JjZS5jb20ucWEyIn19.XOBMhZaL6TCNHiO_eJpyZuYcoJNT3O82pOGpe6Pi3lx5i7M0emmoZEaxtO-A6rAK7DV_SWloj3C_eixUI3qhcvzvMksEHbfaS_Dk-IFU3KgNCas7KYDNdtLyspMHST_8ffnDNNm_wx--g4yBvL4cozhjWtKK4NEftpoOD6wBMg7Rvbht4e8QMSzdyVeDMZDuHEf1ptE8FEDdyV57_3IJada4hbcy-XqFzRZePQ5dDcK5p4mVnIiuuaIXid79PrBFauMK1c74qP7tec8grwHVYgSHS_rZqPQzSIp5XjBAQRZ6Fem7B40Y8krrX4m__4HU384plf8YJdTdoFfyeXE9aLzj7GQLksQ-iulTvyGDwLGuzhGwfDbiRWUG-qCRTQUbVvS7zXn8bLJOhignXN8wcRiSGUfDoBoc1rirF-ybvwVR8UuWRPY_utxfJPKGBsAVga39OsVu6mVFGYomfFqSWhFLKhvKoaLn5YFfjqCDkjo8iDj0qzGfysKaQTD588dR1NSJ15_hOOA81JBSEzI-qQexIXf8IKBS72OtHscVyFlvH1ZCgIWHopIGDbdrQhJKqyPvb0ZMaIjVwBznGKlYWXziD_f8UuBouz3rljBTm83BKPguSSR9rGru2sdfvXrCA3ChwT6J2UiYWLKJmxuaE56bZ0IAVXM4DO5alQ-36Ro"
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
            "{\n\t\"query\": \"query {\\n      story(id: 1) {\\n        id\\n        publishDate\\n        publishState\\n        expirationDate\\n        name\\n        subheading\\n        excerpt\\n        featuredVideo\\n        description\\n        primaryImageCrid\\n        primaryImageAlt\\n        ctaText\\n        causes {\\n          id\\n        }\\n        sdgs {\\n          id\\n        }\\n        localized {\\n          locale\\n          name\\n          subheading\\n          excerpt\\n          featuredVideo\\n          description\\n          primaryImageCrid\\n          primaryImageAlt\\n          ctaText\\n        }\\n        relatedContent {\\n          ...ContentCard\\n          __typename\\n        }\\n      }\\n    }\\n\\n    fragment ContentCard on Content {\\n      ... on Story {\\n        ...StoryCard\\n        __typename\\n      }\\n      ... on ImpactFund {\\n        ...ImpactFundCard\\n        __typename\\n      }\\n      ... on Campaign {\\n        ...CampaignCard\\n        __typename\\n      }\\n      ... on NpoPage {\\n        ...NonprofitCard\\n        __typename\\n      }\\n      __typename\\n    }\\n\\n    fragment StoryCard on Story {\\n      id\\n      name\\n      __typename\\n    }\\n\\n    fragment ImpactFundCard on ImpactFund {\\n      id\\n      name\\n      __typename\\n    }\\n\\n    fragment CampaignCard on Campaign {\\n      id\\n      name\\n      __typename\\n    }\\n\\n    fragment NonprofitCard on NpoPage {\\n      id\\n      name\\n      __typename\\n    }\"\n}"

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
