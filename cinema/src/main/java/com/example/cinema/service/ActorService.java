package com.example.cinema.service;

import static com.example.cinema.utils.SpecificationUtils.safeAnd;

import com.example.cinema.dto.actor.ActorFilter;
import com.example.cinema.dto.actor.ActorRequest;
import com.example.cinema.dto.actor.ActorResponse;
import com.example.cinema.exception.ActorNotFoundException;
import com.example.cinema.mapper.ActorMapper;
import com.example.cinema.model.Actor;
import com.example.cinema.model.Movie;
import com.example.cinema.repository.ActorRepository;
import com.example.cinema.repository.MovieRepository;
import com.example.cinema.specification.ActorSpecification;
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
public class ActorService {

  private final ActorRepository actorRepository;
  private final MovieRepository movieRepository;
  private final ActorMapper actorMapper;

  public List<ActorResponse> findAll(ActorFilter filter) {
    log.info("Fetching actors with filters: {}", filter);

    Specification<Actor> spec = null;
    spec = safeAnd(spec, ActorSpecification.hasId(filter.id()));
    spec = safeAnd(spec, ActorSpecification.hasDescription(filter.description()));

    List<ActorResponse> actors = actorRepository.findAll(spec).stream()
        .map(actorMapper::toActorResponse)
        .toList();

    log.info("Found {} actors", actors.size());
    return actors;
  }

  public ActorResponse findById(String id) {
    log.info("Fetching actor with id: {}", id);
    Optional<Actor> actor = actorRepository.findById(id);
    ActorResponse response = actorMapper.toActorResponse(actor
        .orElseThrow(() -> new ActorNotFoundException(id)));
    log.info("Found actor: {}", response);
    return response;
  }

  public ActorResponse save(ActorRequest dto) {
    log.info("Saving new actor with data: {}", dto);
    Actor actor = actorMapper.toEntity(dto);
    actor.setId(UUID.randomUUID().toString());

    Actor savedActor = actorRepository.save(actor); // Save first

    List<Movie> movies = movieRepository.findAllById(dto.movieIds());
    savedActor.setMovies(movies);

    for (Movie movie : movies) {
      movie.getActors().add(savedActor);
      movieRepository.save(movie);
    }

    log.info("Saved actor with id: {}", savedActor.getId());
    return actorMapper.toActorResponse(savedActor);
  }

  public ActorResponse update(String id, ActorRequest dto) {
    log.info("Updating actor with id: {} with data: {}", id, dto);
    Actor existing = actorRepository.findById(id)
        .orElseThrow(() -> new ActorNotFoundException(id));
    existing.setDescription(dto.description());
    existing.setMovies(movieRepository.findAllById(dto.movieIds()));
    Actor updatedActor = actorRepository.save(existing);
    log.info("Updated actor with id: {}", updatedActor.getId());
    return actorMapper.toActorResponse(updatedActor);
  }

  public void delete(String id) {
    log.info("Deleting actor with id: {}", id);
    if (!actorRepository.existsById(id))
      throw new ActorNotFoundException(id);

    actorRepository.deleteById(id);
    log.info("Deleted actor with id: {}", id);
  }
}
