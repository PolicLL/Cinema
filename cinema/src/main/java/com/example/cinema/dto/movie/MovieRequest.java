package com.example.cinema.dto.movie;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record MovieRequest(
    @NotBlank(message = "Movie has to have a name.")
    String name,
    List<String> actorIds
) {}
