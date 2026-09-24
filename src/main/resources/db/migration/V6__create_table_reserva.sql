CREATE TABLE reserva (
    id BIGSERIAL PRIMARY KEY,
    quarto_id BIGINT NOT NULL REFERENCES quarto (id),
    pacote_id BIGINT REFERENCES pacote (id),
    checkin DATE NOT NULL,
    checkout DATE NOT NULL,
    valor_total NUMERIC(10,2) NOT NULL CHECK (valor_total >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADA',
    CONSTRAINT chk_reserva_data CHECK (checkout > checkin)
);

CREATE INDEX idx_reserva_quarto_id ON reserva (quarto_id);
CREATE UNIQUE INDEX uq_reserva_pacote_id ON reserva (pacote_id) WHERE pacote_id IS NOT NULL;