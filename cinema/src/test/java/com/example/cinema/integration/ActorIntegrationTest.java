package com.example.cinema.integration;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cinema.dto.actor.ActorRequest;
import com.example.cinema.dto.actor.ActorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
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

    String userJsonRequest = objectMapper.writeValueAsString(actorRequest);

    String actorResponseString = mockMvc.perform(post("/actor")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJsonRequest))
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

    assertThat(actorResponse).isEqualTo(finalActor);
  }


}
