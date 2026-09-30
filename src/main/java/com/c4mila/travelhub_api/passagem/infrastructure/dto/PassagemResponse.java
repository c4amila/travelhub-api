package com.c4mila.travelhub_api.passagem.infrastructure.dto;

import com.c4mila.travelhub_api.passagem.domain.model.Passagem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PassagemResponse(
        Long id,
        Long vooId,
        String numeroVoo,
        LocalDateTime dataCompra,
        BigDecimal valorPago
){
    public static PassagemResponse from(Passagem passagem){
        return new PassagemResponse(
                passagem.getId(),
                passagem.getVooId().getId(),
                passagem.getVooId().getNumeroVoo(),
                passagem.getDataCompra(),
                passagem.getValorPago()
        );
    }
}
