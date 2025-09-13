package com.ridarhnizar.userlist.dto;

public class SearchRequestDTO {
    private String query;
    private int page = 0;
    private int size = 50;
    
    public SearchRequestDTO() {}
    
    public SearchRequestDTO(String query, int page, int size) {
        this.query = query;
        this.page = page;
        this.size = size;
    }
    
    public String getQuery() {
        return query;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    // Validation methods
    public String getValidatedQuery() {
        return query != null ? query.trim() : "";
    }
    
    public int getValidatedPage() {
        return Math.max(0, page);
    }
    
    public int getValidatedSize() {
        if (size <= 0) return 50;
        if (size > 1000) return 1000;
        return size;
    }
}
