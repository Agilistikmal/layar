package zip.agil.layar.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zip.agil.layar.entity.Movie;
import zip.agil.layar.entity.MovieBanner;
import zip.agil.layar.model.CreateMovieBannerRequest;
import zip.agil.layar.model.UpdateMovieBannerRequest;
import zip.agil.layar.repository.MovieBannerRepository;
import zip.agil.layar.repository.MovieRepository;

import java.util.List;

@Service
public class MovieBannerService {

    @Autowired
    private MovieBannerRepository movieBannerRepository;

    @Autowired
    private ValidationService validationService;

    public List<MovieBanner> findAll() {
        return movieBannerRepository.findAll();
    }

    public MovieBanner findById(String id) {
        return movieBannerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public MovieBanner create(Movie movie, CreateMovieBannerRequest request) {
        validationService.validate(request);

        MovieBanner movieBanner = MovieBanner.builder()
                .name(request.getName())
                .url(request.getUrl())
                .movie(movie)
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        return movieBannerRepository.save(movieBanner);
    }

    @Transactional
    public MovieBanner update(String id, UpdateMovieBannerRequest request) {
        validationService.validate(request);

        MovieBanner movieBanner = movieBannerRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        movieBanner.setName(request.getName());
        movieBanner.setUrl(request.getUrl());
        movieBanner.setUpdatedAt(System.currentTimeMillis());

        return movieBannerRepository.save(movieBanner);
    }

    @Transactional
    public MovieBanner delete(String id) {
        MovieBanner movieBanner = movieBannerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        movieBannerRepository.delete(movieBanner);
        return movieBanner;
    }
}
