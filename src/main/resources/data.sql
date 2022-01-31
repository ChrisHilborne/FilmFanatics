INSERT INTO roles
    (id, name)
VALUES
    (0, 'ADMIN'),
    (1, 'USER');

INSERT INTO users (id, username, password, active) VALUES (0,'tokioschool', 'tokioschool', 1);

INSERT INTO user_roles (user_id, role_id) VALUES ((SELECT id FROM users WHERE username = 'tokioschool'), (SELECT id FROM roles WHERE name = 'ADMIN'));

INSERT INTO people
    (id, name, surname, type)
VALUES
    (0, 'Peter', 'Jackson', 'DIRECTOR'),
    (1, 'Orlando', 'Bloom', 'ACTOR'),
    (2, 'Ridley', 'Scott', 'DIRECTOR');

INSERT INTO films
    (id, title, year, duration, user_id, director_id)
VALUES
    (0, 'The Fellowship of the Ring', 2001, 178, (SELECT id FROM users WHERE username = 'tokioschool'), (SELECT id FROM people WHERE name = 'Peter' AND surname ='Jackson')),
    (1, 'Kingdom of Heaven', 2005, 190, (SELECT id FROM users WHERE username = 'tokioschool'), (SELECT id FROM people WHERE name = 'Ridley' AND surname ='Scott'));

INSERT INTO film_actors
    (film_id, actor_id)
VALUES
    ((SELECT id FROM films WHERE title = 'Lord of the Rings'), (SELECT id FROM people WHERE name = 'Orlando' AND surname = 'Bloom')),
    ((SELECT id FROM films WHERE title = 'Kingdom of Heaven'), (SELECT id FROM people WHERE name = 'Orlando' AND surname = 'Bloom'));


