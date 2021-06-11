INSERT INTO users VALUES(1, "admin@springoc.com", 1, 1, 1, 1, "$2a$10$QwVpZJ4Up9YbeqboJnNPzeqkJgD6IYrMPEeU0G7ye3uIT6aNTPN76", "admin");
INSERT INTO users VALUES(2, "user@springco.com", 1, 1, 1, 1, "$2a$10$QwVpZJ4Up9YbeqboJnNPzeqkJgD6IYrMPEeU0G7ye3uIT6aNTPN76", "user");
INSERT INTO roles VALUES(1, 'ROLE_ADMIN');
INSERT INTO roles VALUES(2, 'ROLE_USER');
INSERT INTO users_roles VALUES(1, 1);
INSERT INTO users_roles VALUES(1, 2);
