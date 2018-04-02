package performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import performance.simulations.lib.CommonHeader._
import performance.simulations.lib.JenkinsParam._

/**
 * Created by Tarun Kale
 */

class PostRequests extends Simulation{

  val headers_common = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json",
    "Authorization" -> "Bearer eyJraWQiOiIyMTIiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiMFlxVjNiYkdENjVacm9yZzlnMVppQSIsInN1YiI6Imh0dHBzOi8vdGVzdC5zYWxlc2ZvcmNlLmNvbS9pZC8wMEQwdDAwMDAwMDhmeDlFQUEvMDA1MHQwMDAwMDBYN2JHQUFTIiwiYXVkIjoiM01WRzlpZm1BS0NISVNiYVR6Q3JuQWx3SVhmTERNNGV2UUxmcTZDVkpIVnd2ZzdrOHR3VGVReHJzYWszMGhERHo0VHppNzFncC56bUZLbXVfRWxnciIsImlzcyI6Imh0dHBzOi8vcWEyLXBoaWxhbnRocm9weWNsb3VkLmNzNzcuZm9yY2UuY29tLyIsImV4cCI6MTUyMTUzMDY5NCwiaWF0IjoxNTIxNTMwNTc0LCJub25jZSI6IjE1MjE1MzA1NTM0MjkwMCIsImN1c3RvbV9hdHRyaWJ1dGVzIjp7IlVzZXJuYW1lIjoidGVzdHVzZXJAc2FsZXNmb3JjZS5jb20ucWEyIn19.Cf9s6_z6qBy6IjV41ayrRbN1t64SjkkvhiJTbR8KDylyoJbYs6tXuWGXyrLlK9IhegsPF943LvSoQCgAu0wFFlbec8Lx1f12I3Cub-ZVZwm1yTkq28gRFXTpETEhsThd57fzMJzCXf048yR0AaeBnNUBBjc1nzy1WW6lDm3LHDAnSq3gXvIGkToGDTGpL9rVNlwHKz1DlxuE3pQICpOaJBxF2mAGqfR_n5Uq_DruvSfIO5QX4BdlCa-p1rJaMT2K9YVFnEJleeWi1wyMiMQNpMFrY3FsCeZT78PD2-ef0uNvNicmY0TJzWUBfPS-kH498dzN5FK2cF87uapVAB-q7UJj-gGgGGwvV3o7Vq_KYc8n4kJ5kaQ7zl--abKX2wBpy5hv9hIr8Kmbqbm4m9fdqiIU1hf_TeEUCGsZTFwec5pO7y8RBBvpAOhyuJbzJ6es5zynVHIbNWydLAF4OIN9MmgxZfyOjxfPsoArInC5a3H4JHsrG-BFNjsywOpU5opJZ2P3j9XJdAwBnA8fGPOzlvXDet-tJZs5JBoQJlOTkI3KhRe1n67QDtzvzH3VOMTQ66y0FwByFZc5KUiv4SETkf2sclggHi7XbohMMDAfUWI98GY9s3TEdpJOMfUmXtFULatMmvDmv-iOHzweQ6_1uNzQNoGpZ1zxvCQd5uQAaUo"
  )

  val grpProfile = "UserProfile"

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
    )


  }


  val grpPerson = "Person"

  val scnIsPersonActive = scenario("isPersonActive").group(grpPerson) {
    feed(timestampFeeder)
      .exec(
        http("IsPersonActive")
          .post("""/graphql""")
          .headers(headers_common)
          .body(StringBody(
            """{
                "query": "query isPersonActive(email\n: testuser@salesforce.com.qa2) {\n  email   \n pcActive     \n sfidActive\n }\n"
              }""")).asJSON
          .check(status.is(200))
      )


  }


  val grpCauses = "Causes"

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
      )


  }


  val grpSDG = "SDG"

  val scnListSDG = scenario("ListSDGs").group(grpCauses) {
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
      )


  }

}
