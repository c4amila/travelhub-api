CREATE TABLE quarto (
    id BIGSERIAL PRIMARY KEY,
    hotel_id BIGINT NOT NULL REFERENCES hotel (id),
    tipo VARCHAR(30) NOT NULL,
    capacidade INTEGER NOT NULL CHECK (capacidade > 0),
    preco_diaria NUMERIC(10,2) NOT NULL CHECK (preco_diaria >= 0)
);

CREATE INDEX idx_quarto_hotel_id ON quarto (hotel_id)