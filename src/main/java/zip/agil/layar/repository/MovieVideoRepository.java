package zip.agil.layar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zip.agil.layar.entity.MovieVideo;

@Repository
public interface MovieVideoRepository extends JpaRepository<MovieVideo, String> {
}
