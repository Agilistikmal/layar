package zip.agil.layar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import zip.agil.layar.entity.User;
import zip.agil.layar.enumerate.UserRole;
import zip.agil.layar.model.AuthUserResponse;
import zip.agil.layar.model.LoginUserRequest;
import zip.agil.layar.model.RegisterUserRequest;
import zip.agil.layar.repository.UserRepository;

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

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        String token = jwtService.generateToken(user);

        return AuthUserResponse.builder()
                .accessToken(token)
                .accessTokenExpiredAt(jwtService.extractExpiration(token).getTime())
                .build();
    }

    public AuthUserResponse register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username is already in use");
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

        String token = jwtService.generateToken(user);

        return AuthUserResponse.builder()
                .accessToken(token)
                .build();
    }
}
