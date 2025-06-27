package com.example.cinema.repository;

import com.example.cinema.model.Movie;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>,
    JpaSpecificationExecutor<Movie>
{

  boolean existsByName(String name);

  Optional<Movie> findByName(String name);
}
