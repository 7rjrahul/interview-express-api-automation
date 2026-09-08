package com.interviewexpress.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;


public class JobResponse {
    private String id;
    private String title;
    private String description;
    private Integer minExperienceYears;
    private String jdFileKey;
    private String jdFileUrl;
    private String status;
    private String department;
    private String interviewRigor;
    private Boolean criteriaStale;
    private String interviewNotes;
    private Integer inviteCount;
    private Integer completedInterviewCount;
    private String source;
    private String orgId;
    private String createdBy;
    private String createdByName;
    private String createdAt;
    private String updatedAt;

    private Map<String, Object> screeningCriteria;
    private List<InterviewQuestion> interviewQuestions;

    // Default No-Arg Constructor (Mandatory for Jackson deserialization)
    public JobResponse()
    {

    }
    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
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

    public Integer getMinExperienceYears() {
        return minExperienceYears;
    }

    public void setMinExperienceYears(Integer minExperienceYears) {
        this.minExperienceYears = minExperienceYears;
    }

    public String getJdFileKey() {
        return jdFileKey;
    }

    public void setJdFileKey(String jdFileKey) {
        this.jdFileKey = jdFileKey;
    }

    public String getJdFileUrl() {
        return jdFileUrl;
    }

    public void setJdFileUrl(String jdFileUrl) {
        this.jdFileUrl = jdFileUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Boolean getCriteriaStale() {
        return criteriaStale;
    }

    public void setCriteriaStale(Boolean criteriaStale) {
        this.criteriaStale = criteriaStale;
    }

    public String getInterviewNotes() {
        return interviewNotes;
    }

    public void setInterviewNotes(String interviewNotes) {
        this.interviewNotes = interviewNotes;
    }

    public Integer getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Integer inviteCount) {
        this.inviteCount = inviteCount;
    }

    public Integer getCompletedInterviewCount() {
        return completedInterviewCount;
    }

    public void setCompletedInterviewCount(Integer completedInterviewCount) {
        this.completedInterviewCount = completedInterviewCount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<String, Object> getScreeningCriteria() {
        return screeningCriteria;
    }

    public void setScreeningCriteria(Map<String, Object> screeningCriteria) {
        this.screeningCriteria = screeningCriteria;
    }

    public List<InterviewQuestion> getInterviewQuestions() {
        return interviewQuestions;
    }

    public void setInterviewQuestions(List<InterviewQuestion> interviewQuestions) {
        this.interviewQuestions = interviewQuestions;
    }
}
