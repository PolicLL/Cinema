package com.example.cinema.mapper;

import com.example.cinema.dto.actor.ActorSummary;
import com.example.cinema.dto.movie.MovieRequest;
import com.example.cinema.dto.movie.MovieResponse;
import com.example.cinema.dto.movie.MovieSummary;
import com.example.cinema.model.Actor;
import com.example.cinema.model.Movie;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MovieMapper {

  @Mapping(target = "actorSummaries", source = "actors")
  MovieResponse toMovieResponse(Movie movie);

  @Mapping(target = "actors", ignore = true)
  Movie toEntity(MovieRequest dto);

  @Named("toActorSummary")
  default ActorSummary toActorSummary(Actor actor) {
    return new ActorSummary(actor.getId(), actor.getDescription());
  }

  @IterableMapping(qualifiedByName = "toActorSummary")
  List<ActorSummary> toActorSummaries(List<Actor> actors);

  default MovieSummary toMovieSummary(Movie movie) {
    return new MovieSummary(movie.getId(), movie.getName());
  }
}