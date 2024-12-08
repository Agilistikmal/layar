package zip.agil.layar.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zip.agil.layar.entity.Movie;
import zip.agil.layar.entity.MovieVideo;
import zip.agil.layar.model.CreateMovieVideoRequest;
import zip.agil.layar.model.UpdateMovieVideoRequest;
import zip.agil.layar.repository.MovieVideoRepository;
import zip.agil.layar.repository.MovieRepository;

import java.util.List;

@Service
public class MovieVideoService {

    @Autowired
    private MovieVideoRepository movieVideoRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ValidationService validationService;

    public List<MovieVideo> findAll() {
        return movieVideoRepository.findAll();
    }

    public MovieVideo findById(String id) {
        return movieVideoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public MovieVideo create(CreateMovieVideoRequest request) {
        validationService.validate(request);

        Movie movie = movieRepository.findById(request.getMovieId()).orElseThrow(EntityNotFoundException::new);

        MovieVideo movieVideo = MovieVideo.builder()
                .name(request.getName())
                .url(request.getUrl())
                .quality(request.getQuality())
                .movie(movie)
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        return movieVideoRepository.save(movieVideo);
    }

    @Transactional
    public MovieVideo update(String id, UpdateMovieVideoRequest request) {
        validationService.validate(request);

        MovieVideo movieVideo = movieVideoRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        movieVideo.setName(request.getName());
        movieVideo.setUrl(request.getUrl());
        movieVideo.setUpdatedAt(System.currentTimeMillis());

        return movieVideoRepository.save(movieVideo);
    }

    @Transactional
    public MovieVideo delete(String id) {
        MovieVideo movieVideo = movieVideoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        movieVideoRepository.delete(movieVideo);
        return movieVideo;
    }
}
