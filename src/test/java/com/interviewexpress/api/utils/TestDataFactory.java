package com.interviewexpress.api.utils;

import com.interviewexpress.api.pojos.CreateJobPayload;
import com.interviewexpress.api.pojos.CreateJobRequest;
import com.interviewexpress.api.pojos.InterviewQuestion;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestDataFactory
{

    // 1. Valid Payload Creation (Your method!)
    public static CreateJobPayload createValidJobPayload()
    {
        InterviewQuestion q1 = new InterviewQuestion(
                "Q1",
                "Design the Rest Assured framework for POST API",
                "Config file, pojo class, client class"
        );

        List<InterviewQuestion> q2 = new ArrayList<>();   // Create list of InterviewQuestion objects
        q2.add(q1);                                     // Add q1 to the list

        // Create complete CreateJobPayload object
        CreateJobPayload p1 = new CreateJobPayload(
                "Test engineer",
                "Api automation",
                5,
                "IT",
                "technical",
                "High",
                "active",
                true,
                q2
        );
        return p1;
    }
// 2. Full Valid Request (Payload + File Attachment)

    public static CreateJobRequest createValidJobRequest()
    {
        CreateJobPayload payload = createValidJobPayload();
        File jobSpecFile = new File("src/test/resources/sample_jd.pdf");

        return new CreateJobRequest(payload, jobSpecFile);
    }

    // 3. Valid Request WITHOUT File Attachment (Payload Only)
    public static CreateJobRequest createValidJobRequestWithoutFile() {
        CreateJobPayload payload = createValidJobPayload();
        return new CreateJobRequest(payload, null); // Explicitly passing null for file
    }

    //4. Invalid Request
    // (case 1 title =null)
    public static CreateJobRequest createJobRequestNullTitle()
    {
        CreateJobPayload invalidPayload = new CreateJobPayload();
        invalidPayload.setTitle(null);

        CreateJobRequest request = new CreateJobRequest(invalidPayload,null);
        return request;

    }
    // case 2. Title is Empty String ("")
    public static CreateJobRequest createJobRequestEmptyTitle()
    {
        CreateJobPayload emptyTitle = new CreateJobPayload();
        emptyTitle.setTitle("");

        CreateJobRequest empty = new CreateJobRequest(emptyTitle,null);
        return empty;
    }

    //case 3: Title is Whitespace-Only ("   ")

    public static CreateJobRequest createJobRequestBlankWhitespaceTitle()
    {
        CreateJobPayload blankSpace = new CreateJobPayload();
        blankSpace.setTitle("      ");

        CreateJobRequest blankWhiteSpace = new CreateJobRequest(blankSpace, null);
        return blankWhiteSpace;
    }


}
