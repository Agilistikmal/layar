package zip.agil.layar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zip.agil.layar.model.AuthUserResponse;
import zip.agil.layar.model.LoginUserRequest;
import zip.agil.layar.model.RegisterUserRequest;
import zip.agil.layar.model.WebResponse;
import zip.agil.layar.service.AuthService;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<AuthUserResponse>> authenticate(@RequestBody LoginUserRequest request) {
        WebResponse<AuthUserResponse> response = WebResponse.<AuthUserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("User authenticated successfully")
                .data(authService.authenticate(request))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<AuthUserResponse>> register(@RequestBody RegisterUserRequest request) {
        WebResponse<AuthUserResponse> response = WebResponse.<AuthUserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("User registered successfully")
                .data(authService.register(request))
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
