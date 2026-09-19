package com.interviewexpress.api.tests;

import com.interviewexpress.api.clients.JobsClient;
import com.interviewexpress.api.config.BaseTest;
import com.interviewexpress.api.pojos.JobResponse;
import com.interviewexpress.api.utils.JobUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.checkerframework.checker.units.qual.N;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class UpdateJobTest extends BaseTest {
    private JobsClient jobsClient;
    private String JobId;


    @BeforeClass
    public void set() {
        jobsClient = new JobsClient(requestSpec);
        JobId = JobUtils.createJobAndGetId(requestSpec);
    }

    @Test(description = "200: Happy path for updating a job")
    public void testUpdateJobSuccess() {

        // Step 1: Get the current job details from the server using JobId
        Response response = jobsClient.getJobById(JobId);

        Assert.assertEquals(response.contentType(), "application/json");

        // Convert the JSON response into a Java object so we can read its fields
        JobResponse newCreatedJobResponse = response.as(JobResponse.class);

        // Print the original job details before making any updates
        System.out.println("Job Id :" + newCreatedJobResponse.getId());
        System.out.println("Before updated Title  : " + newCreatedJobResponse.getTitle());
        System.out.println("Before updated Description of job : " + newCreatedJobResponse.getDescription());
        System.out.println("Before update Min Experience Years :" + newCreatedJobResponse.getMinExperienceYears());

        Assert.assertNotNull(newCreatedJobResponse.getId());
        Assert.assertNotNull(newCreatedJobResponse.getTitle());
        Assert.assertNotNull(newCreatedJobResponse.getDescription());
        Assert.assertNotNull(newCreatedJobResponse.getMinExperienceYears());


        // Step 2: Prepare the new values we want to update using a Map
        Map<String, Object> updatePayload = new HashMap<>();
        updatePayload.put("title", "Senior software Test Engineer(patched)");
        updatePayload.put("description", "Rest assured, maven project with rest api");
        updatePayload.put("minExperienceYears", 5);

        // Step 3: Send the PATCH request to update the job
        Response patchResponse = jobsClient.updateJob(JobId, updatePayload, requestSpec);

        Assert.assertEquals(patchResponse.getStatusCode(), 200, "Expected status code 200 OK");

        // Convert the updated JSON response back into a Java object
        JobResponse newUpdatedJobResponse = patchResponse.as(JobResponse.class);

        Assert.assertEquals(patchResponse.contentType(), "application/json");

        // Step 4: Print the updated job details returned from the server
        System.out.println("\n========== AFTER UPDATE (PATCH RESPONSE) ==========");
        System.out.println("Job id :" + newUpdatedJobResponse.getId());
        System.out.println(" Title :" + newUpdatedJobResponse.getTitle());
        System.out.println("description : " + newUpdatedJobResponse.getDescription());
        System.out.println("minExperienceYears: " + newUpdatedJobResponse.getMinExperienceYears());

// Verify that the Job ID returned in the response matches the Job ID sent in the request
        Assert.assertEquals(newUpdatedJobResponse.getId(), JobId, "Response Job ID should match the requested Job ID");

        Assert.assertEquals(newUpdatedJobResponse.getTitle(), "Senior software Test Engineer(patched)");
        Assert.assertEquals(newUpdatedJobResponse.getDescription(), "Rest assured, maven project with rest api");
        Assert.assertEquals(newUpdatedJobResponse.getMinExperienceYears().intValue(), 5);
    }
}