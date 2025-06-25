package com.example.cinema.dto.actor;

import com.example.cinema.dto.movie.MovieSummary;
import java.util.List;

public record ActorResponse(
    String id,
    String description,
    List<MovieSummary> movieSummaries
) {}
