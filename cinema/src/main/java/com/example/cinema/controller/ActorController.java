package com.example.cinema.controller;

import com.example.cinema.dto.actor.ActorRequest;
import com.example.cinema.dto.actor.ActorResponse;
import com.example.cinema.service.ActorService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actor")
@RequiredArgsConstructor
@Slf4j
public class ActorController {

  private final ActorService actorService;

  @GetMapping
  public ResponseEntity<List<ActorResponse>> getAll() {
    log.info("Fetching all actors");
    return ResponseEntity.ok(actorService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ActorResponse> getById(@PathVariable String id) {
    log.info("Fetching actor with id: {}", id);
    return ResponseEntity.ok(actorService.findById(id));
  }

  @PostMapping
  public ResponseEntity<ActorResponse> create(@Valid @RequestBody ActorRequest dto) {
    log.info("Creating actor with data: {}", dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(actorService.save(dto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ActorResponse> update(@PathVariable String id, @Valid @RequestBody ActorRequest dto) {
    log.info("Updating actor with id: {}, data: {}", id, dto);
    return ResponseEntity.ok(actorService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    log.info("Deleting actor with id: {}", id);
    actorService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
