DELETE
FROM LIKES;
DELETE
FROM FILM_GENRE;
DELETE
FROM FRIENDSHIP;
DELETE
FROM USERS;
DELETE
FROM FILMS;
DELETE
FROM GENRES;
DELETE
FROM MPA;

ALTER TABLE USERS
    ALTER COLUMN "id" RESTART WITH 1;
ALTER TABLE FILMS
    ALTER COLUMN "id" RESTART WITH 1;

MERGE INTO MPA ("id","name")
    VALUES (1,'G'),
           (2,'PG'),
           (3,'PG-13'),
           (4,'R'),
           (5,'NC-17');

MERGE INTO GENRES ("id","name")
    VALUES (1,'Комедия'),
           (2,'Драма'),
           (3,'Мультфильм'),
           (4,'Триллер'),
           (5,'Документальный'),
           (6,'Боевик');
