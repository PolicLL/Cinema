package com.example.cinema.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Actor {

  @Id
  private String id;

  private String description;

  @ManyToMany(mappedBy = "actors")
  private List<Movie> movies = new ArrayList<>();
}
