INSERT INTO roles
    (name)
VALUES
    ('ADMIN'),
    ('USER');

INSERT INTO users
    (username, password, active)
VALUES
    ('tokioschool', 'tokioschool', 1);

INSERT INTO user_roles
    (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'tokioschool'), (SELECT id FROM roles WHERE name = 'ADMIN'));

