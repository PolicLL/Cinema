package com.example.cinema.exception;

public class ActorNotFoundException extends RuntimeException{

  public ActorNotFoundException(String message) {
    super(String.format("Actor with id %s is not found.", message));
  }
}
