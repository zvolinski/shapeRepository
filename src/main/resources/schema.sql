CREATE TABLE shape
(
    id        SERIAL PRIMARY KEY,
    type      VARCHAR(255) NOT NULL,
    perimeters DOUBLE PRECISION[]
);