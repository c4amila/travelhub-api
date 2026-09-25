package com.c4mila.travelhub_api.voo.infrastructure.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AtualizarVooRequest (
        @Size(max = 100) String companhia,
        @Size(max = 100) String origem,
        @Size(max = 100) String destino,
        @Future LocalDateTime dataHora,
        @DecimalMin(value = "0.0") BigDecimal preco,
        @Min(1) Integer assentosTotais
){}
