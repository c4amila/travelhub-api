package com.c4mila.travelhub_api.voo.infrastructure.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(VooJaCadastradoException.class)
    public ResponseEntity<Object> tratarVooJaCadastrado(VooJaCadastradoException e, WebRequest request){
        return criarRespostaErro(
                e,
                "VOO_JA_CADASTRADO",
                e.getMessage(),
                List.of(),
                HttpStatus.CONFLICT,
                request
        );
    }


    private ResponseEntity<Object> criarRespostaErro(
            Exception e,
            String erroCodigo,
            String mensagem,
            List<String> detalhes,
            HttpStatus status,
            WebRequest request
    ){

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;

        ErroResponse erroResponse = ErroResponse
                .builder()
                .erroCodigo(erroCodigo)
                .mensagem(mensagem)
                .detalhes(detalhes)
                .status(status.value())
                .path(servletWebRequest.getRequest().getRequestURI())
                .build();

        return handleExceptionInternal(
                e,
                erroResponse,
                new HttpHeaders(),
                status,
                request
        );
    }
}
