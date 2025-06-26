package com.example.cinema.exception;

public class MovieWithNameExistsException extends RuntimeException{

  public MovieWithNameExistsException(String message) {
    super("Movie with name " + message + " already exists.");
  }
}