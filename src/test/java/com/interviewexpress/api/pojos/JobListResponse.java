package com.interviewexpress.api.pojos;

import java.util.List;

public class JobListResponse {
    private int total;
    private int page;
    private int pageSize;
    private int totalPages;
    private List<JobResponse> items; // Renamed variable from getItems to items

    public JobListResponse() {
    }

    public JobListResponse(int total, int page, int pageSize, int totalPages, List<JobResponse> items) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    // Correct getter name for RestAssured/Jackson serialization & assertions
    public List<JobResponse> getItems() {
        return items;
    }

    public void setItems(List<JobResponse> items) {
        this.items = items;
    }
}