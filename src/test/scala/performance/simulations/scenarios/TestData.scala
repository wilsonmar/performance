package performance.simulations.scenarios

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.check._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._

/**
  * Created by Tarun Kale
  */

class TestData extends Simulation {

  val headers_common = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json",
    "Authorization" -> "Bearer eyJraWQiOiIyMTQiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiS25NY0FsZUQ4ZE5XcWxMTFp3ODFZZyIsInN1YiI6Imh0dHBzOi8vdGVzdC5zYWxlc2ZvcmNlLmNvbS9pZC8wMEQweDAwMDAwMDBOMnNFQUUvMDA1MHgwMDAwMDB5dG9DQUFRIiwiYXVkIjoiM01WRzlZYjVJZ3Fua0I0cUlCVUc3bHQ4RnEuS0FaVS5oX2hSWDlTR2t6eUZhb0tzbTdKTFouUFZLbnByOFNib2VEWGNLeVFvRzBVandCdklZQ1hEZyIsImlzcyI6Imh0dHBzOi8vcWEtcGhpbGFudGhyb3B5Y2xvdWQuY3M5NS5mb3JjZS5jb20iLCJleHAiOjE1MjYzNzExMjcsImlhdCI6MTUyNjM3MTAwNywibm9uY2UiOiIxNTI2MzcwOTU5NjQxMDAiLCJjdXN0b21fYXR0cmlidXRlcyI6eyJVc2VybmFtZSI6InRrYWxlK3Rlc3QxQHNhbGVzZm9yY2UuY29tIn19.nUWmiFMlmAPPBteHw7FrsuwRg7edqOhgDTaRk6NZiJJRWg0ME4rndclwJGvvuJY1Y5gJqS4jJFxTuSS3RopHiqD4dSA6yqOfY1E9fLG_RR6pci6Y1hhFEPz9FRBbQr_2kw3ZO_YuFZ2upC8h77iKTKI34i3k6PL71gGjceJ0iLuWw2RHXayaebsInK-lVsgSnsZiKWj-6IolTIquUiIaW-dgrc-K0KeyL1YWP1gOX6-_qO6tocpuAoqFNOJZE0eo2JNfpO2E3X_qjvTtEyOwrxWb5RABT_MBhf-YrXMnjh_v-YVSrzKnuoMAJuZoCcGG7sNTSdO0k2jKWqN3zvZG9unJLEzwyIzlq5lQAICI-0q4-QSQ5-Uwpsj-wlgjE0D5MRc3B0v__P1UZTtpJoXoI_RK4ciUlSHZnf5MvT15icj0tJMhd78Xx0rslV5oKI8h21DxOJzEa5F0V0r53NrDCB1EvfqiioPpP3BBs9IzyvvveKqYTJKWPvj5Uz0jl7c69cT7L6JRy81bnsuj_beTGcs2-FlF-ZUIpBhAqM2AbBvOVKlBzgOPn0NfBv_DAX3VnC2zrkhulSKY24F-sUXViYRaAU7w980nF1OfTu7FL_rMT3_iHlaI_k0WAeV9isbnAylyP5tawDQvOrjqApRKvtsgm_pCxTp2IAtrVaWnRvQ"
  )

  val orgLoader = csv("src/test/resources/performance/data/orgTestData.csv")

  def isNull[T] = new Validator[T] {
    val name = "isNull"

    def apply(actual: Option[T]): Validation[Option[T]] = {
      actual match {
        case Some(null) => Success(actual)
        case _ => Failure("Value is not Null")
      }
    }
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
            "{\"operationName\":\"SaveStory\",\"variables\":{\"orgId\":2175,\"StoryInput\":{\"publishDate\":\"2018-04-24T07:00:00.000Z\",\"expirationDate\":\"2018-05-13T07:00:00.000Z\",\"publishState\":\"Published\",\"tags\":{\"causeIds\":[\"2008\",\"2009\",\"2013\",\"2025\"],\"sdgIds\":[\"1015\",\"1017\",\"1003\",\"1013\"]},\"scope\":{\"workplace\":[{\"id\":2175}],\"country\":[\"US\"],\"whereRaised\":[],\"location\":[]},\"localized\":{\"locale\":\"en_US\",\"name\":\"PerfStory\",\"subheading\":\"PerfStory\",\"excerpt\":\"PerfStory\",\"ctaText\":\"Read\",\"description\":\"<p>PerfStory</p>\",\"primaryImageCrid\":\"f7eukopbls8txhlek6dp\"},\"relatedContent\":[{\"id\":\"659\"}]}},\"query\":\"mutation SaveStory($orgId: ID!, $storyId: ID, $StoryInput: StoryInput!) {\\n  story(orgId: $orgId, storyId: $storyId, input: $StoryInput) {\\n    ...EditStory\\n    __typename\\n  }\\n}\\n\\nfragment EditStory on Story {\\n  relatedContent {\\n    ...ContentCard\\n    __typename\\n  }\\n  tag: __typename\\n  id\\n  localized {\\n    locale\\n    name\\n    primaryImageCrid\\n    primaryImageAlt\\n    excerpt\\n    subheading\\n    ctaText\\n    featuredVideo\\n    description\\n    __typename\\n  }\\n  modifiedDate\\n  createdDate\\n  publishDate\\n  publishState\\n  expirationDate\\n  causes {\\n    id\\n    name\\n    __typename\\n  }\\n  sdgs {\\n    id\\n    name\\n    __typename\\n  }\\n  author {\\n    id\\n    firstName\\n    lastName\\n    __typename\\n  }\\n  scope {\\n    workplace {\\n      id\\n      name\\n      __typename\\n    }\\n    country\\n    whereRaised {\\n      id\\n      name\\n      __typename\\n    }\\n    location {\\n      latitude\\n      longitude\\n      unit\\n      label\\n      radius\\n      __typename\\n    }\\n    __typename\\n  }\\n  modifiedBy {\\n    id\\n    firstName\\n    lastName\\n    __typename\\n  }\\n}\\n\\nfragment ContentCard on Content {\\n  ... on Story {\\n    ...StoryCard\\n    __typename\\n  }\\n  ... on ImpactFund {\\n    ...ImpactFundCard\\n    __typename\\n  }\\n  ... on Campaign {\\n    ...CampaignCard\\n    __typename\\n  }\\n  ... on NpoPage {\\n    ...NpoPageCard\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment StoryCard on Story {\\n  tag: __typename\\n  id\\n  name\\n  primaryImageCrid\\n  primaryImageAlt\\n  excerpt\\n  subheading\\n  category: primaryCauseName\\n  ctaText\\n}\\n\\nfragment ImpactFundCard on ImpactFund {\\n  tag: __typename\\n  id\\n  name\\n  primaryImageCrid\\n  primaryImageAlt\\n  excerpt\\n  subheading\\n  category: primaryCauseName\\n  ctaText\\n}\\n\\nfragment CampaignCard on Campaign {\\n  tag: __typename\\n  id\\n  name\\n  primaryImageCrid\\n  primaryImageAlt\\n  excerpt\\n  subheading\\n  category: primaryCauseName\\n  ctaText\\n}\\n\\nfragment NpoPageCard on NpoPage {\\n  id\\n  name\\n  tag: __typename\\n  excerpt\\n  subheading\\n  description\\n  primaryImageCrid\\n  primaryImageAlt\\n  ctaText\\n  category: primaryCauseName\\n}\\n\"}"
          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String])
          ))
  }


  /* Load Tests on Campaign usecases */

  val grpCampaign = "Campaign"

  // Create Campaign
  val scnCreateCampaign = scenario("CreateCampaign").group(grpCampaign) {
    feed(timestampFeeder)
      .exec(
        http("CreateCampaign")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\"operationName\":\"SaveCampaign\",\"variables\":{\"orgId\":2175,\"input\":{\"publishDate\":\"2018-04-24T07:00:00.000Z\",\"expirationDate\":\"2018-05-13T07:00:00.000Z\",\"publishState\":\"Published\",\"tags\":{\"causeIds\":[\"2001\",\"2002\",\"2003\",\"2008\"],\"sdgIds\":[\"1001\",\"1010\",\"1013\",\"1003\"]},\"scope\":{\"workplace\":[{\"id\":2175}],\"country\":[\"US\"],\"whereRaised\":[],\"location\":[]},\"localized\":{\"locale\":\"en_US\",\"name\":\"PerfCampaign\",\"subheading\":\"PerfCampaign\",\"excerpt\":\"PerfCampaign\",\"ctaText\":\"Donate\",\"description\":\"<p>PerfCampaign</p>\",\"primaryImageCrid\":\"iglpxyxtm6qmmikxgpxj\"},\"oneTimeAskLadder\":null,\"recurringTimeAskLadder\":null,\"fundraisingGoal\":4200000,\"currency\":\"USD\",\"allowNpoDesignation\":true,\"allowSpecifiedFunds\":true,\"allowLocalizedFunds\":true,\"givingOptions\":{\"impactFundIds\":[\"900\"],\"npoPageIds\":[]}}},\"query\":\"mutation SaveCampaign($orgId: ID!, $campaignId: ID, $input: CampaignInput!) {\\n  campaign(orgId: $orgId, campaignId: $campaignId, input: $input) {\\n    ...EditCampaign\\n    __typename\\n  }\\n}\\n\\nfragment EditCampaign on Campaign {\\n  oneTimeAskLadder\\n  recurringTimeAskLadder\\n  fundraisingGoal\\n  impactFunds {\\n    id\\n    name\\n    __typename\\n  }\\n  npoPage {\\n    id\\n    name\\n    __typename\\n  }\\n  currency\\n  allowLocalizedFunds\\n  allowNpoDesignation\\n  allowSpecifiedFunds\\n  tag: __typename\\n  id\\n  localized {\\n    locale\\n    name\\n    primaryImageCrid\\n    primaryImageAlt\\n    excerpt\\n    subheading\\n    ctaText\\n    featuredVideo\\n    description\\n    __typename\\n  }\\n  modifiedDate\\n  createdDate\\n  publishDate\\n  publishState\\n  expirationDate\\n  causes {\\n    id\\n    name\\n    __typename\\n  }\\n  sdgs {\\n    id\\n    name\\n    __typename\\n  }\\n  author {\\n    id\\n    firstName\\n    lastName\\n    __typename\\n  }\\n  scope {\\n    workplace {\\n      id\\n      name\\n      __typename\\n    }\\n    country\\n    whereRaised {\\n      id\\n      name\\n      __typename\\n    }\\n    location {\\n      latitude\\n      longitude\\n      unit\\n      label\\n      radius\\n      __typename\\n    }\\n    __typename\\n  }\\n  modifiedBy {\\n    id\\n    firstName\\n    lastName\\n    __typename\\n  }\\n}\\n\"}"

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
            "{\"operationName\":\"saveImpactFund\",\"variables\":{\"orgId\":2175,\"input\":{\"publishDate\":\"2018-04-24T07:00:00.000Z\",\"expirationDate\":\"2018-05-13T07:00:00.000Z\",\"publishState\":\"Published\",\"tags\":{\"causeIds\":[\"2001\",\"2003\",\"2004\",\"2008\"],\"sdgIds\":[\"1010\",\"1014\",\"1015\",\"1003\"]},\"scope\":{\"workplace\":[{\"id\":2175}],\"country\":[\"US\"],\"whereRaised\":[],\"location\":[]},\"localized\":{\"locale\":\"en_US\",\"name\":\"PerfImpactFund\",\"subheading\":\"PerfImpactFund\",\"excerpt\":\"PerfImpactFund\",\"ctaText\":\"Donate\",\"description\":\"<p>PerfImpactFund</p>\",\"primaryImageCrid\":\"oblnsadjezkorcvic4ci\"},\"accountingCode\":\"PerfFund\"}},\"query\":\"mutation saveImpactFund($orgId: ID!, $impactFundId: ID, $input: ImpactFundInput!) {\\n  impactFund(orgId: $orgId, impactFundId: $impactFundId, input: $input) {\\n    ...EditImpactFund\\n    __typename\\n  }\\n}\\n\\nfragment EditImpactFund on ImpactFund {\\n  accountingCode\\n  tag: __typename\\n  id\\n  localized {\\n    locale\\n    name\\n    primaryImageCrid\\n    primaryImageAlt\\n    excerpt\\n    subheading\\n    ctaText\\n    featuredVideo\\n    description\\n    __typename\\n  }\\n  modifiedDate\\n  createdDate\\n  publishDate\\n  publishState\\n  expirationDate\\n  causes {\\n    id\\n    name\\n    __typename\\n  }\\n  sdgs {\\n    id\\n    name\\n    __typename\\n  }\\n  author {\\n    id\\n    firstName\\n    lastName\\n    __typename\\n  }\\n  scope {\\n    workplace {\\n      id\\n      name\\n      __typename\\n    }\\n    country\\n    whereRaised {\\n      id\\n      name\\n      __typename\\n    }\\n    location {\\n      latitude\\n      longitude\\n      unit\\n      label\\n      radius\\n      __typename\\n    }\\n    __typename\\n  }\\n  modifiedBy {\\n    id\\n    firstName\\n    lastName\\n    __typename\\n  }\\n}\\n\"}"

          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String]))
      )
  }



  /* Create Organization and Workplace*/
  val grpOrganization = "Organization"
  val scnCreateOrganization = scenario("CreateOrganization").group(grpOrganization) {
    feed(timestampFeeder)
      .feed(timestampFeeder2)
      .exec(
        http("CreateOrganization")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"mutation {\\n      createOrganization( \\n        name: \\\"PerfOrg_${getTimeinMS}\\\"\\n        dba: \\\"AX\\\"\\n        street: \\\"2112 Laguna St\\\"\\n        street2: \\\"#201\\\"\\n        city: \\\"Pleasanton\\\"\\n        state: \\\"CA\\\"\\n        postalCode: \\\"23456\\\"\\n        postalCodeExt: \\\"23\\\"\\n        country: \\\"US\\\"    \\n        phone: \\\"4089140148\\\"\\n        website: \\\"www.sfdo.org\\\"\\n        defaultLanguage: \\\"en_US\\\"\\n        defaultCurrency: \\\"USD\\\"\\n        timeZone: \\\"PST\\\"   \\n        isUnitedWay: true\\n      )\\n      {\\n          id\\n        name\\n        dba\\n    \\n      }\\n    }\"\n}"

          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String]))
          .check(jsonPath("$.data.createOrganization.id").saveAs("orgId"))
      )

      .exec(
        http("CreateWorkplace")
          .post("/graphql")
          .headers(headers_common)
          .body(StringBody(
            "{\n  \"query\": \"mutation {\\n  createWorkplace(orgId:${orgId}, processorId:1000, division: \\\"Perf Engineering\\\", reseller: 1000) {\\n    id\\n    \\n  }\\n}\"\n}"

          )).asJSON
          .check(status.is(200))
          .check(jsonPath("$.errors").validate(isNull[String]))
      )
  }


}
