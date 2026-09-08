package com.interviewexpress.api.pojos;


import java.util.List;

public class HTTPValidationError {

    private List<ValidationErrorDetail> detail;

    // 1. Mandatory No-Arg Constructor
    public HTTPValidationError() {}

    // 2. Parameterized Constructor
    public HTTPValidationError(List<ValidationErrorDetail> detail) {
        this.detail = detail;
    }

    // Getter and Setter
    public List<ValidationErrorDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<ValidationErrorDetail> detail) {
        this.detail = detail;
    }
}
/*
Step 2: Create the Outer Response Class (HTTPValidationError.java)
Next, create the wrapper POJO representing the top-level 422 response body containing the detail list.


How to Use It in Your Tests
When an API call returns HTTP 422, deserialize the response directly into HTTPValidationError:

Response response = jobsClient.createJob(invalidRequest);

if (response.getStatusCode() == 422) {
    HTTPValidationError validationError = response.as(HTTPValidationError.class);

    // Validate that errors exist
    Assert.assertFalse(validationError.getDetail().isEmpty());

    // Inspect specific error details
    ValidationErrorDetail firstError = validationError.getDetail().get(0);
    System.out.println("Error Message: " + firstError.getMsg());
    System.out.println("Field Path: " + firstError.getLoc());
} else {
    ErrorResponse standardError = response.as(ErrorResponse.class);
    // Handle standard 400, 401, 403, 404, 500 errors...
}
 */