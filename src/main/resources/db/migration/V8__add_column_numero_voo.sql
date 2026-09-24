ALTER TABLE voo
    ADD COLUMN numero_voo VARCHAR(20) NOT NULL;

ALTER TABLE voo
    ADD CONSTRAINT uk_voo_numero_voo UNIQUE (numero_voo);