package com.example.cinema.specification;

import com.example.cinema.model.Movie;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecification {

  public static Specification<Movie> hasId(String id) {
    return (root, query, criteriaBuilder) ->
        id == null ? null : criteriaBuilder.equal(root.get("id"), id);
  }

  public static Specification<Movie> hasName(String name) {
    return (root, query, criteriaBuilder) ->
        name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(
            root.get("name")), "%" + name.toLowerCase() + "%");
  }
}