package com.ridarhnizar.userlist.controllers;

import com.ridarhnizar.userlist.dto.UserRequestDTO;
import com.ridarhnizar.userlist.dto.UserResponseDTO;
import com.ridarhnizar.userlist.dto.SearchRequestDTO;
import com.ridarhnizar.userlist.dto.AlphabetResponseDTO;
import com.ridarhnizar.userlist.dto.AllUsersResponseDTO;
import com.ridarhnizar.userlist.models.AlphabetInfo;
import com.ridarhnizar.userlist.models.User;
import com.ridarhnizar.userlist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Allow CORS for frontend integration
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Get paginated list of all users
     * GET /api/users?page=0&size=50
     */
    @GetMapping
    public ResponseEntity<UserResponseDTO> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        UserRequestDTO request = new UserRequestDTO(page, size);
        UserResponseDTO response = userService.getUsers(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get ALL users without pagination
     * GET /api/users/all
     */
    @GetMapping("/all")
    public ResponseEntity<AllUsersResponseDTO> getAllUsers() {
        AllUsersResponseDTO response = userService.getAllUsersWithCount();
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get users by alphabet letter
     * GET /api/users/letter/A?page=0&size=50
     */
    @GetMapping("/letter/{letter}")
    public ResponseEntity<UserResponseDTO> getUsersByLetter(
            @PathVariable char letter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        UserRequestDTO request = new UserRequestDTO(page, size);
        UserResponseDTO response = userService.getUsersByLetter(letter, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search users by name
     * GET /api/users/search?q=john&page=0&size=50
     */
    @GetMapping("/search")
    public ResponseEntity<UserResponseDTO> searchUsers(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        SearchRequestDTO request = new SearchRequestDTO(q, page, size);
        UserResponseDTO response = userService.searchUsers(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get alphabet navigation information
     * GET /api/users/alphabet
     */
    @GetMapping("/alphabet")
    public ResponseEntity<AlphabetResponseDTO> getAlphabetInfo() {
        AlphabetResponseDTO alphabetInfo = userService.getAlphabetInfo();
        return ResponseEntity.ok(alphabetInfo);
    }
    
    /**
     * Get specific alphabet information
     * GET /api/users/alphabet/A
     */
    @GetMapping("/alphabet/{letter}")
    public ResponseEntity<AlphabetInfo> getAlphabetInfo(@PathVariable char letter) {
        AlphabetInfo info = userService.getAlphabetInfo(letter);
        if (info != null) {
            return ResponseEntity.ok(info);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get total user count
     * GET /api/users/count
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getUserCount() {
        return ResponseEntity.ok(userService.getTotalUserCount());
    }
    
    /**
     * Health check endpoint
     * GET /api/users/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User API is running. Total users: " + userService.getTotalUserCount());
    }
}
