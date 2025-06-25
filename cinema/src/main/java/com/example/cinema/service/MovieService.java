package com.example.cinema.service;

import com.example.cinema.dto.MovieDto;
import com.example.cinema.mapper.MovieMapper;
import com.example.cinema.model.Movie;
import com.example.cinema.repository.MovieRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public List<MovieDto> findAll() {
    return movieRepository.findAll().stream()
        .map(movieMapper::toDto)
        .collect(Collectors.toList());
  }

  public MovieDto findById(String id) {
    return movieMapper.toDto(movieRepository.findById(id).orElseThrow());
  }

  public MovieDto save(MovieDto dto) {
    Movie movie = movieMapper.toEntity(dto);
    return movieMapper.toDto(movieRepository.save(movie));
  }

  public MovieDto update(String id, MovieDto dto) {
    Movie existing = movieRepository.findById(id).orElseThrow();
    existing.setName(dto.name());
    existing.setActors(movieMapper.toEntity(dto).getActors());
    return movieMapper.toDto(movieRepository.save(existing));
  }

  public void delete(String id) {
    movieRepository.deleteById(id);
  }
}
