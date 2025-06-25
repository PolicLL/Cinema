package com.example.cinema.mapper;

import com.example.cinema.dto.MovieDto;
import com.example.cinema.model.Actor;
import com.example.cinema.model.Movie;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MovieMapper {

  MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

  MovieDto toDto(Movie movie);

  @Mapping(target = "actors", ignore = true)
  Movie toEntity(MovieDto dto);

  default List<String> mapActorsToIds(List<Actor> actors) {
    return actors.stream().map(Actor::getId).toList();
  }
}