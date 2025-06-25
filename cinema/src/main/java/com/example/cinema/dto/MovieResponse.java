package com.example.cinema.dto;

import java.util.List;

public record MovieResponse(
    String id,
    String name,
    List<ActorResponse> actors
) {}
