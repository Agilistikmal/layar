package zip.agil.layar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zip.agil.layar.entity.User;
import zip.agil.layar.model.UpdateUserRequest;
import zip.agil.layar.model.UserResponse;
import zip.agil.layar.model.WebResponse;
import zip.agil.layar.repository.UserRepository;
import zip.agil.layar.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "")
    public ResponseEntity<WebResponse<List<User>>> findAll() {
        List<User> users = userService.findAll();

        WebResponse<List<User>> response = WebResponse.<List<User>>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(users)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "/current")
    public ResponseEntity<WebResponse<UserResponse>> findByUsername(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());

        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(user.toResponse())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PatchMapping(path = "/current")
    public ResponseEntity<WebResponse<UserResponse>> updateCurrent(Authentication authentication, @RequestBody UpdateUserRequest request) {
        User user = userService.findByUsername(authentication.getName());

        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("User updated successfully")
                .data(userService.update(user.getId(), request).toResponse())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(path = "/current")
    public ResponseEntity<WebResponse<UserResponse>> deleteCurrent(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        userService.delete(user.getId());

        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("User deleted successfully")
                .data(user.toResponse())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
