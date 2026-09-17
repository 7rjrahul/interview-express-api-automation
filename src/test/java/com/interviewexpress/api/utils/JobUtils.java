package com.interviewexpress.api.utils;

import com.interviewexpress.api.clients.JobsClient;
import com.interviewexpress.api.config.BaseTest;
import com.interviewexpress.api.pojos.CreateJobPayload;
import com.interviewexpress.api.pojos.CreateJobRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

public class JobUtils  {

    public static String createJobAndGetId(RequestSpecification requestSpec)
    {

        // --- Step 1: Prepare the request payload ---
        CreateJobRequest requestPayload;
        requestPayload = TestDataFactory.createValidJobRequestWithoutFile();

        // --- Step 2: Send the API request ---
        JobsClient client = new JobsClient(requestSpec);
        Response response = client.createJob(requestPayload);

      //  Validate successful creation before extracting ID ---
        Assert.assertEquals(response.getStatusCode(), 201, "Job creation failed during setup!");

        // --- Step 3: Extract & Return Job ID ---
        String jobId = response.jsonPath().getString("id");

        return jobId; // Returns the extracted UUID back to the caller
    }

    }


