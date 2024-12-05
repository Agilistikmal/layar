package zip.agil.layar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AuthUserResponse> authenticate(@RequestBody LoginUserRequest request) {
        WebResponse<AuthUserResponse> response = new WebResponse<>();
        response.setData(authService.authenticate(request));

        return response;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AuthUserResponse> register(@RequestBody RegisterUserRequest request) {
        WebResponse<AuthUserResponse> response = new WebResponse<>();
        response.setData(authService.register(request));

        return response;
    }
}
