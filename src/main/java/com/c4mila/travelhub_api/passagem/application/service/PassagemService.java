package com.c4mila.travelhub_api.passagem.application.service;

import com.c4mila.travelhub_api.passagem.domain.enums.StatusPassagem;
import com.c4mila.travelhub_api.passagem.domain.model.Passagem;
import com.c4mila.travelhub_api.passagem.domain.repository.PassagemRepository;
import com.c4mila.travelhub_api.passagem.infrastructure.dto.PassagemRequest;
import com.c4mila.travelhub_api.passagem.infrastructure.dto.PassagemResponse;
import com.c4mila.travelhub_api.passagem.infrastructure.exception.AssentoIndisponivelException;
import com.c4mila.travelhub_api.passagem.infrastructure.exception.PassagemCanceladaException;
import com.c4mila.travelhub_api.passagem.infrastructure.exception.PassagemNaoEncontradaException;
import com.c4mila.travelhub_api.voo.domain.enums.StatusVoo;
import com.c4mila.travelhub_api.voo.domain.model.Voo;
import com.c4mila.travelhub_api.voo.domain.repository.VooRepository;
import com.c4mila.travelhub_api.passagem.infrastructure.exception.VooIndisponivelException;
import com.c4mila.travelhub_api.voo.infrastructure.exception.VooNaoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PassagemService {
    private final PassagemRepository passagemRepository;
    private final VooRepository vooRepository;

    public PassagemService(PassagemRepository passagemRepository, VooRepository vooRepository) {
        this.passagemRepository = passagemRepository;
        this.vooRepository = vooRepository;
    }

    @Transactional
    public PassagemResponse comprarPassagem(PassagemRequest request){
        String numeroVoo = request.numeroVoo().trim().toUpperCase();
        Voo voo = vooRepository.findByNumeroVoo(numeroVoo)
                .orElseThrow(() -> {
                    log.warn("Tentativa de comprar passagem para voo inexistente. numeroVoo={}", numeroVoo);

                    return new VooNaoEncontradoException(
                            "Voo não encontrado."
                    );
                });

        if (voo.getStatus() == StatusVoo.CANCELADO){
            log.warn("Tentativa de comprar passagem para um voo cancelado -> numeroVoo={}", numeroVoo);
            throw new VooIndisponivelException(
                    "Não é possível realizar a compra da passagem para um voo não ativo."
            );
        }
        if (voo.getAssentosDisponiveis() == 0){
            log.warn("Tentativa de comprar passagem em um voo sem assentos -> numeroVoo={}", numeroVoo);
            throw new AssentoIndisponivelException(
                    "Não há assentos disponiveis para este voo."
            );
        }

        voo.setAssentosDisponiveis(voo.getAssentosDisponiveis() - 1);

        Passagem passagem = new Passagem(voo);
        Passagem passagemComprada = passagemRepository.save(passagem);

        log.info("Passagem comprada com sucesso! -> numeroVoo={}, passagem={}", numeroVoo, passagemComprada.getId());

        return PassagemResponse.from(passagemComprada);
    }

    @Transactional(readOnly = true)
    public PassagemResponse buscarPassagem(Long id){
        Passagem passagem = passagemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Passagem não encontrada -> id={}", id);
                    return new PassagemNaoEncontradaException(
                            "Passagem não encontrada."
                    );
                });
        log.info("Passagem encontrada -> id={}", id);

        return PassagemResponse.from(passagem);
    }

    @Transactional(readOnly = true)
    public List<PassagemResponse> listarPassagens(){
        List<PassagemResponse> passagens = passagemRepository.findAll()
                .stream()
                .map(PassagemResponse::from)
                .toList();

        log.info("Listagem de passagens concluída -> {} passagens", passagens.size());
        return passagens;
    }

    @Transactional
    public PassagemResponse cancelarPassagem(Long id){
        Passagem passagem = passagemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tentativa de cancelar uma passagem que não existe -> id={}", id);
                    return new PassagemNaoEncontradaException(
                            "Passagem não encontrada."
                    );
                });

        if (passagem.getStatus() == StatusPassagem.CANCELADA){
            log.warn("Tentativa de cancelar passagem já cancelada -> id={}", id);
            throw new PassagemCanceladaException(
                    "Não é possível cancelar uma passagem que já está cancelada"
            );
        }

        Voo voo = passagem.getVooId();
        if(!voo.getDataHora().isAfter(LocalDateTime.now())){
            log.warn("Tentativa de cancelar um voo que já aconteceu -> numeroVoo={}", voo.getNumeroVoo());
            throw new PassagemCanceladaException(
                    "Não é possível cancelar uma passagem de um voo que já aconteceu."
            );
        }

        passagem.setStatus(StatusPassagem.CANCELADA);
        voo.setAssentosDisponiveis(voo.getAssentosDisponiveis() + 1);
        vooRepository.save(voo);

        log.info("Passagem cancelada com sucesso -> id={}, numeroVoo={}", id, voo.getNumeroVoo());

        passagemRepository.save(passagem);

        return PassagemResponse.from(passagem);
    }
}
