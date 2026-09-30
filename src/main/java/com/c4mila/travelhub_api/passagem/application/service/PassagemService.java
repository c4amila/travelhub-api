package com.c4mila.travelhub_api.passagem.application.service;

import com.c4mila.travelhub_api.passagem.domain.model.Passagem;
import com.c4mila.travelhub_api.passagem.domain.repository.PassagemRepository;
import com.c4mila.travelhub_api.passagem.infrastructure.dto.PassagemRequest;
import com.c4mila.travelhub_api.passagem.infrastructure.dto.PassagemResponse;
import com.c4mila.travelhub_api.passagem.infrastructure.exception.AssentoIndisponivelException;
import com.c4mila.travelhub_api.voo.domain.enums.StatusVoo;
import com.c4mila.travelhub_api.voo.domain.model.Voo;
import com.c4mila.travelhub_api.voo.domain.repository.VooRepository;
import com.c4mila.travelhub_api.passagem.infrastructure.exception.VooIndisponivelException;
import com.c4mila.travelhub_api.voo.infrastructure.exception.VooNaoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
