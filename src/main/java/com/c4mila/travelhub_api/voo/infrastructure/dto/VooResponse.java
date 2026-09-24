package com.c4mila.travelhub_api.voo.infrastructure.dto;

import com.c4mila.travelhub_api.voo.domain.enums.StatusVoo;
import com.c4mila.travelhub_api.voo.domain.model.Voo;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public record VooResponse(
        Long id,
        String numeroVoo,
        String companhia,
        String origem,
        String destino,
        LocalDateTime dataHora,
        BigDecimal preco,
        Integer assentosTotais,
        Integer assentosDisponiveis,
        StatusVoo status,
        Instant criadoEm,
        Instant atualizadoEm
) {
    public static VooResponse from(Voo voo){
        return new VooResponse(
                voo.getId(),
                voo.getNumeroVoo(),
                voo.getCompanhia(),
                voo.getOrigem(),
                voo.getDestino(),
                voo.getDataHora(),
                voo.getPreco(),
                voo.getAssentosTotais(),
                voo.getAssentosDisponiveis(),
                voo.getStatus(),
                voo.getCriadoEm(),
                voo.getAtualizadoEm()
        );
    }
}
