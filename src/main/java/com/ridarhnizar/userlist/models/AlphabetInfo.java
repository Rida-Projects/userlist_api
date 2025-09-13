package com.ridarhnizar.userlist.models;

public class AlphabetInfo {
    private char letter;
    private int count;
    private int startIndex;
    private int endIndex;
    
    public AlphabetInfo() {}
    
    public AlphabetInfo(char letter, int count, int startIndex, int endIndex) {
        this.letter = letter;
        this.count = count;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
    
    // Getters and Setters
    public char getLetter() {
        return letter;
    }
    
    public void setLetter(char letter) {
        this.letter = letter;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public int getStartIndex() {
        return startIndex;
    }
    
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
    
    public int getEndIndex() {
        return endIndex;
    }
    
    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
