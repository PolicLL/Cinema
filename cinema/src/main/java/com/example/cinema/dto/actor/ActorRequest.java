package com.example.cinema.dto.actor;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ActorRequest(
    @NotBlank(message = "Actor has to have a description.")
    String description,
    List<String> movieIds
) {}
