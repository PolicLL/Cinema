package com.example.cinema.dto;

import java.util.List;

public record ActorResponse(
    String id,
    String description,
    List<MovieResponse> movieResponses
) {}
