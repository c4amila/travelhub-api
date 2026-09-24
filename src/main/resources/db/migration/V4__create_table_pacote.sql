CREATE TABLE pacote (
    id BIGSERIAL PRIMARY KEY,
    valor_total NUMERIC(10,2) NOT NULL CHECK (valor_total >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADO'
);