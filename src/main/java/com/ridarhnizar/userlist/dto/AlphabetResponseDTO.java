package com.ridarhnizar.userlist.dto;

import com.ridarhnizar.userlist.models.AlphabetInfo;

import java.util.List;

public class AlphabetResponseDTO {
    private List<AlphabetInfo> alphabetInfo;
    private int totalLetters;
    private int totalCount;
    
    public AlphabetResponseDTO() {}
    
    public AlphabetResponseDTO(List<AlphabetInfo> alphabetInfo) {
        this.alphabetInfo = alphabetInfo;
        this.totalLetters = alphabetInfo != null ? alphabetInfo.size() : 0;
        this.totalCount = calculateTotalCount(alphabetInfo);
    }
    
    public AlphabetResponseDTO(List<AlphabetInfo> alphabetInfo, int totalCount) {
        this.alphabetInfo = alphabetInfo;
        this.totalLetters = alphabetInfo != null ? alphabetInfo.size() : 0;
        this.totalCount = totalCount;
    }
    
    public List<AlphabetInfo> getAlphabetInfo() {
        return alphabetInfo;
    }
    
    public void setAlphabetInfo(List<AlphabetInfo> alphabetInfo) {
        this.alphabetInfo = alphabetInfo;
        this.totalLetters = alphabetInfo != null ? alphabetInfo.size() : 0;
    }
    
    public int getTotalLetters() {
        return totalLetters;
    }
    
    public void setTotalLetters(int totalLetters) {
        this.totalLetters = totalLetters;
    }
    
    public int getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    
    private int calculateTotalCount(List<AlphabetInfo> alphabetInfo) {
        if (alphabetInfo == null) {
            return 0;
        }
        return alphabetInfo.stream()
                .mapToInt(AlphabetInfo::getCount)
                .sum();
    }
}
