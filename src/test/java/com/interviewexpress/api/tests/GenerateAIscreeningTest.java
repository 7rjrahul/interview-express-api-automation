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

 @Test(description = "200 : suceessful response")
    public void testSuccessFullResponse()
 {
Response response =jobsClient.generateScreening(JobId);

      //response.prettyPrint();
     Assert.assertEquals(response.getStatusCode(), 200 ,"Expected status code should be 200");

     JobResponse rest = response.as(JobResponse.class);
     System.out.println("Success Message :"+rest.getTitle());
     System.out.println("Description:" +rest.getDescription());
     System.out.println("Print the id :" +rest.getStatus());

 }

}
