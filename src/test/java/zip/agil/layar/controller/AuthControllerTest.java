package zip.agil.layar.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zip.agil.layar.model.*;
import zip.agil.layar.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUser() throws Exception {

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();

        registerUserRequest.setUsername("agilistikmal");
        registerUserRequest.setPassword("test12345");
        registerUserRequest.setFullName("Agil Ghani Istikmal");

        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<AuthUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(HttpStatus.CREATED.value(), response.getStatus());
            assertEquals( "User registered successfully", response.getMessage());
        });
    }

    @Test
    void testRegisterUserBadRequest() throws Exception {

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();

        registerUserRequest.setUsername("AB");
        registerUserRequest.setPassword("test12345");

        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequest))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<AuthUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        });
    }

    @Test
    void testRegisterUserDuplicate() throws Exception {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("agilistikmal");
        registerUserRequest.setPassword("test12345");
        registerUserRequest.setFullName("Agil Ghani Istikmal");

        // Register first user
        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<AuthUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        });

        // Register second user
        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequest))
        ).andExpectAll(
                status().isConflict()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
            assertEquals("Username agilistikmal already exists", response.getMessage());
        });
    }

    @Test
    void testVerifyToken() throws Exception {

        RegisterUserRequest loginUserRequest = RegisterUserRequest.builder()
                .username("agilistikmal")
                .password("test12345")
                .fullName("Agil Ghani Istikmal")
                .build();

        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<AuthUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(HttpStatus.CREATED.value(), response.getStatus());

            VerifyTokenRequest verifyTokenRequest = VerifyTokenRequest.builder()
                    .accessToken(response.getData().getAccessToken())
                    .refreshToken(response.getData().getRefreshToken())
                    .build();

            mockMvc.perform(
                    post("/auth/verifyToken")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(verifyTokenRequest))
            ).andExpectAll(
                    status().isOk()
            ).andDo(resultVerifyToken -> {
                WebResponse<AuthUserResponse> responseVerifyToken = objectMapper.readValue(resultVerifyToken.getResponse().getContentAsString(), new TypeReference<>() {
                });

                assertEquals(HttpStatus.OK.value(), responseVerifyToken.getStatus());
            });
        });

    }
}