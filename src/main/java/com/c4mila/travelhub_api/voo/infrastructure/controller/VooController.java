package com.c4mila.travelhub_api.voo.infrastructure.controller;

import com.c4mila.travelhub_api.voo.application.service.VooService;
import com.c4mila.travelhub_api.voo.domain.model.Voo;
import com.c4mila.travelhub_api.voo.infrastructure.dto.VooRequest;
import com.c4mila.travelhub_api.voo.infrastructure.dto.VooResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.c4mila.travelhub_api.voo.infrastructure.controller.RestConstants.PATH_VOOS;

@RestController
@RequestMapping(PATH_VOOS)
public class VooController {
    private final VooService vooService;

    public VooController(VooService vooService) {
        this.vooService = vooService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<VooResponse> cadastrar(@Valid @RequestBody VooRequest request){
        VooResponse vooResponse = vooService.cadastrarVoo(request);

        return ResponseEntity.created(URI.create(PATH_VOOS + "/" + vooResponse.id()))
                .body(vooResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VooResponse> buscar(@PathVariable("id") Long id){
        VooResponse response = vooService.buscarVoo(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VooResponse>> listar(){
        List<VooResponse> vooResponse = vooService.listarVoos();
        return ResponseEntity.ok(vooResponse);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<VooResponse>> filtrar(
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) String companhia
    ){
        List<VooResponse> vooResponse = vooService.filtrarVoos(origem, destino, companhia);

        return ResponseEntity.ok(vooResponse);
    }
}
