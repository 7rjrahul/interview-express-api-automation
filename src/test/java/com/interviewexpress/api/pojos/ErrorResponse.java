package com.interviewexpress.api.pojos;


import java.util.List;

public class ErrorResponse {

    private Integer statusCode;
    private String error;
    private Object message; // Can be a String or a List<String>
    private String timestamp;
    private String traceId;

    // 1. Mandatory No-Arg Constructor (for Jackson Deserialization)
    public ErrorResponse()
    {
    }

    // 2. Parameterized Constructor
    public ErrorResponse(Integer statusCode, String error, Object message, String timestamp, String traceId) {
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
        this.traceId = traceId;
    }

    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }


    }
/*
    ├── ErrorResponse -> 400/401/403/404/409/500
    this class will handle all the above error code except 422.

    For 422 , we build validationErrorDetails and HTTPValidationError


    Complete POJO Inventory for Create Job API

1. Request POJOs (Sending Data)
InterviewQuestion.java (Child POJO): Contains id, text, and answerHint.

CreateJobPayload.java (Data POJO): Contains job fields (title, description, minExperienceYears, department, interviewRigor, interviewNotes, status, autoGenerateScreening) + List<InterviewQuestion>.

CreateJobRequest.java (Outer Wrapper POJO): Wraps CreateJobPayload and java.io.File for multipart/form-data requests.

2. Response POJOs (Deserializing Server Responses)
CreateJobResponse.java (Success 201): Maps created job metadata (id, createdAt, updatedAt, fileUrl) alongside returned job details.

ErrorResponse.java (Standard Error 400, 401, 403, 404, 500): Maps standard error JSON (statusCode, error, message, timestamp, traceId).

ValidationErrorDetail.java & HTTPValidationError.java (FastAPI 422 Validation Error): Maps detailed schema validation failure arrays (detail -> loc, msg, type, input, ctx).

 */