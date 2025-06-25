package com.example.cinema.mapper;

import com.example.cinema.dto.actor.ActorRequest;
import com.example.cinema.dto.actor.ActorResponse;
import com.example.cinema.dto.actor.ActorSummary;
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
public interface ActorMapper {

  @Mapping(target = "movieSummaries", source = "movies")
  ActorResponse toActorResponse(Actor actor);

  @Mapping(target = "movies", ignore = true)
  Actor toEntity(ActorRequest dto);

  @Named("toMovieSummary")
  default MovieSummary toMovieSummary(Movie movie) {
    return new MovieSummary(movie.getId(), movie.getName());
  }

  @IterableMapping(qualifiedByName = "toMovieSummary")
  List<MovieSummary> toMovieSummaries(List<Movie> movies);

  default ActorSummary toActorSummary(Actor actor) {
    return new ActorSummary(actor.getId(), actor.getDescription());
  }
}
