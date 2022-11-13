CREATE ALIAS IF NOT EXISTS getDate AS
'java.util.Date getDate() {
    return new java.util.Date();
}';

CREATE TABLE IF NOT EXISTS mpa
(
    id   BIGINT  NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR NOT NULL,
    releaseDate DATE    NOT NULL,
    description VARCHAR(200),
    duration    INTEGER NOT NULL,
    rate        INTEGER NOT NULL,
    mpa         INTEGER NOT NULL,
    genres      INTEGER,
    likes       INTEGER,
    CONSTRAINT duration_positive
        CHECK (duration >= 0),
    CONSTRAINT space_name
        CHECK (name NOT LIKE ' ' and name NOT LIKE ''),
    CONSTRAINT date_release
        check (releaseDate>'1895-12-27'),
    CONSTRAINT "films_mpa_foreign" FOREIGN KEY (mpa) REFERENCES mpa (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    VARCHAR NOT NULL UNIQUE,
    login    VARCHAR NOT NULL UNIQUE,
    name     VARCHAR,
    birthday DATE,
    CONSTRAINT login_space
        CHECK (login NOT LIKE '% %' and login NOT LIKE ''),
    CONSTRAINT email_at
        CHECK (email LIKE '%@%'),
    CONSTRAINT birthday_check
        CHECK (CAST(birthday AS date) <= CAST(getDate() as date))
);


CREATE TABLE IF NOT EXISTS genres
(
    id   BIGINT  NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL
);



CREATE TABLE IF NOT EXISTS friendship
(
    user_id   BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    CONSTRAINT "friendship_friend_id_foreign" FOREIGN KEY (friend_id) REFERENCES users (id),
    CONSTRAINT "friendship_user_id_foreign" FOREIGN KEY (user_id) REFERENCES users (id)
);


CREATE TABLE IF NOT EXISTS likes
(
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT "likes_film_id_foreign" FOREIGN KEY (film_id) REFERENCES films (id),
    CONSTRAINT "likes_user_id_foreign" FOREIGN KEY (user_id) REFERENCES users (id)
);


CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  BIGINT  NOT NULL,
    genre_id INTEGER NOT NULL,
    CONSTRAINT "film_genre_film_id_foreign" FOREIGN KEY (film_id) REFERENCES FILMS (id),
    CONSTRAINT "film_genre_genre_id_foreign" FOREIGN KEY (genre_id) REFERENCES GENRES (id)
);