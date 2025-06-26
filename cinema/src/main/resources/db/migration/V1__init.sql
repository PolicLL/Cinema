CREATE TABLE actor (
    id VARCHAR(255) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE movie (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE movie_actor (
    movie_id VARCHAR(255),
    actor_id VARCHAR(255),
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES movie(id),
    FOREIGN KEY (actor_id) REFERENCES actor(id)
);