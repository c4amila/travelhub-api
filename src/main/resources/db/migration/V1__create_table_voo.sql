CREATE TABLE voo (
    id BIGSERIAL PRIMARY KEY,
    companhia VARCHAR(100) NOT NULL,
    origem VARCHAR(100) NOT NULL,
    destino VARCHAR(100) NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    preco NUMERIC(10,2) NOT NULL CHECK (preco >= 0),
    assentos_totais INTEGER NOT NULL CHECK (assentos_totais > 0),
    assentos_disponiveis INTEGER NOT NULL CHECK (assentos_disponiveis >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    CONSTRAINT chk_voo_assentos CHECK (assentos_disponiveis <= assentos_totais)
);

CREATE INDEX idx_voo_origem_destino ON voo (origem, destino);
CREATE INDEX idx_voo_data_hora ON voo (data_hora);