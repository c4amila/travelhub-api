package com.c4mila.travelhub_api.passagem.infrastructure.exception;

public class PassagemNaoEncontradaException extends RuntimeException {
    public PassagemNaoEncontradaException(String message) {
        super(message);
    }
}
