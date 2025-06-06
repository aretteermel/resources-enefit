CREATE TABLE IF NOT EXISTS resource (
    id SERIAL PRIMARY KEY,
    type VARCHAR(30) NOT NULL,
    country_code VARCHAR(2) NOT NULL
);

CREATE TABLE IF NOT EXISTS location (
    id SERIAL PRIMARY KEY,
    street_address VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    postal_code VARCHAR(12) NOT NULL,
    country_code VARCHAR(2) NOT NULL,
    resource_id INTEGER UNIQUE NOT NULL,
    CONSTRAINT fk_resource_location FOREIGN KEY (resource_id) REFERENCES resource(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS characteristic (
    id SERIAL PRIMARY KEY,
    code VARCHAR(5) NOT NULL,
    type VARCHAR(30) NOT NULL,
    value VARCHAR(500) NOT NULL,
    resource_id INTEGER NOT NULL,
    CONSTRAINT fk_resource_characteristic FOREIGN KEY (resource_id) REFERENCES resource(id) ON DELETE CASCADE
);
