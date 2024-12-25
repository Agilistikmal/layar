package zip.agil.layar.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import zip.agil.layar.entity.Movie;
import zip.agil.layar.entity.User;
import zip.agil.layar.model.*;
import zip.agil.layar.repository.MovieRepository;
import zip.agil.layar.repository.UserRepository;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieBannerService movieBannerService;

    @Autowired
    private MovieVideoService movieVideoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private SlugService slugService;

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Movie findBySlug(String slug) {
        return movieRepository.findBySlug(slug).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public MovieResponse create(String userId, CreateMovieRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        Movie movie = Movie.builder()
                .slug(request.getSlug())
                .title(request.getTitle())
                .description(request.getDescription())
                .uploader(user)
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        if (movie.getSlug() == null) {
            movie.setSlug(slugService.toSlug(movie.getTitle()));
        }

        if (movieRepository.existsBySlug(movie.getSlug())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Slug %s already exists", movie.getSlug()));
        }

        return movieRepository.save(movie).toResponse();
    }

    @Transactional
    public Movie update(String slug, UpdateMovieRequest request) {
        validationService.validate(request);

        Movie movie = movieRepository.findBySlug(slug).orElseThrow(EntityNotFoundException::new);

        movie.setSlug(request.getSlug());
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setUpdatedAt(System.currentTimeMillis());

        return movieRepository.save(movie);
    }

    @Transactional
    public Movie delete(String slug) {
        Movie movie = movieRepository.findBySlug(slug).orElseThrow(EntityNotFoundException::new);
        movieRepository.delete(movie);
        return movie;
    }
}
