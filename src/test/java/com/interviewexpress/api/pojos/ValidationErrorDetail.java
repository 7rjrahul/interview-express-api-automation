package com.interviewexpress.api.pojos;


import java.util.List;
import java.util.Map;

public class ValidationErrorDetail {

    private List<Object> loc; // Location of the field (e.g., ["body", "minExperienceYears"])
    private String msg;       // Human-readable error message
    private String type;      // Error type identifier (e.g., "value_error.number.not_ge")
    private Object input;     // The raw invalid input supplied
    private Map<String, Object> ctx; // Additional context details (if any)

    // 1. Mandatory No-Arg Constructor
    public ValidationErrorDetail() {}

    // 2. Parameterized Constructor
    public ValidationErrorDetail(List<Object> loc, String msg, String type, Object input, Map<String, Object> ctx) {
        this.loc = loc;
        this.msg = msg;
        this.type = type;
        this.input = input;
        this.ctx = ctx;
    }

    // Getters and Setters
    public List<Object> getLoc() {
        return loc;
    }

    public void setLoc(List<Object> loc) {
        this.loc = loc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public Map<String, Object> getCtx() {
        return ctx;
    }

    public void setCtx(Map<String, Object> ctx) {
        this.ctx = ctx;
    }
}

/*
Step 1: Create the Inner Item Class (ValidationErrorDetail.java)
First, create the class representing each individual validation failure item in the detail array.
 */