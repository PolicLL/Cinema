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

INSERT INTO actor (id, description) VALUES
('a1', 'Funny comedian'),
('a2', 'Action movie hero'),
('a3', 'Drama specialist'),
('a4', 'Sci-fi enthusiast'),
('a5', 'Romantic lead'),
('a6', 'Villain roles master'),
('a7', 'Indie film expert'),
('a8', 'Oscar winner'),
('a9', 'Voice actor'),
('a10', 'Martial artist');

INSERT INTO movie (id, name) VALUES
('m1', 'The Last Laugh'),
('m2', 'Explosive Justice'),
('m3', 'Tears of Stone'),
('m4', 'Galactic Voyage'),
('m5', 'Love in Paris'),
('m6', 'Dark Empire'),
('m7', 'Quiet Storm'),
('m8', 'The Winner'),
('m9', 'Animated World'),
('m10', 'Kung Fu Master');

INSERT INTO movie_actor (movie_id, actor_id) VALUES
('m1', 'a1'),
('m1', 'a7'),

('m2', 'a2'),
('m2', 'a6'),

('m3', 'a3'),
('m3', 'a5'),

('m4', 'a4'),
('m4', 'a8'),

('m5', 'a5'),
('m5', 'a3'),

('m6', 'a6'),
('m6', 'a2'),

('m7', 'a7'),
('m7', 'a9'),

('m8', 'a8'),
('m8', 'a1'),

('m9', 'a9'),
('m9', 'a10'),

('m10', 'a10'),
('m10', 'a2');
