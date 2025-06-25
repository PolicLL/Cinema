package com.example.cinema.controller;

import com.example.cinema.dto.ActorDto;
import com.example.cinema.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actors")
@RequiredArgsConstructor
public class ActorController {

  private final ActorService actorService;

  @GetMapping
  public List<ActorDto> getAll() {
    return actorService.findAll();
  }

  @GetMapping("/{id}")
  public ActorDto getById(@PathVariable String id) {
    return actorService.findById(id);
  }

  @PostMapping
  public ActorDto create(@RequestBody ActorDto dto) {
    return actorService.save(dto);
  }

  @PutMapping("/{id}")
  public ActorDto update(@PathVariable String id, @RequestBody ActorDto dto) {
    return actorService.update(id, dto);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable String id) {
    actorService.delete(id);
  }
}
