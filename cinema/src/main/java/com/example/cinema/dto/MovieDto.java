package com.example.cinema.dto;

import java.util.List;

public record MovieDto(
    String id,
    String name,
    List<String> actorIds
) {}
