package com.ridarhnizar.userlist.services;

import com.ridarhnizar.userlist.dto.UserRequestDTO;
import com.ridarhnizar.userlist.dto.UserResponseDTO;
import com.ridarhnizar.userlist.dto.SearchRequestDTO;
import com.ridarhnizar.userlist.dto.AlphabetResponseDTO;
import com.ridarhnizar.userlist.dto.AllUsersResponseDTO;
import com.ridarhnizar.userlist.models.AlphabetInfo;
import com.ridarhnizar.userlist.models.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    
    private static final String USERNAMES_FILE = "usernames.txt";
    private int totalUserCount = 0;
    private Map<Character, AlphabetInfo> alphabetIndex = new ConcurrentHashMap<>();
    private List<String> allUsernames = new ArrayList<>();
    
    @PostConstruct
    public void initializeUserData() {
        try {
            loadUserData();
            buildAlphabetIndex();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize user data", e);
        }
    }
    
    private void loadUserData() throws IOException {
        ClassPathResource resource = new ClassPathResource(USERNAMES_FILE);
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    allUsernames.add(line.trim());
                }
            }
        }
        
        totalUserCount = allUsernames.size();
    }
    
    private void buildAlphabetIndex() {
        char currentLetter = '\0';
        int startIndex = 0;
        int count = 0;
        
        for (int i = 0; i < allUsernames.size(); i++) {
            String username = allUsernames.get(i);
            if (username.isEmpty()) continue;
            
            char firstLetter = Character.toUpperCase(username.charAt(0));
            
            if (firstLetter != currentLetter) {
                // Save previous letter info if exists
                if (currentLetter != '\0') {
                    alphabetIndex.put(currentLetter, new AlphabetInfo(currentLetter, count, startIndex, i - 1));
                }
                
                // Start new letter
                currentLetter = firstLetter;
                startIndex = i;
                count = 1;
            } else {
                count++;
            }
        }
        
        // Don't forget the last letter
        if (currentLetter != '\0') {
            alphabetIndex.put(currentLetter, new AlphabetInfo(currentLetter, count, startIndex, allUsernames.size() - 1));
        }
    }
    
    public UserResponseDTO getUsers(UserRequestDTO request) {
        int page = request.getValidatedPage();
        int pageSize = request.getValidatedSize();
        
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalUserCount);
        
        if (startIndex >= totalUserCount) {
            return new UserResponseDTO(new ArrayList<>(), totalUserCount, page, pageSize);
        }
        
        List<User> users = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            users.add(new User(allUsernames.get(i), i));
        }
        
        return new UserResponseDTO(users, totalUserCount, page, pageSize);
    }
    
    // Overloaded method for backward compatibility
    public UserResponseDTO getUsers(int page, int pageSize) {
        return getUsers(new UserRequestDTO(page, pageSize));
    }
    
    /**
     * Get ALL users without pagination
     * Returns the complete list of all users
     */
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        for (int i = 0; i < allUsernames.size(); i++) {
            allUsers.add(new User(allUsernames.get(i), i));
        }
        return allUsers;
    }
    
    /**
     * Get ALL users with count in response DTO
     * Returns the complete list of all users with total count
     */
    public AllUsersResponseDTO getAllUsersWithCount() {
        List<User> allUsers = getAllUsers();
        return new AllUsersResponseDTO(allUsers, totalUserCount);
    }
    
    public UserResponseDTO getUsersByLetter(char letter, UserRequestDTO request) {
        letter = Character.toUpperCase(letter);
        AlphabetInfo info = alphabetIndex.get(letter);
        
        if (info == null) {
            return new UserResponseDTO(new ArrayList<>(), 0, request.getValidatedPage(), request.getValidatedSize());
        }
        
        int page = request.getValidatedPage();
        int pageSize = request.getValidatedSize();
        
        int startIndex = info.getStartIndex() + (page * pageSize);
        int endIndex = Math.min(startIndex + pageSize, info.getEndIndex() + 1);
        
        if (startIndex > info.getEndIndex()) {
            return new UserResponseDTO(new ArrayList<>(), info.getCount(), page, pageSize);
        }
        
        List<User> users = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            users.add(new User(allUsernames.get(i), i));
        }
        
        int totalPages = (int) Math.ceil((double) info.getCount() / pageSize);
        UserResponseDTO response = new UserResponseDTO(users, info.getCount(), page, pageSize);
        response.setTotalPages(totalPages);
        response.setHasNext(page < totalPages - 1);
        response.setHasPrevious(page > 0);
        
        return response;
    }
    
    // Overloaded method for backward compatibility
    public UserResponseDTO getUsersByLetter(char letter, int page, int pageSize) {
        return getUsersByLetter(letter, new UserRequestDTO(page, pageSize));
    }
    
    public UserResponseDTO searchUsers(SearchRequestDTO request) {
        String query = request.getValidatedQuery();
        if (query.isEmpty()) {
            return getUsers(new UserRequestDTO(request.getValidatedPage(), request.getValidatedSize()));
        }
        
        query = query.toLowerCase();
        int page = request.getValidatedPage();
        int pageSize = request.getValidatedSize();
        
        List<User> matchingUsers = new ArrayList<>();
        
        // Simple linear search - for better performance with 10M records, 
        // consider implementing binary search or using a search index
        for (int i = 0; i < allUsernames.size(); i++) {
            String username = allUsernames.get(i).toLowerCase();
            if (username.contains(query)) {
                matchingUsers.add(new User(allUsernames.get(i), i));
            }
        }
        
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, matchingUsers.size());
        
        if (startIndex >= matchingUsers.size()) {
            return new UserResponseDTO(new ArrayList<>(), matchingUsers.size(), page, pageSize);
        }
        
        List<User> pageUsers = matchingUsers.subList(startIndex, endIndex);
        return new UserResponseDTO(pageUsers, matchingUsers.size(), page, pageSize);
    }
    
    // Overloaded method for backward compatibility
    public UserResponseDTO searchUsers(String query, int page, int pageSize) {
        return searchUsers(new SearchRequestDTO(query, page, pageSize));
    }
    
    public AlphabetResponseDTO getAlphabetInfo() {
        List<AlphabetInfo> result = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            AlphabetInfo info = alphabetIndex.get(c);
            if (info != null) {
                result.add(info);
            }
        }
        return new AlphabetResponseDTO(result, totalUserCount);
    }
    
    // Overloaded method for backward compatibility
    public List<AlphabetInfo> getAlphabetInfoList() {
        List<AlphabetInfo> result = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            AlphabetInfo info = alphabetIndex.get(c);
            if (info != null) {
                result.add(info);
            }
        }
        return result;
    }
    
    public int getTotalUserCount() {
        return totalUserCount;
    }
    
    public AlphabetInfo getAlphabetInfo(char letter) {
        return alphabetIndex.get(Character.toUpperCase(letter));
    }
}
