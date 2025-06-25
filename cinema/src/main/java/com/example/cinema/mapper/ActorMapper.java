package com.example.cinema.mapper;

import com.example.cinema.dto.ActorDto;
import com.example.cinema.model.Actor;
import com.example.cinema.model.Movie;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ActorMapper {

  ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

  @Mapping(target = "movieIds", source = "movies")
  ActorDto toDto(Actor actor);

  @Mapping(target = "movies", source = "movieIds")
  Actor toEntity(ActorDto dto);

  default List<String> mapMoviesToIds(List<Movie> movies) {
    return movies.stream().map(Movie::getId).toList();
  }

  default List<Movie> mapIdsToMovies(List<String> ids) {
    return null;
  }
}
