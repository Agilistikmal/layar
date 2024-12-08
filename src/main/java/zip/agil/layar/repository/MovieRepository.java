package zip.agil.layar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zip.agil.layar.entity.Movie;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

    boolean existsBySlug(String slug);

    Optional<Movie> findBySlug(String slug);
}
