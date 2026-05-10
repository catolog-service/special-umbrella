CREATE TABLE IF NOT EXISTS events (
    id          UUID         PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    date_time   TIMESTAMP,
    location    VARCHAR(255)
);

