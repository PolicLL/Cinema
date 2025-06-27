package com.example.cinema.integration;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cinema.dto.actor.ActorRequest;
import com.example.cinema.dto.actor.ActorResponse;
import com.example.cinema.dto.movie.MovieRequest;
import com.example.cinema.dto.movie.MovieResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ActorIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  void getAllActorsSuccess() throws Exception {
    mockMvc.perform(get("/actor")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

  @Test
  void getActorById() throws Exception {
    ActorRequest actorRequest = new ActorRequest("DESCRIPTION", List.of());

    String actorJsonRequest = objectMapper.writeValueAsString(actorRequest);

    String actorResponseString = mockMvc.perform(post("/actor")
            .contentType(MediaType.APPLICATION_JSON)
            .content(actorJsonRequest))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn().getResponse().getContentAsString();

    ActorResponse actorResponse = objectMapper.readValue(actorResponseString, ActorResponse.class);

    String retrievedActorJson = mockMvc.perform(get("/actor/" + actorResponse.id())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn().getResponse().getContentAsString();

    ActorResponse finalActor = objectMapper.readValue(retrievedActorJson, ActorResponse.class);

    assertThat(actorResponse).isEqualTo(finalActor);
  }

  @Test
  void createActorWithMovie() throws Exception {
    MovieRequest request = new MovieRequest("Movie", List.of());
    String jsonForMovieCreation = objectMapper.writeValueAsString(request);

    String createdMovieString = mockMvc.perform(post("/movie")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonForMovieCreation))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    MovieResponse movieResponse = objectMapper.readValue(createdMovieString, MovieResponse.class);

    ActorRequest actorRequest = new ActorRequest("DESCRIPTION", List.of(movieResponse.id()));


    String actorResponseString = mockMvc.perform(post("/actor")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(actorRequest)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn().getResponse().getContentAsString();

    ActorResponse actorResponse = objectMapper.readValue(actorResponseString, ActorResponse.class);

    String retrievedUserJson = mockMvc.perform(get("/actor/" + actorResponse.id())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn().getResponse().getContentAsString();

    ActorResponse finalActor = objectMapper.readValue(retrievedUserJson, ActorResponse.class);

    assertThat(finalActor.movieSummaries().get(0).id()).isEqualTo(movieResponse.id());
  }

  @Test
  void updateActorSuccess() throws Exception {
    ActorRequest createRequest = new ActorRequest("Initial description", List.of());
    String createJson = objectMapper.writeValueAsString(createRequest);

    String response = mockMvc.perform(post("/actor")
            .contentType(MediaType.APPLICATION_JSON)
            .content(createJson))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    ActorResponse created = objectMapper.readValue(response, ActorResponse.class);

    ActorRequest updateRequest = new ActorRequest("Updated description", List.of());
    String updateJson = objectMapper.writeValueAsString(updateRequest);

    String updatedResponse = mockMvc.perform(put("/actor/" + created.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    ActorResponse updated = objectMapper.readValue(updatedResponse, ActorResponse.class);
    assertThat(updated.description()).isEqualTo("Updated description");
  }

  @Test
  void deleteActorSuccess() throws Exception {
    ActorRequest createRequest = new ActorRequest("To be deleted", List.of());
    String createJson = objectMapper.writeValueAsString(createRequest);

    String response = mockMvc.perform(post("/actor")
            .contentType(MediaType.APPLICATION_JSON)
            .content(createJson))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    ActorResponse created = objectMapper.readValue(response, ActorResponse.class);

    mockMvc.perform(MockMvcRequestBuilders.delete("/actor/" + created.id()))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/actor/" + created.id()))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldFailToDeleteMovieWhenIdDoesNotExists() throws Exception {
    String nonExistingId = "NON-EXISTING-ID";
    mockMvc.perform(delete("/actor/" + nonExistingId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Actor with id " + nonExistingId + " is not found."))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.path").value("/actor/" + nonExistingId));
  }

  @Test
  void getAllActorsWithFilter() throws Exception {
    String uniqueDescription = "UniqueDesc123";
    ActorRequest request = new ActorRequest(uniqueDescription, List.of());
    String json = objectMapper.writeValueAsString(request);

    mockMvc.perform(post("/actor")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/actor")
            .param("description", uniqueDescription)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string(containsString(uniqueDescription)));
  }
}
