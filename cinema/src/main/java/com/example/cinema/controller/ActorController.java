package com.example.cinema.controller;

import com.example.cinema.dto.actor.ActorRequest;
import com.example.cinema.dto.actor.ActorResponse;
import com.example.cinema.service.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Actor", description = "CRUD operations for actors")
public class ActorController {

  private final ActorService actorService;

  @GetMapping
  @Operation(summary = "Get all actors", description = "Returns a list of all actors")
  @ApiResponse(responseCode = "200", description = "List of actors returned successfully")
  public ResponseEntity<List<ActorResponse>> getAll() {
    log.info("Fetching all actors");
    return ResponseEntity.ok(actorService.findAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get actor by ID", description = "Returns actor details by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Actor found"),
      @ApiResponse(responseCode = "404", description = "Actor not found")
  })
  public ResponseEntity<ActorResponse> getById(@PathVariable String id) {
    log.info("Fetching actor with id: {}", id);
    return ResponseEntity.ok(actorService.findById(id));
  }

  @PostMapping
  @Operation(summary = "Create new actor", description = "Creates a new actor from request body")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Actor created successfully"),
      @ApiResponse(responseCode = "400", description = "Validation failed")
  })
  public ResponseEntity<ActorResponse> create(@Valid @RequestBody ActorRequest dto) {
    log.info("Creating actor with data: {}", dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(actorService.save(dto));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update existing actor", description = "Updates an actor with given ID and request body")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Actor updated successfully"),
      @ApiResponse(responseCode = "404", description = "Actor not found"),
      @ApiResponse(responseCode = "400", description = "Validation failed")
  })
  public ResponseEntity<ActorResponse> update(@PathVariable String id, @Valid @RequestBody ActorRequest dto) {
    log.info("Updating actor with id: {}, data: {}", id, dto);
    return ResponseEntity.ok(actorService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete actor", description = "Deletes the actor with the given ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Actor deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Actor not found")
  })
  public ResponseEntity<Void> delete(@PathVariable String id) {
    log.info("Deleting actor with id: {}", id);
    actorService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
