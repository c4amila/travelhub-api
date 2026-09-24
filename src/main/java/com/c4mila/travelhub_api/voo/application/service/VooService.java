package com.c4mila.travelhub_api.voo.application.service;

import com.c4mila.travelhub_api.voo.infrastructure.dto.VooRequest;
import com.c4mila.travelhub_api.voo.domain.model.Voo;
import com.c4mila.travelhub_api.voo.domain.repository.VooRepository;
import com.c4mila.travelhub_api.voo.infrastructure.dto.VooResponse;
import com.c4mila.travelhub_api.voo.infrastructure.exception.VooJaCadastradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class VooService {
    private final VooRepository vooRepository;

    public VooService(VooRepository vooRepository) {
        this.vooRepository = vooRepository;
    }

    @Transactional
    public VooResponse cadastrarVoo(VooRequest request){
        String numeroVoo = request.numeroVoo().trim().toUpperCase();

        boolean vooExiste = vooRepository.existsByNumeroVoo(request.numeroVoo());
        if (vooRepository.existsByNumeroVoo(numeroVoo)){
            log.warn("Tentativa de cadastro de voo duplicado. numeroVoo={}", numeroVoo);

            throw new VooJaCadastradoException(
                    "Já existe um voo cadastrado com este número."
            );
        }

        Voo voo = new Voo(
                request.numeroVoo(),
                request.companhia().trim(),
                request.origem().trim(),
                request.destino().trim(),
                request.dataHora(),
                request.preco(),
                request.assentosTotais()
        );

        Voo vooCadastrado = vooRepository.save(voo);

        log.info("Voo cadastrado com sucesso. id={}, numeroVoo={}",
                vooCadastrado.getId(), vooCadastrado.getNumeroVoo()
        );

        return VooResponse.from(vooCadastrado);
    }
}
