package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("张三");
        testUser.setEmail("zhangsan@example.com");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        // Given
        List<User> userList = Arrays.asList(testUser);
        when(userMapper.findAll()).thenReturn(userList);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("张三", result.get(0).getName());
        verify(userMapper, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userMapper.findById(1L)).thenReturn(testUser);

        // When
        Optional<User> result = userService.getUserById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("张三", result.get().getName());
        verify(userMapper, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturnEmpty() {
        // Given
        when(userMapper.findById(999L)).thenReturn(null);

        // When
        Optional<User> result = userService.getUserById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(userMapper, times(1)).findById(999L);
    }

    @Test
    void createUser_WhenEmailNotExists_ShouldCreateUser() {
        // Given
        User newUser = new User();
        newUser.setName("李四");
        newUser.setEmail("lisi@example.com");

        when(userMapper.countByEmail(anyString())).thenReturn(0);
        when(userMapper.insert(any(User.class))).thenReturn(1);

        // When
        User result = userService.createUser(newUser);

        // Then
        assertNotNull(result);
        assertEquals("李四", result.getName());
        assertEquals("lisi@example.com", result.getEmail());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(userMapper, times(1)).countByEmail("lisi@example.com");
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    void createUser_WhenEmailExists_ShouldThrowException() {
        // Given
        User newUser = new User();
        newUser.setName("李四");
        newUser.setEmail("existing@example.com");

        when(userMapper.countByEmail("existing@example.com")).thenReturn(1);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(newUser);
        });

        assertEquals("Email already exists: existing@example.com", exception.getMessage());
        verify(userMapper, times(1)).countByEmail("existing@example.com");
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateUser() {
        // Given
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("张三");
        existingUser.setEmail("zhangsan@example.com");

        User updateData = new User();
        updateData.setName("张三更新");
        updateData.setEmail("zhangsan.updated@example.com");

        when(userMapper.findById(1L)).thenReturn(existingUser);
        when(userMapper.countByEmail("zhangsan.updated@example.com")).thenReturn(0);
        when(userMapper.update(any(User.class))).thenReturn(1);

        // When
        User result = userService.updateUser(1L, updateData);

        // Then
        assertNotNull(result);
        assertEquals("张三更新", result.getName());
        assertEquals("zhangsan.updated@example.com", result.getEmail());
        assertNotNull(result.getUpdatedAt());
        verify(userMapper, times(1)).findById(1L);
        verify(userMapper, times(1)).update(any(User.class));
    }

    @Test
    void updateUser_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userMapper.findById(999L)).thenReturn(null);

        User updateData = new User();
        updateData.setName("不存在的用户");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(999L, updateData);
        });

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userMapper, times(1)).findById(999L);
        verify(userMapper, never()).update(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Given
        when(userMapper.countById(1L)).thenReturn(1);
        when(userMapper.deleteById(1L)).thenReturn(1);

        // When & Then
        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userMapper, times(1)).countById(1L);
        verify(userMapper, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userMapper.countById(999L)).thenReturn(0);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(999L);
        });

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userMapper, times(1)).countById(999L);
        verify(userMapper, never()).deleteById(anyLong());
    }
}