package com.example.cinema.integration;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cinema.dto.movie.MovieRequest;
import com.example.cinema.dto.movie.MovieResponse;
import com.example.cinema.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MovieIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MovieRepository movieRepository;

  @Test
  void getAllMoviesSuccess() throws Exception {
    mockMvc.perform(get("/movie")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void getMovieByIdSuccess() throws Exception {
    MovieRequest movieRequest = new MovieRequest("Inception 1", List.of());
    String movieJson = objectMapper.writeValueAsString(movieRequest);

    String response = mockMvc.perform(post("/movie")
            .contentType(MediaType.APPLICATION_JSON)
            .content(movieJson))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    MovieResponse created = objectMapper.readValue(response, MovieResponse.class);

    String retrievedJson = mockMvc.perform(get("/movie/" + created.id())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    MovieResponse retrieved = objectMapper.readValue(retrievedJson, MovieResponse.class);
    assertThat(retrieved).usingRecursiveComparison().isEqualTo(created);
  }

  @Test
  void updateMovieSuccess() throws Exception {
    MovieRequest createRequest = new MovieRequest("Old Title", List.of());
    String createJson = objectMapper.writeValueAsString(createRequest);

    String createdResponse = mockMvc.perform(post("/movie")
            .contentType(MediaType.APPLICATION_JSON)
            .content(createJson))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    MovieResponse created = objectMapper.readValue(createdResponse, MovieResponse.class);

    MovieRequest updateRequest = new MovieRequest("New Title", List.of());
    String updateJson = objectMapper.writeValueAsString(updateRequest);

    String updatedResponse = mockMvc.perform(put("/movie/" + created.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    MovieResponse updated = objectMapper.readValue(updatedResponse, MovieResponse.class);
    assertThat(updated.name()).isEqualTo("New Title");
    assertThat(updated.actorSummaries()).isEmpty();
  }
  @Test
  void shouldFailToCreateMovieWhenNameAlreadyExists() throws Exception {
    MovieRequest createRequest = new MovieRequest("Duplicate Test", List.of());

    String request = objectMapper.writeValueAsString(createRequest);

    mockMvc.perform(post("/movie")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    mockMvc.perform(post("/movie")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Movie with name Duplicate Test already exists."))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.path").value("/movie"));
  }

  @Test
  void shouldFailToCreateMovieWhenNameIsBlank() throws Exception {
    MovieRequest createRequest = new MovieRequest("", List.of());

    String request = objectMapper.writeValueAsString(createRequest);

    mockMvc.perform(post("/movie")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Movie has to have a name."))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.path").value("/movie"));
  }


  @Test
  void deleteMovieSuccess() throws Exception {
    MovieRequest request = new MovieRequest("To Be Deleted", List.of());
    String json = objectMapper.writeValueAsString(request);

    String response = mockMvc.perform(post("/movie")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    MovieResponse created = objectMapper.readValue(response, MovieResponse.class);

    mockMvc.perform(delete("/movie/" + created.id()))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/movie/" + created.id()))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldFailToDeleteMovieWhenIdDoesNotExists() throws Exception {
    String nonExistingId = "NON-EXISTING-ID";
    mockMvc.perform(delete("/movie/" + nonExistingId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Movie with id " + nonExistingId + " is not found."))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.path").value("/movie/" + nonExistingId));
  }

  @Test
  void getAllMoviesWithNameFilter() throws Exception {
    String uniqueName = "Unique Movie " + System.currentTimeMillis();
    MovieRequest request = new MovieRequest(uniqueName, List.of());
    String json = objectMapper.writeValueAsString(request);

    mockMvc.perform(post("/movie")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/movie")
            .param("name", uniqueName)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(uniqueName)));
  }
}