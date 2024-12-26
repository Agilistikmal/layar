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
import zip.agil.layar.entity.User;
import zip.agil.layar.enumerate.UserRole;
import zip.agil.layar.model.*;
import zip.agil.layar.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieBannerRepository movieBannerRepository;

    @Autowired
    private MovieVideoRepository movieVideoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        movieRepository.deleteAll();
        movieBannerRepository.deleteAll();
        movieVideoRepository.deleteAll();

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

        // Set user to ADMIN
        User user = userRepository.findByUsername("agilistikmal").orElseThrow();
        user.setRoles(UserRole.USER);
        userRepository.save(user);

        LoginUserRequest loginUserRequest = LoginUserRequest.builder()
                .username("agilistikmal")
                .password("test12345")
                .build();

        mockMvc.perform(
                post("/auth/authenticate")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AuthUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(HttpStatus.OK.value(), response.getStatus());

            token = response.getData().getAccessToken();
        });
    }

    @Test
    void testCreateMovie() throws Exception {

        CreateMovieRequest createMovieRequest = CreateMovieRequest.builder()
                .title("Movie 1")
                .description("Movie 1 description")
                .build();

        mockMvc.perform(
                post("/movie")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(createMovieRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<MovieResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(HttpStatus.OK.value(), response.getStatus());
        });
    }

    @Test
    void testUpdateMovie() throws Exception {

        CreateMovieRequest createMovieRequest = CreateMovieRequest.builder()
                .title("Movie 1")
                .description("Movie 1 description")
                .build();

        mockMvc.perform(
                post("/movie")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(createMovieRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<MovieResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(HttpStatus.OK.value(), response.getStatus());

            UpdateMovieRequest updateMovieRequest = UpdateMovieRequest.builder()
                    .slug(response.getData().getSlug())
                    .title(response.getData().getTitle())
                    .description("Movie 1 description updated")
                    .build();

            mockMvc.perform(
                    put("/movie/" + response.getData().getSlug())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(objectMapper.writeValueAsString(updateMovieRequest))
            ).andExpectAll(
                    status().isOk()
            ).andDo(resultUpdate -> {
                WebResponse<MovieResponse> responseUpdate = objectMapper.readValue(resultUpdate.getResponse().getContentAsString(), new TypeReference<>() {
                });

                assertEquals(HttpStatus.OK.value(), responseUpdate.getStatus());
                assertEquals("Movie 1 description updated", responseUpdate.getData().getDescription());
            });
        });
    }

    @Test
    void testDeleteMovie() throws Exception {

        CreateMovieRequest createMovieRequest = CreateMovieRequest.builder()
                .title("Movie 1")
                .description("Movie 1 description")
                .build();

        mockMvc.perform(
                post("/movie")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(createMovieRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<MovieResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(HttpStatus.OK.value(), response.getStatus());

            mockMvc.perform(
                    delete("/movie/" + response.getData().getSlug())
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
            ).andExpectAll(
                    status().isOk()
            ).andDo(resultDelete -> {
                WebResponse<MovieResponse> responseDelete = objectMapper.readValue(resultDelete.getResponse().getContentAsString(), new TypeReference<>() {
                });

                assertEquals(HttpStatus.OK.value(), responseDelete.getStatus());
            });
        });
    }
}