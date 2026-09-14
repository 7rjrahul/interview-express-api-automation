package com.interviewexpress.api.tests;

import com.interviewexpress.api.clients.JobsClient;
import com.interviewexpress.api.config.BaseTest;
import com.interviewexpress.api.pojos.JobResponse;
import com.interviewexpress.api.utils.JobUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class GenerateAIscreeningTest extends BaseTest
{
 private JobsClient jobsClient;
 private String JobId;

 @BeforeClass
 public void setJobsClient()
 {
     jobsClient = new JobsClient(requestSpec);

     JobId = JobUtils.createJobAndGetId(requestSpec);
 }

    @Test(description = "200: Successful response")
    public void testSuccessfulResponse()
    {
        // Send the Job ID to the Generate Screening API and capture the API response
        Response response = jobsClient.generateScreening(JobId);

        // Verify that the API returns HTTP 200
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code should be 200");

        // Extract the Job ID returned in the response JSON
        String responseJobId = response.jsonPath().getString("id");

        // Print the Job ID returned by the API
        System.out.println("Response Job ID: " + responseJobId);

        // Verify that the Job ID sent in the request matches the Job ID returned in the response
        Assert.assertEquals(JobId, responseJobId);

        // Print whether the request Job ID and response Job ID are the same
        System.out.println("Job IDs match: " + JobId.equals(responseJobId));

        // Print the complete response JSON for debugging/inspection
        System.out.println(response.asPrettyString());


 }

}
