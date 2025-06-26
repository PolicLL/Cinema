package com.example.cinema.repository;

import com.example.cinema.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, String>,
    JpaSpecificationExecutor<Actor> {
}
