package zip.agil.layar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import zip.agil.layar.entity.User;
import zip.agil.layar.model.UserResponse;
import zip.agil.layar.model.WebResponse;
import zip.agil.layar.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "")
    public WebResponse<List<User>> findAll() {
        List<User> users = userService.findAll();

        return WebResponse.<List<User>>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(users)
                .build();
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "/current")
    public WebResponse<UserResponse> findByUsername(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .roles(user.getRoles())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return WebResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(userResponse)
                .build();
    }
}
