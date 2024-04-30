CREATE TABLE IF NOT EXISTS manufacturer
(
    id   BIGSERIAL NOT NULL ,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS category
(
    id   BIGSERIAL NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS model
(
    id              BIGSERIAL NOT NULL,
    name            VARCHAR(255),
    manufacturer_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (manufacturer_id)
        REFERENCES manufacturer
);

CREATE TABLE IF NOT EXISTS car
(
    id        BIGSERIAL NOT NULL,
    object_id VARCHAR(255),
    year      INTEGER,
    model_id  BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (model_id)
        REFERENCES model
);

CREATE TABLE IF NOT EXISTS car_category
(
    car_id      BIGSERIAL NOT NULL,
    category_id BIGSERIAL NOT NULL,
    FOREIGN KEY (car_id)
        REFERENCES car,
    FOREIGN KEY (category_id)
        REFERENCES category
);