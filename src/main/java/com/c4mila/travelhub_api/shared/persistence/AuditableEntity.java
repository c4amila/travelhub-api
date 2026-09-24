package com.c4mila.travelhub_api.shared.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Instant;

@MappedSuperclass
public abstract class AuditableEntity {
    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    @PrePersist
    protected void definirCriacao(){
        Instant now = Instant.now();

        this.criadoEm = now;
        this.atualizadoEm = now;
    }

    @PreUpdate
    protected void definirAtualizacao(){
        this.atualizadoEm = Instant.now();
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }
}
