CREATE TABLE hotel (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cidade VARCHAR(100) NOT NULL
);

CREATE INDEX idx_hotel_cidade ON hotel (cidade);