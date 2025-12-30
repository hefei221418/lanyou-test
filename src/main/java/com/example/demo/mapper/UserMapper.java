package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    
    List<User> findAll();
    
    User findById(Long id);
    
    User findByEmail(String email);
    
    List<User> findByNameContaining(String name);
    
    int insert(User user);
    
    int update(User user);
    
    int deleteById(Long id);
    
    int countByEmail(String email);
    
    int countById(Long id);
}