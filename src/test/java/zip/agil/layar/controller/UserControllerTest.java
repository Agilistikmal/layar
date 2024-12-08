package zip.agil.layar.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();

        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .username("agilistikmal")
                .password("test12345")
                .fullName("Agil Ghani Istikmal")
                .build();

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

            token = response.getData().getAccessToken();
        });
    }

    @Test
    void testFindAllUserUnauthorized() throws Exception {

        mockMvc.perform(
                get("/user")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        );
    }

    @Test
    void testFindAllUserForbidden() throws Exception {

        mockMvc.perform(
                get("/user")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden()
        );
    }

    @Test
    void testGetCurrentUser() throws Exception {

        mockMvc.perform(
                get("/user/current")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertEquals("agilistikmal", response.getData().getUsername());
        });
    }

    @Test
    void testUpdateUser() throws Exception {

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .username("ghani")
                .fullName("Ghani Only")
                .build();

        mockMvc.perform(
                put("/user/current")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertEquals("ghani", response.getData().getUsername());
            assertEquals("Ghani Only", response.getData().getFullName());
        });
    }

    @Test
    void testDeleteUser() throws Exception {

        mockMvc.perform(
                delete("/user/current")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertEquals("agilistikmal", response.getData().getUsername());
            assertEquals("Agil Ghani Istikmal", response.getData().getFullName());
        });
    }

}