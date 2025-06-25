package com.example.cinema.dto.movie;

import com.example.cinema.dto.actor.ActorSummary;
import java.util.List;

public record MovieResponse(
    String id,
    String name,
    List<ActorSummary> actorSummaries
) {}
