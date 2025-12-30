package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

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
    void getAllUsers_WhenUsersExist_ShouldReturnUserList() {
        // Given
        List<User> userList = Arrays.asList(testUser);
        when(userService.getAllUsers()).thenReturn(userList);

        // When
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("张三", response.getBody().get(0).getName());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));

        // When
        ResponseEntity<User> response = userController.getUserById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("张三", response.getBody().getName());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturnNotFound() {
        // Given
        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<User> response = userController.getUserById(999L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).getUserById(999L);
    }

    @Test
    void getUserByEmail_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userService.getUserByEmail("zhangsan@example.com")).thenReturn(Optional.of(testUser));

        // When
        ResponseEntity<User> response = userController.getUserByEmail("zhangsan@example.com");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("zhangsan@example.com", response.getBody().getEmail());
        verify(userService, times(1)).getUserByEmail("zhangsan@example.com");
    }

    @Test
    void getUserByEmail_WhenUserNotExists_ShouldReturnNotFound() {
        // Given
        when(userService.getUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When
        ResponseEntity<User> response = userController.getUserByEmail("nonexistent@example.com");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).getUserByEmail("nonexistent@example.com");
    }

    @Test
    void getUsersByName_WhenUsersExist_ShouldReturnUserList() {
        // Given
        List<User> userList = Arrays.asList(testUser);
        when(userService.getUsersByNameContaining("张")).thenReturn(userList);

        // When
        ResponseEntity<List<User>> response = userController.getUsersByName("张");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getUsersByNameContaining("张");
    }

    @Test
    void createUser_WhenValidUser_ShouldReturnCreatedUser() {
        // Given
        User newUser = new User();
        newUser.setName("李四");
        newUser.setEmail("lisi@example.com");

        User createdUser = new User();
        createdUser.setId(2L);
        createdUser.setName("李四");
        createdUser.setEmail("lisi@example.com");

        when(userService.createUser(any(User.class))).thenReturn(createdUser);

        // When
        ResponseEntity<User> response = userController.createUser(newUser);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("李四", response.getBody().getName());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void createUser_WhenInvalidUser_ShouldReturnBadRequest() {
        // Given
        User newUser = new User();
        newUser.setName("李四");
        newUser.setEmail("existing@example.com");

        when(userService.createUser(any(User.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        // When
        ResponseEntity<User> response = userController.createUser(newUser);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void updateUser_WhenValidUpdate_ShouldReturnUpdatedUser() {
        // Given
        User updateData = new User();
        updateData.setName("张三更新");
        updateData.setEmail("zhangsan.updated@example.com");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("张三更新");
        updatedUser.setEmail("zhangsan.updated@example.com");

        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(updatedUser);

        // When
        ResponseEntity<User> response = userController.updateUser(1L, updateData);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("张三更新", response.getBody().getName());
        verify(userService, times(1)).updateUser(anyLong(), any(User.class));
    }

    @Test
    void updateUser_WhenUserNotExists_ShouldReturnNotFound() {
        // Given
        User updateData = new User();
        updateData.setName("不存在的用户");

        when(userService.updateUser(anyLong(), any(User.class)))
                .thenThrow(new RuntimeException("User not found"));

        // When
        ResponseEntity<User> response = userController.updateUser(999L, updateData);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).updateUser(anyLong(), any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnNoContent() {
        // Given
        doNothing().when(userService).deleteUser(1L);

        // When
        ResponseEntity<Void> response = userController.deleteUser(1L);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldReturnNotFound() {
        // Given
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser(999L);

        // When
        ResponseEntity<Void> response = userController.deleteUser(999L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).deleteUser(999L);
    }
}