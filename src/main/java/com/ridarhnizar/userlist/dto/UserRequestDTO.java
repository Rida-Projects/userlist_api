package com.ridarhnizar.userlist.dto;

public class UserRequestDTO {
    private int page = 0;
    private int size = 50;
    
    public UserRequestDTO() {}
    
    public UserRequestDTO(int page, int size) {
        this.page = page;
        this.size = size;
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
    public int getValidatedPage() {
        return Math.max(0, page);
    }
    
    public int getValidatedSize() {
        if (size <= 0) return 50;
        if (size > 50000) return 1000;
        return size;
    }
}
