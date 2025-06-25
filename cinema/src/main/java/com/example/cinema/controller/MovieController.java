package com.example.cinema.controller;

import com.example.cinema.dto.movie.MovieRequest;
import com.example.cinema.dto.movie.MovieResponse;
import com.example.cinema.service.MovieService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

  private final MovieService movieService;

  @GetMapping
  public ResponseEntity<List<MovieResponse>> getAll() {
    log.info("Fetching all movies");
    return ResponseEntity.ok(movieService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<MovieResponse> getById(@PathVariable String id) {
    log.info("Fetching movie with id: {}", id);
    return ResponseEntity.ok(movieService.findById(id));
  }

  @PostMapping
  public ResponseEntity<MovieResponse> create(@Valid @RequestBody MovieRequest dto) {
    log.info("Creating movie with data: {}", dto);
    MovieResponse response = movieService.save(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MovieResponse> update(@PathVariable String id, @Valid @RequestBody MovieRequest dto) {
    log.info("Updating movie with id: {}, data: {}", id, dto);
    return ResponseEntity.ok(movieService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    log.info("Deleting movie with id: {}", id);
    movieService.delete(id);
    return ResponseEntity.noContent().build();
  }
}