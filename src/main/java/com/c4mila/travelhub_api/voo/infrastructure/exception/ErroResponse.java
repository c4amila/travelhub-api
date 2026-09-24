package com.c4mila.travelhub_api.voo.infrastructure.exception;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErroResponse {
    private final String erroCodigo;
    private final String mensagem;
    private final List<String> detalhes;
    private final int status;
    private final String path;
}
