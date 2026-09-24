CREATE TABLE passagem (
    id BIGSERIAL PRIMARY KEY,
    voo_id BIGINT NOT NULL REFERENCES voo(id),
    pacote_id BIGINT REFERENCES pacote(id),
    data_compra TIMESTAMP NOT NULL DEFAULT now(),
    valor_pago NUMERIC(10,2) NOT NULL CHECK (valor_pago >= 0)
);

CREATE INDEX idx_passagem_voo_id ON passagem(voo_id);
CREATE UNIQUE INDEX uq_passagem_pacote_id ON passagem(pacote_id) WHERE pacote_id IS NOT NULL;