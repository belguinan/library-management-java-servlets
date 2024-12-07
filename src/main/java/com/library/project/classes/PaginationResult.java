package com.library.project.classes;

import java.util.List;

public class PaginationResult<T> {
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int perPage;

    public PaginationResult(List<T> data, int currentPage, int perPage, long totalItems) {
        this.data = data;
        this.currentPage = currentPage;
        this.perPage = perPage;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / perPage);
    }

    public List<T> getData() {
        return this.data;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public long getTotalItems() {
        return this.totalItems;
    }

    public int getPerPage() {
        return this.perPage;
    }
}
