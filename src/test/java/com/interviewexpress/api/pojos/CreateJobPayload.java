package com.interviewexpress.api.pojos;

import java.util.List;

public class CreateJobPayload
{
    private String title;
    private String description;
    private int minExperienceYears;
    private String department;
    private String interviewRigor;
    private String interviewNotes;
    private List<InterviewQuestion> interviewQuestions;
    private String status;
    private boolean autoGenerateScreening;

    // non parameter constructor
    public CreateJobPayload()
    {

    }

// Parameterized Constructor for quick instantiation in tests

    public CreateJobPayload(String title, String description, Integer minExperienceYears,
                            String department, String interviewRigor, String interviewNotes,
                            String status, Boolean autoGenerateScreening,
                            List<InterviewQuestion> interviewQuestions) {
        this.title = title;
        this.description = description;
        this.minExperienceYears = minExperienceYears;
        this.department = department;
        this.interviewRigor = interviewRigor;
        this.interviewNotes = interviewNotes;
        this.status = status;
        this.autoGenerateScreening = autoGenerateScreening;
        this.interviewQuestions = interviewQuestions;
    }
// ==========================================
    // GETTERS AND SETTERS
    // ==========================================


    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinExperienceYears() {
        return minExperienceYears;
    }

    public void setMinExperienceYears(int minExperienceYears) {
        this.minExperienceYears = minExperienceYears;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInterviewRigor() {
        return interviewRigor;
    }

    public void setInterviewRigor(String interviewRigor) {
        this.interviewRigor = interviewRigor;
    }

    public String getInterviewNotes() {
        return interviewNotes;
    }

    public void setInterviewNotes(String interviewNotes) {
        this.interviewNotes = interviewNotes;
    }

    public List<InterviewQuestion> getInterviewQuestions() {
        return interviewQuestions;
    }

    public void setInterviewQuestions(List<InterviewQuestion> interviewQuestions) {
        this.interviewQuestions = interviewQuestions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAutoGenerateScreening() {
        return autoGenerateScreening;
    }

    public void setAutoGenerateScreening(boolean autoGenerateScreening) {
        this.autoGenerateScreening = autoGenerateScreening;
    }
}

/*

Comparison: Setting Data With vs. Without Parameterized Constructors
Without Parameterized Constructors (Using Setters):

// Needs 10 lines of code
CreateJobPayload payload = new CreateJobPayload();
payload.setTitle("Senior Automation Engineer");
payload.setDescription("Leading QA efforts");
payload.setMinExperienceYears(5);
payload.setDepartment("Engineering");
payload.setInterviewRigor("High");
payload.setInterviewNotes("Focus on Java API Frameworks");
payload.setStatus("active");
payload.setAutoGenerateScreening(true);



With Parameterized Constructor:

CreateJobPayload payload = new CreateJobPayload(
    "Senior Automation Engineer", "Leading QA efforts", 5,
    "Engineering", "High", "Focus on Java API Frameworks",
    "active", true, questionsList
);
 */