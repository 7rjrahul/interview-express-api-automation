package com.interviewexpress.api.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import com.interviewexpress.api.utils.ConfigUtil;  // Import your utils class

public class BaseTest {
    // This will hold the RestAssured request setup
    protected static RequestSpecification requestSpec;

    @BeforeClass
    public void setup() {
        /*
         Step 1: Get values from ConfigUtil
         - base.uri → comes from config file (dev/qa/local)
         - api.key → comes from config file or environment variable
        */
        String baseUri = ConfigUtil.get("base.uri");
        String apiKey = ConfigUtil.get("api.key");

        /*
         Step 2: Build the RestAssured request specification
         - Set base URI
         - Add API key header
         - Set content type to JSON
         - Enable logging for debugging
        */
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .addHeader("X-Api-Key", apiKey)
                .setContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }
}
