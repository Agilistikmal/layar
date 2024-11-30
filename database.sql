CREATE TABLE users
(
    id               varchar(255) NOT NULL PRIMARY KEY,
    username         varchar(100) NOT NULL,
    password         varchar(100) NOT NULL,
    full_name        varchar(100) NOT NULL,
    token            varchar(255),
    token_expired_at BIGINT,
    created_at       BIGINT,
    updated_at       BIGINT,
    UNIQUE (token)
);


SELECT * FROM users;

CREATE TABLE movies
(
    id          varchar(255) NOT NULL PRIMARY KEY,
    slug        varchar(100) NOT NULL,
    title       varchar(100) NOT NULL,
    description TEXT,
    created_at  BIGINT,
    updated_at  BIGINT,
    UNIQUE (slug)
);

CREATE TABLE movie_banners
(
    id       varchar(255) NOT NULL PRIMARY KEY,
    movie_id varchar(255) NOT NULL,
    url      varchar(255) NOT NULL,
    name     varchar(100) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies (id)
);

CREATE TABLE movie_videos
(
    id       varchar(255) NOT NULL PRIMARY KEY,
    movie_id varchar(255) NOT NULL,
    url      varchar(255) NOT NULL,
    name     VARCHAR(100) NOT NULL,
    quality  VARCHAR(100) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies (id)
);

