package com.c4mila.travelhub_api.voo.infrastructure.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(VooNaoEncontradoException.class)
    public ResponseEntity<Object> tratarVooNaoEncontrado(VooNaoEncontradoException e, WebRequest request){
        return criarRespostaErro(
                e,
                "VOO_NAO_ENCONTRADO",
                e.getMessage(),
                List.of(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request){

        List<String> detalhes = e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        return criarRespostaErro(
                e,
                "DADOS_INVALIDOS",
                "Existem campos inválidos na requisição",
                detalhes,
                HttpStatus.BAD_REQUEST,
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
