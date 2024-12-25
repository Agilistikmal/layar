package zip.agil.layar.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import zip.agil.layar.entity.Movie;
import zip.agil.layar.entity.MovieBanner;
import zip.agil.layar.model.MovieBannerResponse;
import zip.agil.layar.model.UpdateMovieBannerRequest;
import zip.agil.layar.repository.MovieBannerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieBannerService {

    @Autowired
    private MovieBannerRepository movieBannerRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private StorageService storageService;

    public List<MovieBannerResponse> findAll() {
        List<MovieBannerResponse> movieBannerResponses = new ArrayList<>();

        for (MovieBanner movieBanner : movieBannerRepository.findAll()) {
            movieBannerResponses.add(movieBanner.toResponse());
        }

        return movieBannerResponses;
    }

    public MovieBannerResponse findById(String id) {
        return movieBannerRepository.findById(id).orElseThrow(EntityNotFoundException::new).toResponse();
    }

    public ResponseInputStream<GetObjectResponse> getSource(String id) {
        MovieBannerResponse movieBanner = findById(id);
        return storageService.download(movieBanner.getName());
    }

    @Transactional
    public MovieBannerResponse create(Movie movie, MultipartFile multipartFile) {

        String fileName = storageService.upload(multipartFile);

        MovieBanner movieBanner = MovieBanner.builder()
                .name(fileName)
                .movie(movie)
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        return movieBannerRepository.save(movieBanner).toResponse();
    }

    @Transactional
    public MovieBannerResponse update(String id, UpdateMovieBannerRequest request) {
        validationService.validate(request);

        MovieBanner movieBanner = movieBannerRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        movieBanner.setName(request.getName());
        movieBanner.setUrl(request.getUrl());
        movieBanner.setUpdatedAt(System.currentTimeMillis());

        return movieBannerRepository.save(movieBanner).toResponse();
    }

    @Transactional
    public MovieBannerResponse delete(String id) {
        MovieBanner movieBanner = movieBannerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        movieBannerRepository.delete(movieBanner);
        return movieBanner.toResponse();
    }
}
