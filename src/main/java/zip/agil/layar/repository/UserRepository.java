package zip.agil.layar.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zip.agil.layar.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(@NotBlank @Size(min = 3, max = 50) String username);

    Optional<User> findByUsername(@NotBlank @Size(min = 3, max = 50) String username);
}
