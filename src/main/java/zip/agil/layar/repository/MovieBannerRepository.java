package zip.agil.layar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zip.agil.layar.entity.MovieBanner;

@Repository
public interface MovieBannerRepository extends JpaRepository<MovieBanner, String> {
}
