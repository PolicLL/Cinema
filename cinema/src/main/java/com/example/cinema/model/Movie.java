package com.example.cinema.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

  @Id
  private String id;

  private String name;

  @ManyToMany
  @JoinTable(
      name = "movie_actor",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "actor_id")
  )
  private List<Actor> actors = new ArrayList<>();

  @Override
  public String toString() {
    return "Movie{" +
        "name='" + name + '\'' +
        '}';
  }
}