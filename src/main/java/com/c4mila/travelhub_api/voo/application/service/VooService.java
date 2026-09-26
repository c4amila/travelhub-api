package com.c4mila.travelhub_api.voo.application.service;

import com.c4mila.travelhub_api.voo.domain.enums.StatusVoo;
import com.c4mila.travelhub_api.voo.domain.repository.specification.VooSpecification;
import com.c4mila.travelhub_api.voo.infrastructure.dto.AtualizarVooRequest;
import com.c4mila.travelhub_api.voo.infrastructure.dto.VooRequest;
import com.c4mila.travelhub_api.voo.domain.model.Voo;
import com.c4mila.travelhub_api.voo.domain.repository.VooRepository;
import com.c4mila.travelhub_api.voo.infrastructure.dto.VooResponse;
import com.c4mila.travelhub_api.voo.infrastructure.exception.VooCanceladoException;
import com.c4mila.travelhub_api.voo.infrastructure.exception.VooJaCadastradoException;
import com.c4mila.travelhub_api.voo.infrastructure.exception.VooNaoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    public VooResponse buscarVoo(Long id){
        Voo voo = vooRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Voo não encontrado. id={}", id);
                    return new VooNaoEncontradoException(
                            "Voo não encontrado."
                    );
                });

        log.info("Voo encontrado. id={}, numeroVoo={}", voo.getId(), voo.getNumeroVoo());

        return VooResponse.from(voo);
    }

    @Transactional(readOnly = true)
    public List<VooResponse> listarVoos(){

        List<VooResponse> voos = vooRepository.findAll()
                .stream()
                .map(VooResponse::from)
                .toList();

        log.info("Listagem de voos concluída. {} voos", voos.size());

        return voos;
    }

    @Transactional(readOnly = true)
    public List<VooResponse> filtrarVoos(String origem, String destino, String companhia){

        log.info("Filtrando voos -> origem={}, destino={}, companhia={}", origem, destino, companhia);

        Specification<Voo> specification = (root, query, criteriaBuilder)
                                            -> criteriaBuilder.conjunction();

        if (origem != null && !origem.isBlank()){
            specification = specification.and(
                    VooSpecification.origemIgualA(origem.trim())
            );
        }
        if (destino != null && !destino.isBlank()){
            specification = specification.and(
                    VooSpecification.destinoIgualA(destino.trim())
            );
        }
        if (companhia != null && !companhia.isBlank()){
            specification = specification.and(
                    VooSpecification.companhiaIgualA(companhia.trim())
            );
        }

        List<VooResponse> voos = vooRepository.findAll(specification)
                .stream()
                .map(VooResponse::from)
                .toList();

        log.info("Filtragem de voos concluída -> {} voos", voos.size());

        return voos;
    }

    @Transactional
    public VooResponse atualizarVoo(Long id, AtualizarVooRequest request) {
        Voo voo = vooRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Tentativa de atualizar coo inexistente -> id={}", id);
                    return new VooNaoEncontradoException(
                            "Voo não encontrado."
                    );
                });

        if (request.companhia() != null){
            voo.setCompanhia(request.companhia().trim());
        }
        if (request.origem() != null){
            voo.setOrigem(request.origem().trim());
        }
        if (request.destino() != null){
            voo.setDestino(request.destino().trim());
        }
        if (request.dataHora() != null){
            voo.setDataHora(request.dataHora());
        }
        if (request.preco() != null){
            voo.setPreco(request.preco());
        }

        Voo vooAtualizado = vooRepository.save(voo);
        log.info(
                "Voo atualizado com sucesso -> id={}, numeroVoo={}",
                vooAtualizado.getId(),
                vooAtualizado.getNumeroVoo()
        );

        return VooResponse.from(vooAtualizado);
    }

    @Transactional
    public VooResponse cancelarVoo(String numeroVoo){
        Voo voo = vooRepository.findByNumeroVoo(numeroVoo.trim().toUpperCase()).orElseThrow(
                () -> {
                    log.warn("Tentativa de cancelamento de voo inexistente -> numeroVoo={}", numeroVoo);
                    return new VooNaoEncontradoException(
                            "Voo não encontrado."
                    );
                });
        if (voo.getStatus() == StatusVoo.CANCELADO){
            log.warn("Tentative de cancelar um voo já cancelado -> numeroVoo={}", numeroVoo);

            throw new VooCanceladoException(
                    "Voo já está cancelado."
            );
        }
        if (!voo.getDataHora().isAfter(LocalDateTime.now())){
            log.warn("Tentativa de cancelar voo com data antiga -> numeroVoo={}", numeroVoo);

            throw new VooCanceladoException(
                    "Não é possível cancelar um voo data antiga."
            );
        }

        voo.setStatus(StatusVoo.CANCELADO);
        Voo vooCancelado = vooRepository.save(voo);

        log.info("Voo cancelado com sucesso! numeroVoo={}", vooCancelado.getNumeroVoo());

        return VooResponse.from(vooCancelado);

    }

    @Transactional
    public VooResponse excluirVoo(Long id){

    }
}
