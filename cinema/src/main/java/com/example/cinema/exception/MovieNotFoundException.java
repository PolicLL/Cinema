package com.example.cinema.exception;

public class MovieNotFoundException extends RuntimeException {
  public MovieNotFoundException(String message) {
    super(String.format("Movie with id %s is not found.", message));
  }
}