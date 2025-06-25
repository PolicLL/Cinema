package com.example.cinema.dto;

import java.util.List;

public record ActorDto(
    String id,
    String description,
    List<String> movieIds
) {}
