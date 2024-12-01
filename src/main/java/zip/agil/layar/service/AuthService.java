package zip.agil.layar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import zip.agil.layar.entity.User;
import zip.agil.layar.model.AuthUserResponse;
import zip.agil.layar.model.LoginUserRequest;
import zip.agil.layar.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    public AuthUserResponse authenticate(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            AuthUserResponse authUserResponse = new AuthUserResponse();

            authUserResponse.setAccessToken("");
            authUserResponse.setRefreshToken("");
            authUserResponse.setAccessTokenExpiredAt(System.currentTimeMillis());
            authUserResponse.setRefreshTokenExpiredAt(System.currentTimeMillis());
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

}
