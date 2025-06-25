package com.example.cinema.service;

import com.example.cinema.dto.ActorDto;
import com.example.cinema.mapper.ActorMapper;
import com.example.cinema.model.Actor;
import com.example.cinema.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActorService {

  private final ActorRepository actorRepository;
  private final ActorMapper actorMapper;

  public List<ActorDto> findAll() {
    return actorRepository.findAll().stream()
        .map(actorMapper::toDto)
        .collect(Collectors.toList());
  }

  public ActorDto findById(String id) {
    return actorMapper.toDto(actorRepository.findById(id).orElseThrow());
  }

  public ActorDto save(ActorDto dto) {
    Actor actor = actorMapper.toEntity(dto);
    return actorMapper.toDto(actorRepository.save(actor));
  }

  public ActorDto update(String id, ActorDto dto) {
    Actor existing = actorRepository.findById(id).orElseThrow();
    existing.setDescription(dto.description());
    existing.setMovies(actorMapper.toEntity(dto).getMovies());
    return actorMapper.toDto(actorRepository.save(existing));
  }

  public void delete(String id) {
    actorRepository.deleteById(id);
  }
}
