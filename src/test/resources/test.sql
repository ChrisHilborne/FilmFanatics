INSERT INTO people
    (name, surname, type)
VALUES
    ('Peter', 'Jackson', 'DIRECTOR'),
    ('Orlando', 'Bloom', 'ACTOR'),
    ('Ridley', 'Scott', 'DIRECTOR');

INSERT INTO films
    (title, year, duration, poster, director_id)
VALUES
    ('The Fellowship of the Ring', 2001, 178, 'the-fellowship-of-the-ring.jpeg', (SELECT id FROM people WHERE name = 'Peter' AND surname ='Jackson')),
    ('Kingdom of Heaven', 2005, 190, 'kingdom-of-heaven.jpeg', (SELECT id FROM people WHERE name = 'Ridley' AND surname ='Scott'));

INSERT INTO film_actors
    (film_id, actor_id)
VALUES
    ((SELECT id FROM films WHERE title = 'The Fellowship of the Ring'), (SELECT id FROM people WHERE name = 'Orlando' AND surname = 'Bloom')),
    ((SELECT id FROM films WHERE title = 'Kingdom of Heaven'), (SELECT id FROM people WHERE name = 'Orlando' AND surname = 'Bloom'));

INSERT INTO scores
    (value, film_id)
VALUES
    (4, (SELECT id FROM films WHERE title = 'The Fellowship of the Ring')),
    (5, (SELECT id FROM films WHERE title = 'The Fellowship of the Ring')),
    (4, (SELECT id FROM films WHERE title = 'The Fellowship of the Ring')),
    (2, (SELECT id FROM films WHERE title = 'Kingdom of Heaven')),
    (3, (SELECT id FROM films WHERE title = 'Kingdom of Heaven')),
    (2, (SELECT id FROM films WHERE title = 'Kingdom of Heaven'));
