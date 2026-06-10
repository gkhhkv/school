package com.example.ecommerce.service;

import com.example.ecommerce.common.ResultCode;
import com.example.ecommerce.domain.User;
import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.dto.UserProfileUpdateRequest;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.repository.UserMapper;
import com.example.ecommerce.service.impl.UserServiceImpl;
import com.example.ecommerce.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void should_returnToken_when_credentialsAreCorrect() {
        User user = TestDataFactory.createTestUser(1L);
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser1");
        request.setPassword("password");

        when(userMapper.selectOne(any())).thenReturn(user);

        Map<String, Object> result = userService.login(request);

        assertNotNull(result);
        assertNotNull(result.get("token"));
        assertEquals(1L, result.get("userId"));
        assertEquals("testuser1", result.get("username"));
    }

    @Test
    void should_throwLoginFailed_when_userNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("password");

        when(userMapper.selectOne(any())).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> userService.login(request));
        assertEquals(ResultCode.LOGIN_FAILED.getCode(), ex.getCode());
    }

    @Test
    void should_throwLoginFailed_when_passwordIncorrect() {
        User user = TestDataFactory.createTestUser(1L);
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser1");
        request.setPassword("wrongpassword");

        when(userMapper.selectOne(any())).thenReturn(user);

        BusinessException ex = assertThrows(BusinessException.class, () -> userService.login(request));
        assertEquals(ResultCode.LOGIN_FAILED.getCode(), ex.getCode());
    }

    @Test
    void should_returnToken_when_registerSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("pass1234");

        when(userMapper.exists(any())).thenReturn(false);
        when(userMapper.insert(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setUserId(10L);
            return 1;
        });

        Map<String, Object> result = userService.register(request);

        assertNotNull(result);
        assertNotNull(result.get("token"));
        assertEquals(10L, result.get("userId"));
        assertEquals("newuser", result.get("username"));
    }

    @Test
    void should_throwUsernameExists_when_duplicateUsername() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existing");
        request.setPassword("pass1234");

        when(userMapper.exists(any())).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> userService.register(request));
        assertEquals(ResultCode.USERNAME_EXISTS.getCode(), ex.getCode());
    }

    @Test
    void should_returnUser_when_getProfile() {
        User user = TestDataFactory.createTestUser(1L);
        when(userMapper.selectById(1L)).thenReturn(user);

        User result = userService.getProfile(1L);

        assertEquals("testuser1", result.getUsername());
        assertEquals("13800138000", result.getPhone());
    }

    @Test
    void should_updateProfile_when_validRequest() {
        User user = TestDataFactory.createTestUser(1L);
        when(userMapper.selectById(1L)).thenReturn(user);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        UserProfileUpdateRequest request = new UserProfileUpdateRequest();
        request.setPhone("13900139000");
        request.setAddress("新地址");

        userService.updateProfile(1L, request);

        assertEquals("13900139000", user.getPhone());
        assertEquals("新地址", user.getAddress());
        verify(userMapper).updateById(user);
    }
}
