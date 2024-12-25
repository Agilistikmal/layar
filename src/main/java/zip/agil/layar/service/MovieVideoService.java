package zip.agil.layar.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zip.agil.layar.entity.Movie;
import zip.agil.layar.entity.MovieBanner;
import zip.agil.layar.entity.MovieVideo;
import zip.agil.layar.model.UpdateMovieVideoRequest;
import zip.agil.layar.repository.MovieVideoRepository;

import java.util.List;

@Service
public class MovieVideoService {

    @Autowired
    private MovieVideoRepository movieVideoRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private StorageService storageService;

    public List<MovieVideo> findAll() {
        return movieVideoRepository.findAll();
    }

    public MovieVideo findById(String id) {
        return movieVideoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public MovieVideo create(Movie movie, MultipartFile multipartFile) {

        String fileName = storageService.upload(multipartFile);

        MovieVideo movieVideo = MovieVideo.builder()
                .name(fileName)
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
