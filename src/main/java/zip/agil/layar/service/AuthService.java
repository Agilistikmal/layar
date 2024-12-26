package zip.agil.layar.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import zip.agil.layar.entity.User;
import zip.agil.layar.enumerate.UserRole;
import zip.agil.layar.model.AuthUserResponse;
import zip.agil.layar.model.LoginUserRequest;
import zip.agil.layar.model.RegisterUserRequest;
import zip.agil.layar.model.VerifyTokenRequest;
import zip.agil.layar.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthUserResponse authenticate(LoginUserRequest request) {
        validationService.validate(request);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthUserResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiredAt(jwtService.extractExpiration(accessToken).getTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiredAt(jwtService.extractExpiration(refreshToken).getTime())
                .build();
    }

    @Transactional
    public AuthUserResponse register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Username %s already exists", request.getUsername()));
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .roles(UserRole.USER)
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthUserResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiredAt(jwtService.extractExpiration(accessToken).getTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiredAt(jwtService.extractExpiration(refreshToken).getTime())
                .build();
    }

    public boolean verifyToken(VerifyTokenRequest request) {
        validationService.validate(request);

        boolean isAccessTokenValid = jwtService.validateToken(request.getAccessToken());
        boolean isRefreshTokenValid = jwtService.validateToken(request.getRefreshToken());

        return isAccessTokenValid && isRefreshTokenValid;
    }
}
