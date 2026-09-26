package com.c4mila.travelhub_api.voo.application.service;

import com.c4mila.travelhub_api.voo.domain.enums.StatusVoo;
import com.c4mila.travelhub_api.voo.domain.model.Voo;
import com.c4mila.travelhub_api.voo.domain.repository.VooRepository;
import com.c4mila.travelhub_api.voo.infrastructure.dto.VooRequest;
import com.c4mila.travelhub_api.voo.infrastructure.dto.VooResponse;
import com.c4mila.travelhub_api.voo.infrastructure.exception.VooJaCadastradoException;
import com.c4mila.travelhub_api.voo.infrastructure.exception.VooNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VooServiceTest {
    @Mock
    private VooRepository vooRepository;

    @InjectMocks
    private VooService vooService;

    @Test
    @DisplayName("Deve cadastrar voo corretamente")
    void cadastrarVooCorretamente(){
        VooRequest vooRequest = VooRequest.builder()
                .numeroVoo("LA1234")
                .companhia("AZUL")
                .origem("Belo Horizonte")
                .destino("São Paulo")
                .dataHora(LocalDateTime.now().plusDays(5))
                .preco(new BigDecimal("500.00"))
                .assentosTotais(180)
                .build();

        when(vooRepository.existsByNumeroVoo("LA1234")).thenReturn(false);
        when(vooRepository.save(any(Voo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VooResponse response = vooService.cadastrarVoo(vooRequest);
        assertNotNull(response);

        assertEquals("LA1234", response.numeroVoo());
        assertEquals("AZUL", response.companhia());
        assertEquals("Belo Horizonte", response.origem());
        assertEquals("São Paulo", response.destino());
        assertEquals(new BigDecimal("500.00"), response.preco());
        assertEquals(180, response.assentosTotais());
        assertEquals(180, response.assentosDisponiveis());
        assertEquals(StatusVoo.ATIVO, response.status());

        verify(vooRepository).existsByNumeroVoo("LA1234");
        verify(vooRepository).save(any(Voo.class));
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar cadastrar um voo que já existe")
    void lancarExcecaoQuandoVooJaExistir(){
        VooRequest vooRequest = VooRequest.builder()
                .numeroVoo("LA1234")
                .companhia("AZUL")
                .origem("Belo Horizonte")
                .destino("São Paulo")
                .dataHora(LocalDateTime.now().plusDays(5))
                .preco(new BigDecimal("500.00"))
                .assentosTotais(180)
                .build();

        when(vooRepository.existsByNumeroVoo("LA1234")).thenReturn(true);
        VooJaCadastradoException ex = assertThrows(VooJaCadastradoException.class,
                () -> vooService.cadastrarVoo(vooRequest));

        assertEquals("Já existe um voo cadastrado com este número.", ex.getMessage());
        verify(vooRepository, never()).save(any(Voo.class));
    }

    @Test
    @DisplayName("Deve buscar voo corretamente")
    void deveBuscarVooCorretamente(){
        Long id = 1L;

        Voo voo = new Voo(
                "LA1234",
                "AZUL",
                "Belo Horizonte",
                "São Paulo",
                LocalDateTime.now().plusDays(5),
                new BigDecimal("500.00"),
                180
        );

        voo.setId(id);
        when(vooRepository.findById(id)).thenReturn(Optional.of(voo));

        VooResponse response = vooService.buscarVoo(id);

        assertNotNull(response);

        assertEquals(id, response.id());
        assertEquals("LA1234", response.numeroVoo());
        assertEquals("AZUL", response.companhia());
        assertEquals("Belo Horizonte", response.origem());
        assertEquals("São Paulo", response.destino());
        assertEquals(new BigDecimal("500.00"), response.preco());
        assertEquals(180, response.assentosTotais());
        assertEquals(180, response.assentosDisponiveis());
        assertEquals(StatusVoo.ATIVO, response.status());

        verify(vooRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o voo não for encontrado")
    void lancarExcecaoQuandoVooNaoForEncontrado(){
        Long id = 99L;

        when(vooRepository.findById(id)).thenReturn(Optional.empty());

        VooNaoEncontradoException ex = assertThrows(VooNaoEncontradoException.class,
                () -> vooService.buscarVoo(id));

        assertEquals("Voo não encontrado.", ex.getMessage());
        verify(vooRepository).findById(id);
        verify(vooRepository, never()).save(any(Voo.class));
    }
}
