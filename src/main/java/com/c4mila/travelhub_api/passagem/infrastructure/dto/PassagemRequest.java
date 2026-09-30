package com.c4mila.travelhub_api.passagem.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record PassagemRequest(
        @NotBlank String numeroVoo
){}
