package com.c4mila.travelhub_api.passagem.infrastructure.exception;

public class VooIndisponivelException extends RuntimeException {
    public VooIndisponivelException(String message) {
        super(message);
    }
}
