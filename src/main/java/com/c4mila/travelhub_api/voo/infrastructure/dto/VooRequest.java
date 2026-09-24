package com.c4mila.travelhub_api.voo.infrastructure.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VooRequest (

        @NotBlank @Size(max = 20) String numeroVoo,
        @NotBlank @Size(max = 100) String companhia,
        @NotBlank @Size(max = 100) String origem,
        @NotBlank @Size(max = 100) String destino,
        @NotNull @Future LocalDateTime dataHora,
        @NotNull @DecimalMin(value = "0.0") BigDecimal preco,
        @NotNull @Min(1) Integer assentosTotais
){}
