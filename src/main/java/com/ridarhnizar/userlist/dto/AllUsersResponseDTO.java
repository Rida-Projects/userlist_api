package com.ridarhnizar.userlist.dto;

import com.ridarhnizar.userlist.models.User;

import java.util.List;

public class AllUsersResponseDTO {
    private List<User> users;
    private int totalCount;
    
    public AllUsersResponseDTO() {}
    
    public AllUsersResponseDTO(List<User> users, int totalCount) {
        this.users = users;
        this.totalCount = totalCount;
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public int getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
