package zip.agil.layar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zip.agil.layar.model.RegisterUserRequest;
import zip.agil.layar.model.WebResponse;
import zip.agil.layar.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        userService.register(request);

        return WebResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("User registered successfully")
                .build();
    }
}
