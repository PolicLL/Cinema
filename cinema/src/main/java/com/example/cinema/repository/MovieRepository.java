package com.example.cinema.repository;

import com.example.cinema.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

  boolean existsByName(String name);
}
