INSERT INTO roles
    (id, name)
VALUES
    (0, 'ADMIN'),
    (1, 'USER');

INSERT INTO users
    (username, password, image, name, surname, email, birth_date, creation_date, last_login, active)
VALUES
    ('tokioschool', '$2a$10$.JGCNuoStUWFceU0S2oh.eoI5c3dIwzm5KiYvzoJW7KGrIOBb41yq', 'tokioschool.jpeg'
        , 'Tokio', 'School', 'tokioschool@gmail.com', '2010-06-06', '2021-12-03', '2022-01-01', 1);

INSERT INTO user_roles
    (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'tokioschool'), (SELECT id FROM roles WHERE name = 'ADMIN'));

INSERT INTO people
    (name, surname, type)
VALUES
    ('Peter', 'Jackson', 'DIRECTOR'),
    ('Orlando', 'Bloom', 'ACTOR'),
    ('Ridley', 'Scott', 'DIRECTOR');

INSERT INTO films
    (title, year, duration, avg_score, poster,user_id, director_id)
VALUES
    ('The Fellowship of the Ring', 2001, 178, 4,'the-fellowship-of-the-ring.jpg',(SELECT id FROM users WHERE username = 'tokioschool'), (SELECT id FROM people WHERE name = 'Peter' AND surname ='Jackson')),
    ('Kingdom of Heaven', 2005, 190, 3,'kingdom-of-heaven.jpg', (SELECT id FROM users WHERE username = 'tokioschool'), (SELECT id FROM people WHERE name = 'Ridley' AND surname ='Scott'));

INSERT INTO film_actors
    (film_id, actor_id)
VALUES
    ((SELECT id FROM films WHERE title = 'The Fellowship of the Ring'), (SELECT id FROM people WHERE name = 'Orlando' AND surname = 'Bloom')),
    ((SELECT id FROM films WHERE title = 'Kingdom of Heaven'), (SELECT id FROM people WHERE name = 'Orlando' AND surname = 'Bloom'));

INSERT INTO scores
    (value, film_id, user_id)
VALUES
    (4, (SELECT id FROM films WHERE title = 'The Fellowship of the Ring'), (SELECT id FROM users WHERE username = 'tokioschool')),
    (5, (SELECT id FROM films WHERE title = 'The Fellowship of the Ring'), (SELECT id FROM users WHERE username = 'tokioschool')),
    (4, (SELECT id FROM films WHERE title = 'The Fellowship of the Ring'), (SELECT id FROM users WHERE username = 'tokioschool')),
    (2, (SELECT id FROM films WHERE title = 'Kingdom of Heaven'), (SELECT id FROM users WHERE username = 'tokioschool')),
    (3, (SELECT id FROM films WHERE title = 'Kingdom of Heaven'), (SELECT id FROM users WHERE username = 'tokioschool')),
    (2, (SELECT id FROM films WHERE title = 'Kingdom of Heaven'), (SELECT id FROM users WHERE username = 'tokioschool'));
