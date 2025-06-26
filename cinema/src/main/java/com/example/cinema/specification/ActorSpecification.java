package com.example.cinema.specification;

import com.example.cinema.model.Actor;
import org.springframework.data.jpa.domain.Specification;

public class ActorSpecification {

  public static Specification<Actor> hasId(String id) {
    return (root, query, criteriaBuilder) ->
        id == null ? null : criteriaBuilder.equal(root.get("id"), id);
  }

  public static Specification<Actor> hasDescription(String description) {
    return (root, query, criteriaBuilder) ->
        description == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
  }
}