package com.interviewexpress.api.clients;

import com.interviewexpress.api.pojos.CreateJobRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class JobsClient {

    private static final String JOBS_ENDPOINT = "/v1/jobs";
    private final RequestSpecification requestSpec;
    private final ObjectMapper objectMapper;

    // Constructor: Inject base RequestSpecification from BaseTest
    public JobsClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Executes POST request to create a job using CreateJobRequest wrapper POJO.
     */
    public Response createJob(CreateJobRequest request) {
        try {
            // 1. Convert CreateJobPayload POJO to JSON String
            String jsonPayload = objectMapper.writeValueAsString(request.getData());

            // 2. Build multipart request
            RequestSpecification req = given()
                    .spec(requestSpec)
                    .contentType("multipart/form-data")
                    .multiPart("data", jsonPayload);

// 3. Attach file if provided AND exists on disk
            if (request.getFile() != null && request.getFile().exists()) {
                req.multiPart("file", request.getFile());
            }
            // 4. Send POST request and return raw Response
            return req.when().post(JOBS_ENDPOINT);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize CreateJobPayload to JSON string", e);
        }
    }

    /**
     * Overloaded method to send raw strings (useful for invalid JSON test cases)
     */
    public Response createJobWithRawBody(String rawJson, String contentType) {
        return given()
                .spec(requestSpec)
                .contentType(contentType)
                .body(rawJson)
                .when()
                .post(JOBS_ENDPOINT);
    }

    //==================================Below Get api method client operations==========================
    //===============================   Master test plan section 1            =====================================
    public Response getAllJObs() {
      /*
      Option A: Storing in a variable first (What you learned EARLIER)

      Response response = RestAssured
               .given()
               .spec(requestSpec)
               .when().get(JOBS_ENDPOINT);
       return response;
       */
/*
Why Option B works without typing Response response =:
1.RestAssured's .get("/v1/jobs") method creates and returns a Response object automatically behind the scenes.

2.Because your method header says public Response getAllJobs(), Java expects you to return a Response object.

3.Placing return right before given() hands that Response directly back to whoever called client.getAllJobs().

If Option A feels easier and clearer to read right now, use Option A! Both are completely valid in real-world frameworks.
 */
        //Option B: Returning directly (Shorter / Cleaner)
        //Sends the request and immediately returns the result in one line
        return given().spec(requestSpec).when().get(JOBS_ENDPOINT);
    }

    public Response getAllJobs(Map<String, Object> parms) {
      /*
      here do not need add the hard cording data
       parms.put("page", 1);
       parms.put("pageSize", 5);
       parms.put("status", "active");
       return given().spec(requestSpec).queryParams(queryParams).given().get(JOBS_ENDPOINT);

. Your client will always send those exact parameters.
.You won't be able to test page = -1 (invalid page test).
.You won't be able to test pageSize = 50000 (edge case test).
.You won't be able to test non-existent search filters.

The job of the Client is only to pass whatever map the Test class gives it!

       */

        return given()
                .spec(requestSpec)
                .queryParams(parms)
                .when()
                .get(JOBS_ENDPOINT);
    }

    public Response getJobById(String jobId) {
        return given()
                .spec(requestSpec)
                .pathParam("id", jobId)
                .when().get(JOBS_ENDPOINT + "{id}");

       /*
       How RestAssured executes this behind the scenes:
         If a test calls getJobById("123"):

     pathParam("id", jobId) sets id = "123".

get(JOBS_ENDPOINT + "/{id}") translates to GET /v1/jobs/123.
        */
    }

    public Response getAllJobsWithoutAuth()
           /*
           Do not use .spec(requestSpec) here, because requestSpec already contains your valid authentication tokens/headers.
Start with given() directly.
Specify Base Settings Manually:

Call .baseUri("[https://client-api.interview.express](https://client-api.interview.express)")
(or use your base URI constant) directly inside the chain to specify where to send the request
 without attaching any auth headers.
            */ {
        return given().baseUri("https://client-api.interview.express")
                .when()
                .get(JOBS_ENDPOINT);
    }
/*
Summary of JobsClient.java
You have now successfully built all the foundational driver methods required by our Master Test Plan:

getAllJobs() — Default list retrieval (Happy Path)

getAllJobs(Map<String, Object> queryParams) — Parameterized retrieval (Pagination & Filters)

getJobById(String jobId) — Single resource lookup by ID (Path params)

getAllJobsWithoutAuth() — Unauthenticated call (Security/401 testing)
 */
//===========================client for error code###########################

    public Response getErrorJobResponse() {
        return given().baseUri("https://client-api.interview.express")
                .when().get(JOBS_ENDPOINT);
    }
}