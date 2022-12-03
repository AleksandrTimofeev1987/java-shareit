
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    user_id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    CONSTRAINT email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    item_id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    item_name VARCHAR(50) NOT NULL,
    item_description VARCHAR(200) NOT NULL,
    owner_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    available BOOLEAN NOT NULL
);