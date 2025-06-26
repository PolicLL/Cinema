package com.example.cinema.utils;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {

  public static <T> Specification<T> safeAnd(Specification<T> first, Specification<T> second) {
    if (first == null) return second;
    if (second == null) return first;
    return first.and(second);
  }
}