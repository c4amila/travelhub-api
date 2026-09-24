package com.c4mila.travelhub_api.voo.application.service;

import com.c4mila.travelhub_api.voo.infrastructure.dto.VooRequest;
import com.c4mila.travelhub_api.voo.domain.model.Voo;
import com.c4mila.travelhub_api.voo.domain.repository.VooRepository;
import com.c4mila.travelhub_api.voo.infrastructure.dto.VooResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VooService {
    private final VooRepository vooRepository;

    public VooService(VooRepository vooRepository) {
        this.vooRepository = vooRepository;
    }

    @Transactional
    public VooResponse cadastrarVoo(VooRequest request){
        boolean vooExiste = vooRepository.existsByNumeroVoo(request.numeroVoo());
        if (vooExiste){
            throw new RuntimeException(
                    "Já existe um voo cadastrado com estes dados"
            );
        }

        Voo voo = new Voo(
                request.numeroVoo().trim().toUpperCase(),
                request.companhia().trim(),
                request.origem().trim(),
                request.destino().trim(),
                request.dataHora(),
                request.preco(),
                request.assentosTotais()
        );

        Voo vooCadastrado = vooRepository.save(voo);

        return new VooResponse.from(vooCadastrado);
    }
}
