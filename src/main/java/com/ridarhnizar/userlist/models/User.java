package com.ridarhnizar.userlist.models;

public class User {
    private String name;
    private int index;
    
    public User() {}
    
    public User(String name, int index) {
        this.name = name;
        this.index = index;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
}
