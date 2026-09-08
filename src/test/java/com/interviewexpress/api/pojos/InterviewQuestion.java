package com.interviewexpress.api.pojos;

public class InterviewQuestion
{
    private String id;
    private String text;
    private String answerHint;
    // 1. Default No-Args Constructor (Required by Jackson for Deserialization)
    public InterviewQuestion()
    {

    }
    // 2. Parameterized Constructor
    public InterviewQuestion(String id,String text, String answerHint)
    {
        this.id=id;
        this.text=text;
        this.answerHint=answerHint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswerHint() {
        return answerHint;
    }

    public void setAnswerHint(String answerHint) {
        this.answerHint = answerHint;
    }
}
