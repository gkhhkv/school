package com.example.ecommerce.controller;

import com.example.ecommerce.common.ResultCode;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.exception.GlobalExceptionHandler;
import com.example.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private MockMvc mockMvc;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        AuthController controller = new AuthController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void should_returnToken_when_loginSuccessfully() throws Exception {
        String json = """
                {"username":"testuser","password":"password"}""";
        when(userService.login(any())).thenReturn(
                Map.of("token", "fake-jwt-token", "userId", 1L, "username", "testuser"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    void should_return400_when_usernameMissing() throws Exception {
        String json = """
                {"password":"password"}""";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message", containsString("用户名")));
    }

    @Test
    void should_return400_when_passwordMissing() throws Exception {
        String json = """
                {"username":"testuser"}""";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message", containsString("密码")));
    }

    @Test
    void should_returnLoginFailed_when_wrongCredentials() throws Exception {
        String json = """
                {"username":"testuser","password":"wrong"}""";
        when(userService.login(any())).thenThrow(
                new BusinessException(ResultCode.LOGIN_FAILED));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.LOGIN_FAILED.getCode()));
    }
}
