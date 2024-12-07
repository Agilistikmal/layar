package zip.agil.layar.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zip.agil.layar.entity.User;
import zip.agil.layar.model.UpdateUserRequest;
import zip.agil.layar.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public User update(String id, UpdateUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());

        return userRepository.save(user);
    }
}
