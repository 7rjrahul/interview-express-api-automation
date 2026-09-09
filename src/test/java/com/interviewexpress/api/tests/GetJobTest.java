package com.interviewexpress.api.tests;

import com.interviewexpress.api.clients.JobsClient;
import com.interviewexpress.api.config.BaseTest;
import com.interviewexpress.api.pojos.JobListResponse;
import com.interviewexpress.api.pojos.JobResponse;
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
    @Test(description = "Test 2.1: Default Pagination Fallback")
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
    @Test(description = "Test 2.2: Single Filter Matching (status)")
    public void testSingleFilterMatchingStatus() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("status", "ACTIVE");

        Response response = jobsClient.getAllJobs(queryParams);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        List<String> statuses = response.jsonPath().getList("items.status");
        Assert.assertNotNull(statuses, "Statuses list should not be null");

        for (String status : statuses) {
            Assert.assertEquals(status, "ACTIVE", "Every item's status must match query param 'ACTIVE'");
        }
    }

    /**
     * 4. Single Filter Matching (department / title)
     */
    @Test(description = "Test 2.3: Single Filter Matching (department / title)")
    public void testSingleFilterMatchingDepartment() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("department", "Engineering");

        Response response = jobsClient.getAllJobs(queryParams);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        List<String> departments = response.jsonPath().getList("items.department");
        Assert.assertNotNull(departments, "Departments list should not be null");

        for (String dept : departments) {
            Assert.assertEquals(dept, "Engineering", "Every item's department must match query param 'Engineering'");
        }
    }

    /**
     * 5. Combined Filtering & Pagination
     */
    @Test(description = "Test 2.4: Combined Filtering & Pagination")
    public void testCombinedFilteringAndPagination() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", 1);
        queryParams.put("pageSize", 2);
        queryParams.put("status", "ACTIVE");
        queryParams.put("department", "Engineering");

        Response response = jobsClient.getAllJobs(queryParams);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");
        Assert.assertEquals(response.jsonPath().getInt("page"), 1, "Page metadata should be 1");
        Assert.assertEquals(response.jsonPath().getInt("pageSize"), 2, "PageSize metadata should be 2");

        List<String> statuses = response.jsonPath().getList("items.status");
        List<String> departments = response.jsonPath().getList("items.department");

        for (int i = 0; i < statuses.size(); i++) {
            Assert.assertEquals(statuses.get(i), "ACTIVE", "Status must match query param 'ACTIVE'");
            Assert.assertEquals(departments.get(i), "Engineering", "Department must match query param 'Engineering'");
        }
    }

    /**
     * 6. Non-Matching Filter Results
     */
    @Test(description = "Test 2.5: Non-Matching Filter Results")
    public void testNonMatchingFilterResults() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("title", "NON_EXISTENT_JOB_TITLE_XYZ");

        Response response = jobsClient.getAllJobs(queryParams);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");
        Assert.assertEquals(response.jsonPath().getInt("total"), 0, "Total count should be 0");

        List<Object> items = response.jsonPath().getList("items");
        Assert.assertNotNull(items, "Items array should not be null");
        Assert.assertTrue(items.isEmpty(), "Items list should be empty []");
    }
}



