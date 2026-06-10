package com.example.ecommerce.service;

import com.example.ecommerce.common.ResultCode;
import com.example.ecommerce.domain.User;
import com.example.ecommerce.dto.LoginRequest;
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
}
