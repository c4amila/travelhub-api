package com.c4mila.travelhub_api.passagem.infrastructure.controller;

import com.c4mila.travelhub_api.passagem.application.service.PassagemService;
import com.c4mila.travelhub_api.passagem.infrastructure.dto.PassagemRequest;
import com.c4mila.travelhub_api.passagem.infrastructure.dto.PassagemResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.c4mila.travelhub_api.shared.web.RestConstants.PATH_PASSAGENS;

@RestController
@RequestMapping(PATH_PASSAGENS)
public class PassagemController {
    private final PassagemService passagemService;

    public PassagemController(PassagemService passagemService) {
        this.passagemService = passagemService;
    }

    @PostMapping("/comprar")
    public ResponseEntity<PassagemResponse> comprar(@Valid @RequestBody PassagemRequest request){
        PassagemResponse response = passagemService.comprarPassagem(request);
        return ResponseEntity.created(URI.create(PATH_PASSAGENS + "/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassagemResponse> buscar(@PathVariable Long id){
        PassagemResponse response = passagemService.buscarPassagem(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PassagemResponse>> listar(){
        List<PassagemResponse> response = passagemService.listarPassagens();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/cancelar/{id}")
    public ResponseEntity<PassagemResponse> cancelar(@PathVariable Long id){
        PassagemResponse response = passagemService.cancelarPassagem(id);
        return ResponseEntity.ok(response);
    }
}
