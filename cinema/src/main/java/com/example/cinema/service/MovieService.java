package com.example.cinema.service;

import static com.example.cinema.utils.SpecificationUtils.safeAnd;

import com.example.cinema.dto.movie.MovieFilter;
import com.example.cinema.dto.movie.MovieRequest;
import com.example.cinema.dto.movie.MovieResponse;
import com.example.cinema.exception.MovieNotFoundException;
import com.example.cinema.exception.MovieWithNameExistsException;
import com.example.cinema.mapper.MovieMapper;
import com.example.cinema.model.Movie;
import com.example.cinema.repository.ActorRepository;
import com.example.cinema.repository.MovieRepository;
import com.example.cinema.specification.MovieSpecification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

  private final MovieRepository movieRepository;
  private final ActorRepository actorRepository;
  private final MovieMapper movieMapper;

  public List<MovieResponse> findAll(MovieFilter filter) {
    log.info("Fetching all movies");

    Specification<Movie> spec = null;
    spec = safeAnd(spec, MovieSpecification.hasId(filter.id()));
    spec = safeAnd(spec, MovieSpecification.hasName(filter.name()));

    List<MovieResponse> movies = movieRepository.findAll(spec).stream()
        .map(movieMapper::toMovieResponse)
        .toList();

    log.info("Found {} movies", movies.size());
    return movies;
  }

  public MovieResponse findById(String id) {
    log.info("Fetching movie with id: {}", id);
    Optional<Movie> movie = movieRepository.findById(id);
    MovieResponse response = movieMapper.toMovieResponse(movie
        .orElseThrow(() -> new MovieNotFoundException(id)));
    log.info("Found movie: {}", response);
    return response;
  }

  public MovieResponse save(MovieRequest dto) {
    log.info("Saving new movie with data: {}", dto);

    if (movieRepository.existsByName(dto.name()))
      throw new MovieWithNameExistsException(dto.name());

    Movie movie = movieMapper.toEntity(dto);
    movie.setActors(actorRepository.findAllById(dto.actorIds()));
    movie.setId(UUID.randomUUID().toString());
    Movie savedMovie = movieRepository.save(movie);
    log.info("Saved movie with id: {}", savedMovie.getId());
    return movieMapper.toMovieResponse(savedMovie);
  }

  public MovieResponse update(String id, MovieRequest dto) {
    log.info("Updating movie with id: {} with data: {}", id, dto);
    Movie existing = movieRepository.findById(id)
        .orElseThrow(() -> new MovieNotFoundException(id));
    existing.setName(dto.name());
    existing.setActors(actorRepository.findAllById(dto.actorIds()));
    Movie updatedMovie = movieRepository.save(existing);
    log.info("Updated movie with id: {}", updatedMovie.getId());
    return movieMapper.toMovieResponse(updatedMovie);
  }

  public void delete(String id) {
    log.info("Deleting movie with id: {}", id);
    if (!movieRepository.existsById(id))
      throw new MovieNotFoundException(id);

    movieRepository.deleteById(id);
    log.info("Deleted movie with id: {}", id);
  }
}
