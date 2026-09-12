package com.interviewexpress.api.tests;

import com.interviewexpress.api.clients.JobsClient;
import com.interviewexpress.api.config.BaseTest;
import com.interviewexpress.api.pojos.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ----------------------------------------------------------------------------
// INHERITANCE (Extending BaseTest)
// GetJobTest inherits from BaseTest to gain access to common test setup,
// such as base URIs, common headers, and the shared 'requestSpec' variable.
// ----------------------------------------------------------------------------
public class GetJobTest extends BaseTest {

    private JobsClient jobsClient;

    @BeforeClass
    public void jobSetUp() {
        // Initialize client once using the requestSpec inherited from BaseTest
        jobsClient = new JobsClient(requestSpec);
    }

    @Test
    public void happyPath() {

        // 1. Act: Call the client method
        Response response = jobsClient.getAllJObs();
        // 2. Assert Status Code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        // 2. Headers & Content-Type Verification
        String contentType = response.getHeader("Content-Type");
        Assert.assertNotNull(contentType, "Content-Type header missing");
        Assert.assertTrue(contentType.contains("application/json"), "Expected application/json header");

        // 3. Payload Root Structure
        JsonPath json = response.jsonPath();
        Assert.assertNotNull(json.get("total"), "Root key 'total' missing");
        Assert.assertNotNull(json.get("page"), "Root key 'page' missing");
        Assert.assertNotNull(json.get("pageSize"), "Root key 'pageSize' missing");
        Assert.assertNotNull(json.get("totalPages"), "Root key 'totalPages' missing");
        Assert.assertNotNull(json.get("items"), "Root key 'items' missing");

        // 4. Non-Empty Items Array
        List<Map<String, Object>> items = json.getList("items");
        Assert.assertNotNull(items, "Items array should not be null");
        Assert.assertTrue(items.size() > 0, "Items array should not be empty");

        // 5. Field Data Type Integrity
        Assert.assertTrue(json.get("total") instanceof Integer, "'total' field must be an Integer");
        Assert.assertTrue(json.get("page") instanceof Integer, "'page' field must be an Integer");
        Assert.assertTrue(items.get(0).get("id") instanceof String, "Job 'id' field must be a String");

        // 6. ISO 8601 Date Format Verification
        String createdAt = json.getString("items[0].createdAt");
        String iso8601Regex = "^\\d{4}-\\d{2}-\\d{2}(T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?(Z|[+-]\\d{2}:\\d{2})?)?$";
        if (createdAt != null) {
            Assert.assertTrue(createdAt.matches(iso8601Regex), "createdAt timestamp is not valid ISO 8601 format");
        }


    }

    // =========================================================================
    // SECTION 2: Query Parameters, Pagination & Filtering
    // =========================================================================

    /**
     * 1. Valid Pagination Execution
     */
    @Test(description = "Test 2.1: Valid Pagination Execution")
    public void testValidPaginationExecution() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", 1);
        queryParams.put("pageSize", 5);

        Response response = jobsClient.getAllJobs(queryParams);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");
        Assert.assertEquals(response.jsonPath().getInt("page"), 1, "Page metadata should be 1");
        Assert.assertEquals(response.jsonPath().getInt("pageSize"), 5, "PageSize metadata should be 5");

        List<Object> items = response.jsonPath().getList("items");
        Assert.assertNotNull(items, "Items list should not be null");
        Assert.assertTrue(items.size() <= 5, "Items size should not exceed requested pageSize of 5");
    }

    /**
     * 2. Default Pagination Fallback
     */
    @Test(description = "Test 2.2: Default Pagination Fallback")
    public void testDefaultPaginationFallback() {
        Response response = jobsClient.getAllJobs(new HashMap<>());

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");
        Assert.assertEquals(response.jsonPath().getInt("page"), 1, "Default page should be 1");
        Assert.assertEquals(response.jsonPath().getInt("pageSize"), 10, "Default pageSize should be 10");

        List<Object> items = response.jsonPath().getList("items");
        Assert.assertNotNull(items, "Items array should not be null");
        Assert.assertTrue(items.size() <= 10, "Items size should adhere to server default limit of 10");
    }

    /**
     * 3. Single Filter Matching (status)
     */
    @Test(description = "Test 2.3: Single Filter Matching (status)")
    public void testSingleFilterMatchingStatus() {
        Map<String, Object> parms = new HashMap<>();
        parms.put("status", "active");

        Response response = jobsClient.getAllJobs(parms);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        List<String> statuses = response.jsonPath().getList("items.status");
        Assert.assertNotNull(statuses, "Statuses list should not be null");

        for (String status : statuses) {
            Assert.assertEquals(status, "active", "Every item's status must match query param 'ACTIVE'");
        }
    }

    /**
     * 4. Single Filter Matching (department / title)
     */
    @Test(description = "Test 2.4: Single Filter Matching (search)")
    public void testSingleFilterMatchingSearch() {
        Map<String, Object> parms = new HashMap<>();
        parms.put("search", "Engineer");

        Response response = jobsClient.getAllJobs(parms);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        List<String> titles = response.jsonPath().getList("items.title");
        Assert.assertNotNull(titles, "Titles list should not be null");

        for (String title : titles) {
            Assert.assertTrue(title.toLowerCase().contains("engineer"),
                    "Every job title should contain the search term 'Engineer'");
        }
    }


    /**
     * 5. Combined Filtering & Pagination
     */
    @Test(description = "Test 2.5: Combined Filtering & Pagination")
    public void testCombinedFilteringAndPagination() {
        Map<String, Object> parms = new HashMap<>();
        parms.put("page", 1);
        parms.put("pageSize", 2);
        parms.put("status", "active");
        parms.put("search", "Engineer");

        Response response = jobsClient.getAllJobs(parms);

        // Validate Status Code & Metadata
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");
        Assert.assertEquals(response.jsonPath().getInt("page"), 1, "Page metadata should be 1");
        Assert.assertEquals(response.jsonPath().getInt("pageSize"), 2, "PageSize metadata should be 2");

        // Extract item details using valid JSONPaths
        List<String> statuses = response.jsonPath().getList("items.status");
        List<String> titles = response.jsonPath().getList("items.title");
        List<String> descriptions = response.jsonPath().getList("items.description");

        // Ensure item count respects pageSize limit
        Assert.assertTrue(statuses.size() <= 2, "Items list length should not exceed pageSize");

        // Validate filtered attributes
        for (int i = 0; i < statuses.size(); i++) {
            Assert.assertEquals(statuses.get(i), "active", "Status must match query param 'active'");

            String title = titles.get(i) != null ? titles.get(i).toLowerCase() : "";
            String description = descriptions.get(i) != null ? descriptions.get(i).toLowerCase() : "";

            Assert.assertTrue(
                    title.contains("engineer") || description.contains("engineer"),
                    "Either title or description should match the search keyword 'Engineer'"
            );
        }
    }

    /**
     * 6. Non-Matching Filter Results
     */
    @Test(description = "Test 2.6: Non-Matching Filter Results")
    public void testNonMatchingFilterResults() {
        Map<String, Object> params = new HashMap<>();
        params.put("search", "NON_EXISTENT_JOB_TITLE_XYZ");

        Response response = jobsClient.getAllJobs(params);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");
        Assert.assertEquals(response.jsonPath().getInt("total"), 0, "Total count should be 0");

        List<Object> items = response.jsonPath().getList("items");
        Assert.assertNotNull(items, "Items array should not be null");
        Assert.assertTrue(items.isEmpty(), "Items list should be empty []");
    }


// =========================================================================
// SECTION 3: Error code
// =========================================================================

    @Test(description = "401: Invalid API key")
    public void testInvalidApiKey() {
        Response response = jobsClient.getAllJobsWithCustomAuth("aih_totally_invalid_key_123");

        Assert.assertEquals(response.getStatusCode(), 401, "Expected 401 for invalid auth");

        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        Assert.assertNotNull(errorResponse.getError(), "Error field should not be null");
        Assert.assertNotNull(errorResponse.getMessage(), "Message field should not be null");
        Assert.assertNotNull(errorResponse.getTimestamp(), "Timestamp field should not be null");
        Assert.assertNotNull(errorResponse.getTraceId(), "TraceId field should not be null");

        System.out.println("Error: " + errorResponse.getError());
        System.out.println("Message: " + errorResponse.getMessage());

        System.out.println("======Full error response body=============");
        response.prettyPrint();
    }

    @Test(description = "401:Empty api key")
    public void testMissingApiKey()
    {
        Response response = jobsClient.getAllJobsWithCustomAuth("");
        Assert.assertEquals(response.getStatusCode(),401,"Expected 401 for missing Api key");

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        System.out.println("Error :" + errorResponse.getError());
        System.out.println("Message :"+ errorResponse.getMessage());
    }
@Test(description = "401: white space")
    public void testWithSpaceApiKey()
{
    Response response = jobsClient.getAllJobsWithCustomAuth("  ");
    Assert.assertEquals(response.getStatusCode(), 401, "Expected 401 for white space");


    ErrorResponse errorResponse = response.as(ErrorResponse.class);
    System.out.println("Error :" + errorResponse.getError());
    System.out.println("Message :"+ errorResponse.getMessage());

}

@Test(description = "404: Bad request")
    public void testWithBadRequest()
{
    // jobId is expected to be in UUID format.
    // A valid but non-existent UUID is used here so that the API
    // passes UUID validation and returns 404 (Job Not Found).
    Response response = jobsClient.getJobById("550e8400-e29b-41d4-a716-446655440000\n");

    System.out.println(response.asPrettyString());

    Assert.assertEquals(response.getStatusCode(),404, "Expected 404 by passing wrong jobId");

    ErrorResponse error = response.as(ErrorResponse.class);
    System.out.println("Error: " + error.getError());
    System.out.println("Message :" + error.getMessage());

}

@Test(description = "422 : test Validation error by wrong job id format")
    public void testwithValidation()
{
    Response response = jobsClient.getJobById("399");
    Assert.assertEquals(response.getStatusCode(), 422);



}
    @Test(description = "422: Invalid page query parameter type")
    public void testWithInvalidPageQueryParam()
{
    Map<String, Object> parms = new HashMap<>();
    parms.put("page", "ssss");

    Response response= jobsClient.getAllJobs(parms);
    Assert.assertEquals(response.getStatusCode(), 422);

    HTTPValidationError errorResponse = response.as(HTTPValidationError.class);
    System.out.println("details: "+ errorResponse.getDetail());

    ValidationErrorDetail detail = errorResponse.getDetail().get(0);
    System.out.println("Error Type: "+detail.getType());
    System.out.println("Message: " + detail.getMsg());
    System.out.println("Input :" + detail.getInput());
    System.out.println("ctx  :" + detail.getCtx());

}

    @Test(description = "422: pageSize exceeds maximum allowed value")
    public void testWithInvalidPageSizeQueryParam()
    {
        Map<String, Object> parms = new HashMap<>();
        parms.put("pageSize", "101");

        Response response= jobsClient.getAllJobs(parms);
        Assert.assertEquals(response.getStatusCode(), 422);
        HTTPValidationError errorResponse = response.as(HTTPValidationError.class);
        System.out.println("details: "+ errorResponse.getDetail());

        ValidationErrorDetail detail = errorResponse.getDetail().get(0);
        System.out.println("Error Type: "+detail.getType());
        System.out.println("Message: " + detail.getMsg());
        System.out.println("Input :" + detail.getInput());
        System.out.println("ctx  :" + detail.getCtx());

    }
    @Test(description = "422: pageSize is below minimum allowed value")
    public void testWithMinimumInvalidPageSizeQueryParam()
    {
        Map<String, Object> parms = new HashMap<>();
        parms.put("pageSize", "0");

        Response response= jobsClient.getAllJobs(parms);
        Assert.assertEquals(response.getStatusCode(), 422);
        HTTPValidationError errorResponse = response.as(HTTPValidationError.class);
        System.out.println("details: "+ errorResponse.getDetail());

        ValidationErrorDetail detail = errorResponse.getDetail().get(0);
        System.out.println("Error Type: "+detail.getType());
        System.out.println("Message: " + detail.getMsg());
        System.out.println("Input :" + detail.getInput());
        System.out.println("ctx  :" + detail.getCtx());

    }


    }
