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
}


