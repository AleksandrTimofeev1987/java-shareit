DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS item_requests CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    user_id          bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name             VARCHAR(255) UNIQUE,
    email            VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS item_requests
(
    request_id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created         TIMESTAMP NOT NULL,
    description     VARCHAR(255) NOT NULL,
    requester_id    BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items
(
    item_id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    available        BOOLEAN NOT NULL,
    item_description VARCHAR(255) NOT NULL,
    item_name        VARCHAR(255) NOT NULL,
    owner_id         BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    request_id       BIGINT REFERENCES item_requests (request_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created    TIMESTAMP NOT NULL,
    text       VARCHAR(255) NOT NULL,
    author_id  BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    item_id    BIGINT NOT NULL REFERENCES items (item_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings
(
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    end_date   TIMESTAMP NOT NULL,
    start_date TIMESTAMP NOT NULL,
    status     VARCHAR(10) NOT NULL,
    booker_id  BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    item_id    BIGINT NOT NULL REFERENCES items (item_id) ON DELETE CASCADE
);