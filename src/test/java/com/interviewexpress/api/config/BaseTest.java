package com.interviewexpress.api.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseTest
{
    protected static RequestSpecification requestSpec;

    @BeforeClass
    public void setup()
    {
        // Reads API Key from environment variable or falls back to standard test key
        String apiKey = System.getenv().getOrDefault("API_KEY", "aih_a436abaafd61c9cec8fc287abc760a25234c6e65e1f1daca0a83cb1d9c0e901c");

        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://client-api.interview.express")
                .addHeader("X-Api-Key", apiKey)
                .log(LogDetail.ALL) // Logs complete request details on execution for clear debugging
                .build();
    }
}


/*

public class BaseTest
{

    @BeforeClass
    public void setup() {

        String apiKey = System.getenv().getOrDefault("API_KEY", "aih_a436abaafd61c9cec8fc287abc760a25234c6e65e1f1daca0a83cb1d9c0e901c");

     protected static RequestSpecification   requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://client-api.interview.express")
                .addHeader("X-Api-Key", apiKey)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL) // Logs complete request details on execution for clear debugging
                .build();
    }
}

No. There are two Java problems with that version.

You have two different places

1. Outside the method → declare the variable

public class BaseTest {

    protected static RequestSpecification requestSpec;

This means:

"I am creating a variable called requestSpec that the whole class can use."

2. Inside setup() → give the variable its value

@BeforeClass
public void setup() {

    requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://client-api.interview.express")
            .addHeader("X-Api-Key", apiKey)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
}

This means:

"Now I am putting the actual RequestSpecification object into requestSpec."

Think of it like a box

First, create an empty box:

requestSpec
[          ]

Then inside setup(), fill the box:

requestSpec
[ Base URL
  API Key
  JSON
  Logging ]

So:

CLASS LEVEL
    ↓
declare requestSpec
    ↓
@BeforeClass setup()
    ↓
create configuration
    ↓
put it into requestSpec

That's why we don't write:

protected static RequestSpecification requestSpec = ...

inside setup().

protected static belongs to the class-level variable.

Inside the method, we simply do:

requestSpec = ...

That's the main thing to remember.
 */