package zip.agil.layar.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zip.agil.layar.entity.User;
import zip.agil.layar.model.UpdateUserRequest;
import zip.agil.layar.model.UserResponse;
import zip.agil.layar.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    public List<UserResponse> findAll() {
        List<UserResponse> userResponses = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            userResponses.add(user.toResponse());
        }

        return userResponses;
    }

    public UserResponse findByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new).toResponse();
    }

    @Transactional
    public UserResponse update(String id, UpdateUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setUpdatedAt(System.currentTimeMillis());

        return userRepository.save(user).toResponse();
    }

    @Transactional
    public UserResponse delete(String id) {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);
        return user.toResponse();
    }
}
