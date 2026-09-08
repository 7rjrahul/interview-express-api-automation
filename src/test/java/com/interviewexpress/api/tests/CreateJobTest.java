package com.interviewexpress.api.tests;

import com.interviewexpress.api.clients.JobsClient;
import com.interviewexpress.api.config.BaseTest;
import com.interviewexpress.api.pojos.CreateJobRequest;
import com.interviewexpress.api.pojos.JobResponse;
import com.interviewexpress.api.utils.TestDataFactory;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CreateJobTest extends BaseTest {

    private JobsClient jobsClient;

    @BeforeClass
    public void setupClient() {
        // Initialize client once before test execution
        jobsClient = new JobsClient(requestSpec);
    }

    @Test(description = "Verify successful job creation with payload")
    public void createValidJobTest()
    {
        // 1. Fetch test data (payload-only / safe request)
        CreateJobRequest request = TestDataFactory.createValidJobRequestWithoutFile();

        // 2. Execute POST API request
        Response response = jobsClient.createJob(request);

        // Print response details to debug if anything fails
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asPrettyString());

        // 3. Verify HTTP Status Code 201 Created
        Assert.assertEquals(response.getStatusCode(), 201, "Expected HTTP Status Code 201 Created");

        // 4. Deserialize response into POJO
        JobResponse jobResponse = response.as(JobResponse.class);

        // 5. Assert essential response payload fields
        Assert.assertNotNull(jobResponse.getId(), "Job ID should not be null");
        Assert.assertEquals(jobResponse.getTitle(), request.getData().getTitle(), "Job title mismatch");
        Assert.assertEquals(jobResponse.getDepartment(), request.getData().getDepartment(), "Department mismatch");
    }

    @DataProvider(name = "invalidTitleRequests")

    public Object[][] invalidTitleRequests()
    {
        return new Object[][]
                {
            { TestDataFactory.createJobRequestNullTitle() },
            { TestDataFactory.createJobRequestEmptyTitle() },
            { TestDataFactory.createJobRequestBlankWhitespaceTitle() }
    };
    }

    // TEST 2: Invalid Title Validations (Executes 3 times automatically)
    @Test(priority = 2, dataProvider = "invalidTitleRequests")
            public void testCreateJobInvalidTitle(CreateJobRequest request)
    {
        // 1. Execute the request using the instance variable 'jobsClient'
        Response response = jobsClient.createJob(request);

        // 2. Assert that the server rejects the invalid title with 400 Bad Request
        Assert.assertEquals(response.getStatusCode(), 400, "Expected HTTP status code 400 Bad Request");
    }

    }




/*
=============------------------------------
Initially we have created like this our Test class,
In the this  version, you created the actual data right inside the test method (setting up the titles, questions, numbers, and file paths line by line).

When you switched to TestDataFactory, you didn't change what data was being sent—you just moved
where that data was declared into a separate helper class (TestDataFactory), and then called it using one simple line:

Java
CreateJobRequest request = TestDataFactory.createValidJobRequest();

So:
First version: "Build the data line-by-line right here inside the test."
TestDataFactory version: "Get the ready-made data from my helper tool, so my test code stays short and clean."
----------==========================================


public class CreateJobTest extends BaseTest {

    private JobsClient jobsClient;

    @BeforeClass
    public void setupClient() {
        // Inject requestSpec from BaseTest into JobsClient using Composition
        jobsClient = new JobsClient(requestSpec);
    }

    @Test(description = "Verify successful job creation with payload and attachment")
    public void testCreateJobSuccess() {
        // 1. Prepare Child POJOs (Interview Questions)
        List<InterviewQuestion> questions = new ArrayList<>();
        questions.add(new InterviewQuestion("Q101", "Explain REST Assured framework design.", "Focus on layers and POJOs"));
        questions.add(new InterviewQuestion("Q102", "What is Jackson Object Mapper?", "Serialization and deserialization"));

        // 2. Prepare Payload POJO
        CreateJobPayload payload = new CreateJobPayload(
                "Senior QA Automation Engineer",
                "Responsible for API test framework design",
                5,
                "Engineering",
                "High",
                "Focus on Java and API skills",
                "active",
                true,
                questions
        );

        // 3. Wrap Payload and optional File into CreateJobRequest
        File jobSpecFile = new File("src/test/resources/sample_jd.pdf");
        CreateJobRequest request = new CreateJobRequest(payload, jobSpecFile);

        // 4. Call API via Client Class
        Response response = jobsClient.createJob(request);

        // 5. Assert Status Code
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code 201 Created");

        // 6. Deserialize Response to POJO & Assert Fields
        CreateJobResponse createJobResponse = response.as(CreateJobResponse.class);

        Assert.assertNotNull(createJobResponse.getId(), "Job ID should not be null");
        Assert.assertEquals(createJobResponse.getTitle(), payload.getTitle());
        Assert.assertEquals(createJobResponse.getDepartment(), payload.getDepartment());
        Assert.assertEquals(createJobResponse.getInterviewQuestions().size(), 2);
    }

    @Test(description = "Verify validation failure (422) when mandatory fields are missing")
    public void testCreateJobValidationError() {
        // 1. Payload with null/invalid fields
        CreateJobPayload invalidPayload = new CreateJobPayload();
        invalidPayload.setDescription("Missing title and mandatory fields");

        CreateJobRequest request = new CreateJobRequest(invalidPayload);

        // 2. Execute Request
        Response response = jobsClient.createJob(request);

        // 3. Assert Status Code for FastAPI/Pydantic validation failure
        Assert.assertEquals(response.getStatusCode(), 422, "Expected status code 422 Unprocessable Entity");

        // 4. Deserialize to HTTPValidationError POJO
        HTTPValidationError validationError = response.as(HTTPValidationError.class);

        Assert.assertNotNull(validationError.getDetail(), "Detail list should not be null");
        Assert.assertFalse(validationError.getDetail().isEmpty(), "Detail list should contain validation errors");

        // 5. Inspect specific detail item
        ValidationErrorDetail firstError = validationError.getDetail().get(0);
        Assert.assertNotNull(firstError.getMsg(), "Error message should be present");
    }

    @Test(description = "Verify standard error handling (400) for malformed payload")
    public void testCreateJobBadRequest() {
        // 1. Send invalid raw string payload to trigger 400 Bad Request
        String malformedJson = "{ \"title\": \"Incomplete JSON\" ";

        // 2. Execute request with raw string method
        Response response = jobsClient.createJobWithRawBody(malformedJson, "application/json");

        // 3. Assert Status Code
        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 Bad Request");

        // 4. Deserialize to standard ErrorResponse POJO
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        Assert.assertEquals(errorResponse.getStatusCode(), Integer.valueOf(400));
        Assert.assertEquals(errorResponse.getError(), "Bad Request");
    }
}

 */