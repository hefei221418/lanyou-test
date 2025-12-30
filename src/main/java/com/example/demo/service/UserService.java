package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserMapper userMapper;
    
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userMapper.findById(id));
    }
    
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userMapper.findByEmail(email));
    }
    
    public List<User> getUsersByNameContaining(String name) {
        return userMapper.findByNameContaining(name);
    }
    
    public User createUser(User user) {
        if (userMapper.countByEmail(user.getEmail()) > 0) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        
        userMapper.insert(user);
        return user;
    }
    
    public User updateUser(Long id, User userDetails) {
        User existingUser = userMapper.findById(id);
        if (existingUser == null) {
            throw new RuntimeException("User not found with id: " + id);
        }
        
        if (!existingUser.getEmail().equals(userDetails.getEmail()) && 
            userMapper.countByEmail(userDetails.getEmail()) > 0) {
            throw new RuntimeException("Email already exists: " + userDetails.getEmail());
        }
        
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setUpdatedAt(LocalDateTime.now());
        
        userMapper.update(existingUser);
        return existingUser;
    }
    
    public void deleteUser(Long id) {
        if (userMapper.countById(id) == 0) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userMapper.deleteById(id);
    }
}