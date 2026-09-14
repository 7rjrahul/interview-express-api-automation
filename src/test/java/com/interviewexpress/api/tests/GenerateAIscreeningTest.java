package com.interviewexpress.api.tests;

import com.interviewexpress.api.clients.JobsClient;
import com.interviewexpress.api.config.BaseTest;
import com.interviewexpress.api.pojos.JobResponse;
import com.interviewexpress.api.utils.JobUtils;
import io.restassured.response.Response;
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
// verify the json content type
        Assert.assertEquals(response.contentType(), "application/json");


        // Deserialize the API response JSON into the JobResponse POJO
        JobResponse responseAiScreening = response.as(JobResponse.class);

// Verify that the Job ID returned in the response matches the Job ID sent in the request
        Assert.assertEquals(responseAiScreening.getId(), JobId, "Response Job ID should match requested Job ID");

// Print the requested and response Job IDs for verification/debugging
        System.out.println("Requested Job ID: " + JobId);
        System.out.println("Response Job ID: " + responseAiScreening.getId());

        // Verify that screeningCriteria is present and populated
        Assert.assertNotNull(responseAiScreening.getScreeningCriteria(),"screeningCriteria should not be null");
        Assert.assertFalse(responseAiScreening.getScreeningCriteria().isEmpty(),"screeningCriteria should not be empty");

        Assert.assertTrue(responseAiScreening.getScreeningCriteria().containsKey("must_have"));
        Assert.assertTrue(responseAiScreening.getScreeningCriteria().containsKey("nice_to_have"));

// Verify that interviewQuestions is present
        Assert.assertNotNull(responseAiScreening.getInterviewQuestions(), "interviewQuestions should not be null");

// Verify that at least one interview question is returned
        Assert.assertFalse(responseAiScreening.getInterviewQuestions().isEmpty(),  "interviewQuestions should not be empty");


        //========Verify core job details are returned==========

        // Verify mandatory core job details are not null
        Assert.assertNotNull(responseAiScreening.getTitle(), "title should not be null");

        Assert.assertNotNull(responseAiScreening.getDescription(), "description should not be null");

        Assert.assertNotNull(responseAiScreening.getStatus(), "status should not be null");

        // department and interviewRigor are nullable according to Swagger so leave this for now

        // =============Verify numeric fields are returned with valid values====================
        Assert.assertTrue(responseAiScreening.getMinExperienceYears() >= 0, "minExperienceYears should be zero or greater");

        Assert.assertTrue(responseAiScreening.getInviteCount() >= 0, "inviteCount should be zero or greater");

        Assert.assertTrue(responseAiScreening.getCompletedInterviewCount() >= 0, "completedInterviewCount should be zero or greater");
// Verify criteriaStale is false after successful screening generation
        Assert.assertFalse(responseAiScreening.getCriteriaStale(), "criteriaStale should be false after successful screening generation");

// Verify organization ID is returned
        Assert.assertNotNull(responseAiScreening.getOrgId(), "orgId should not be null");

// Verify createdAt timestamp is returned
        Assert.assertNotNull(responseAiScreening.getCreatedAt(), "createdAt should not be null");

// Verify updatedAt timestamp is returned
        Assert.assertNotNull(responseAiScreening.getUpdatedAt(), "updatedAt should not be null");

 }

}
