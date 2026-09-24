package com.c4mila.travelhub_api.voo.infrastructure.exception;

public class VooNaoEncontradoException extends RuntimeException {
    public VooNaoEncontradoException(String message) {
        super(message);
    }
}
