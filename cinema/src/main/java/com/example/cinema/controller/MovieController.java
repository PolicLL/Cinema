package com.example.cinema.controller;

import com.example.cinema.dto.movie.MovieFilter;
import com.example.cinema.dto.movie.MovieRequest;
import com.example.cinema.dto.movie.MovieResponse;
import com.example.cinema.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Movie", description = "CRUD operations for movies")
public class MovieController {

  private final MovieService movieService;

  @GetMapping
  @Operation(summary = "Get all movies",
      description = "Returns a list of all movies, optionally filtered by id and/or name.")
  @ApiResponse(responseCode = "200", description = "List of movies returned successfully")
  public ResponseEntity<List<MovieResponse>> getAll(
      @RequestParam(required = false) String id,
      @RequestParam(required = false) String name
  ) {
    log.info("Fetching all movies");
    MovieFilter filter = new MovieFilter(id, name);
    return ResponseEntity.ok(movieService.findAll(filter));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get movie by ID", description = "Returns movie details by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Movie found"),
      @ApiResponse(responseCode = "404", description = "Movie not found")
  })
  public ResponseEntity<MovieResponse> getById(@PathVariable String id) {
    log.info("Fetching movie with id: {}", id);
    return ResponseEntity.ok(movieService.findById(id));
  }

  @PostMapping
  @Operation(summary = "Create new movie", description = "Creates a new movie from request body")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Movie created successfully"),
      @ApiResponse(responseCode = "400", description = "Validation failed")
  })
  public ResponseEntity<MovieResponse> create(@Valid @RequestBody MovieRequest dto) {
    log.info("Creating movie with data: {}", dto);
    MovieResponse response = movieService.save(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update existing movie", description = "Updates a movie with given ID and request body")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Movie updated successfully"),
      @ApiResponse(responseCode = "404", description = "Movie not found"),
      @ApiResponse(responseCode = "400", description = "Validation failed")
  })
  public ResponseEntity<MovieResponse> update(@PathVariable String id, @Valid @RequestBody MovieRequest dto) {
    log.info("Updating movie with id: {}, data: {}", id, dto);
    return ResponseEntity.ok(movieService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete movie", description = "Deletes the movie with the given ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Movie deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Movie not found")
  })
  public ResponseEntity<Void> delete(@PathVariable String id) {
    log.info("Deleting movie with id: {}", id);
    movieService.delete(id);
    return ResponseEntity.noContent().build();
  }
}