package zip.agil.layar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zip.agil.layar.entity.Movie;
import zip.agil.layar.entity.User;
import zip.agil.layar.model.CreateMovieRequest;
import zip.agil.layar.model.MovieResponse;
import zip.agil.layar.model.UpdateMovieRequest;
import zip.agil.layar.model.WebResponse;
import zip.agil.layar.service.MovieService;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<MovieResponse>>> findMany() {
        List<Movie> movies = movieService.findAll();

        List<MovieResponse> movieResponses = movies
                .stream()
                .map(Movie::toResponse).toList();

        WebResponse<List<MovieResponse>> response = WebResponse.<List<MovieResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(movieResponses)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping(path = "/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<MovieResponse>> findBySlug(@PathVariable(name = "slug") String slug) {
        WebResponse<MovieResponse> response = WebResponse.<MovieResponse>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(movieService.findBySlug(slug).toResponse())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<MovieResponse>> create(Authentication authentication, @RequestBody CreateMovieRequest request) {
        try {
            User user = (User) authentication.getPrincipal();

            WebResponse<MovieResponse> response = WebResponse.<MovieResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Movie created successfully")
                    .data(movieService.create(user.getId(), request))
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN_EDIT')")
    @PutMapping(path = "/{slug}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Movie>> update(@PathVariable(name = "slug") String slug, @RequestBody UpdateMovieRequest request) {
        WebResponse<Movie> response = WebResponse.<Movie>builder()
                .status(HttpStatus.OK.value())
                .message("Movie updated successfully")
                .data(movieService.update(slug, request))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN_DELETE')")
    @DeleteMapping(path = "/{slug}")
    public ResponseEntity<WebResponse<Movie>> delete(@PathVariable(name = "slug") String slug) {

        WebResponse<Movie> response = WebResponse.<Movie>builder()
                .status(HttpStatus.OK.value())
                .message("Movie deleted successfully")
                .data(movieService.delete(slug))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
