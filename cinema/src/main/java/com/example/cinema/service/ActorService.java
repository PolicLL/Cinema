package com.example.cinema.service;

import com.example.cinema.dto.actor.ActorRequest;
import com.example.cinema.dto.actor.ActorResponse;
import com.example.cinema.mapper.ActorMapper;
import com.example.cinema.model.Actor;
import com.example.cinema.repository.ActorRepository;
import com.example.cinema.repository.MovieRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActorService {

  private final ActorRepository actorRepository;
  private final MovieRepository movieRepository;
  private final ActorMapper actorMapper;

  public List<ActorResponse> findAll() {
    log.info("Fetching all actors");
    List<ActorResponse> actors = actorRepository.findAll().stream()
        .map(actorMapper::toActorResponse)
        .toList();
    log.info("Found {} actors", actors.size());
    return actors;
  }

  public ActorResponse findById(String id) {
    log.info("Fetching actor with id: {}", id);
    ActorResponse response = actorMapper.toActorResponse(actorRepository.findById(id).orElseThrow());
    log.info("Found actor: {}", response);
    return response;
  }

  public ActorResponse save(ActorRequest dto) {
    log.info("Saving new actor with data: {}", dto);
    Actor actor = actorMapper.toEntity(dto);
    actor.setMovies(movieRepository.findAllById(dto.movieIds()));
    actor.setId(UUID.randomUUID().toString());
    Actor savedActor = actorRepository.save(actor);
    log.info("Saved actor with id: {}", savedActor.getId());
    return actorMapper.toActorResponse(savedActor);
  }

  public ActorResponse update(String id, ActorRequest dto) {
    log.info("Updating actor with id: {} with data: {}", id, dto);
    Actor existing = actorRepository.findById(id).orElseThrow();
    existing.setDescription(dto.description());
    existing.setMovies(movieRepository.findAllById(dto.movieIds()));
    Actor updatedActor = actorRepository.save(existing);
    log.info("Updated actor with id: {}", updatedActor.getId());
    return actorMapper.toActorResponse(updatedActor);
  }

  public void delete(String id) {
    log.info("Deleting actor with id: {}", id);
    actorRepository.deleteById(id);
    log.info("Deleted actor with id: {}", id);
  }
}
