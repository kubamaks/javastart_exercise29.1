-- login: user -> password: user
-- login: user1 -> password: user1
-- login: admin -> password : admin
-- login: admin1 -> password : admin1
-- login: user2 -> password: user2

INSERT INTO application_user (login, first_name, last_name, email, password)
VALUES ('user', 'Maciej', 'Kornel', 'maciej.kornel@mojemail.ex', '{bcrypt}$2a$10$ActCQB2npsfQb0XsaIDWrusX5cAP724RflvGCXhE0fv2GF.nisPO.'),
       ('user1', 'Agata', 'Wajda', 'aga@mojastrona.aa', '{bcrypt}$2a$10$1KEykJOXGB4ryKe1a1yW/.gC1HNwg9HotPDYkQ8KPuan9pxKKJ44q'),
       ('admin', 'Krzysztof', 'Zawias', 'krzysiek@malpeczka.pl', '{bcrypt}$2a$10$tW15FmUWHMwvZ7SYPRdftOQ3drklOliSgpdG3cHMtUpYvAW1yPjHq'),
       ('admin1', 'Roland', 'Fajera', 'rollfajer@malpeczka.pl', '{bcrypt}$2a$10$boOhdUaleqXJXRPxl.NN6ee1P8RMhPis.l1CMo5eLtcTDJ5dc0AW2'),
       ('user2', 'Aleksandra', 'Porębska', 'alaporebska@hello.xy', '{bcrypt}$2a$10$L.nGYhX8MhUtpLv/Mu/6Au7O2d7anxGddMrRRvs9n5nxPdDiza6fG');

INSERT INTO
    role (name)
VALUES
    ('ADMIN'),
    ('USER');


INSERT INTO users_roles (user_id, role_id)
VALUES (1, 2),
       (2, 2),
       (3, 1),
       (3, 2),
       (4, 1),
       (4, 2),
       (5, 2);