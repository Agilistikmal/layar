package zip.agil.layar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import zip.agil.layar.entity.Movie;
import zip.agil.layar.entity.MovieBanner;
import zip.agil.layar.model.*;
import zip.agil.layar.service.MovieBannerService;
import zip.agil.layar.service.MovieService;

import java.util.List;

@RestController
@RequestMapping("/movie/{slug}/banner")
public class MovieBannerController {

    @Autowired
    private MovieBannerService movieBannerService;

    @Autowired
    private MovieService movieService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<MovieBanner>>> findMany() {
        List<MovieBanner> movieBanners = movieBannerService.findAll();

        WebResponse<List<MovieBanner>> response = WebResponse.<List<MovieBanner>>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(movieBanners)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<MovieBanner>> findById(@PathVariable(name = "id") String id) {
        WebResponse<MovieBanner> response = WebResponse.<MovieBanner>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(movieBannerService.findById(id))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WebResponse<MovieBanner>> create(@PathVariable("slug") String slug, @RequestParam("file") MultipartFile file) {
        try {
            Movie movie = movieService.findBySlug(slug);

            CreateMovieBannerRequest request = CreateMovieBannerRequest.builder()
                    .name(file.getOriginalFilename())
                    .url("")
                    .build();

            WebResponse<MovieBanner> response = WebResponse.<MovieBanner>builder()
                    .status(HttpStatus.OK.value())
                    .message("Movie created successfully")
                    .data(movieBannerService.create(movie, request))
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<MovieBanner>> update(@PathVariable(name = "id") String id, @RequestBody UpdateMovieBannerRequest request) {
        WebResponse<MovieBanner> response = WebResponse.<MovieBanner>builder()
                .status(HttpStatus.OK.value())
                .message("Movie updated successfully")
                .data(movieBannerService.update(id, request))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WebResponse<MovieBanner>> change(@PathVariable(name = "id") String id, @RequestParam("file") MultipartFile file) {

        MovieBanner movieBanner = movieBannerService.findById(id);

        UpdateMovieBannerRequest request = UpdateMovieBannerRequest.builder()
                .name(movieBanner.getName())
                .url("")
                .build();

        WebResponse<MovieBanner> response = WebResponse.<MovieBanner>builder()
                .status(HttpStatus.OK.value())
                .message("Movie updated successfully")
                .data(movieBannerService.update(id, request))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<WebResponse<MovieBanner>> delete(@PathVariable(name = "id") String id) {

        WebResponse<MovieBanner> response = WebResponse.<MovieBanner>builder()
                .status(HttpStatus.OK.value())
                .message("Movie deleted successfully")
                .data(movieBannerService.delete(id))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
