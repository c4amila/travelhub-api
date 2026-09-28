package com.c4mila.travelhub_api.voo.application.service;

import com.c4mila.travelhub_api.voo.domain.enums.StatusVoo;
import com.c4mila.travelhub_api.voo.domain.model.Voo;
import com.c4mila.travelhub_api.voo.domain.repository.VooRepository;
import com.c4mila.travelhub_api.voo.infrastructure.dto.AtualizarVooRequest;
import com.c4mila.travelhub_api.voo.infrastructure.dto.VooRequest;
import com.c4mila.travelhub_api.voo.infrastructure.dto.VooResponse;
import com.c4mila.travelhub_api.voo.infrastructure.exception.VooCanceladoException;
import com.c4mila.travelhub_api.voo.infrastructure.exception.VooJaCadastradoException;
import com.c4mila.travelhub_api.voo.infrastructure.exception.VooNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    @Test
    @DisplayName("Deve listar todos os voos corretamente")
    void deveListarTodosOsVoosExistentes(){
        Voo voo = new Voo(
                "LA1234",
                "AZUL",
                "Belo Horizonte",
                "São Paulo",
                LocalDateTime.now().plusDays(5),
                new BigDecimal("500.00"),
                180
        );
        Voo voo2 = new Voo(
                "GO6543",
                "GOL",
                "Brasília",
                "São Paulo",
                LocalDateTime.now().plusDays(10),
                new BigDecimal("800.00"),
                180
        );
        when(vooRepository.findAll()).thenReturn(List.of(voo, voo2));

        List<VooResponse> response = vooService.listarVoos();

        assertNotNull(response);

        assertEquals(2, response.size());

        assertEquals("LA1234", response.get(0).numeroVoo());
        assertEquals("AZUL", response.get(0).companhia());
        assertEquals("Belo Horizonte", response.get(0).origem());
        assertEquals("São Paulo", response.get(0).destino());
        assertEquals(new BigDecimal("500.00"), response.get(0).preco());

        assertEquals("GO6543", response.get(1).numeroVoo());
        assertEquals("GOL", response.get(1).companhia());
        assertEquals("Brasília", response.get(1).origem());
        assertEquals("São Paulo", response.get(1).destino());
        assertEquals(new BigDecimal("800.00"), response.get(1).preco());

        verify(vooRepository).findAll();
        verify(vooRepository,never()).save(any(Voo.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existirem voos")
    void retornarListaVaziaAoNaoExistirVoos(){
        when(vooRepository.findAll()).thenReturn(List.of());

        List<VooResponse> response = vooService.listarVoos();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(vooRepository).findAll();
    }

    @Test
    @DisplayName("Deve filtrar voos corretamente")
    void filtrarVoosCorretamente(){
        Voo voo = new Voo(
                "LA1234",
                "AZUL",
                "Belo Horizonte",
                "São Paulo",
                LocalDateTime.now().plusDays(5),
                new BigDecimal("500.00"),
                180
        );

        when(vooRepository.findAll(any(Specification.class))).thenReturn(List.of(voo));

        List<VooResponse> response = vooService.filtrarVoos(
                "Belo Horizonte",
                "São Paulo",
                "AZUL"
        );

        assertNotNull(response);
        assertEquals(1, response.size());

        assertEquals("LA1234", response.get(0).numeroVoo());
        assertEquals("Belo Horizonte", response.get(0).origem());
        assertEquals("São Paulo", response.get(0).destino());

        verify(vooRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum voo corresponder ao filtro")
    void retornarListaVaziaQuandoVooNaoCorresponderAoFiltro(){
        when(vooRepository.findAll(any(Specification.class))).thenReturn(List.of());

        List<VooResponse> response = vooService.filtrarVoos(
                "Curitiba",
                "Rio de Janeiro",
                "LATAM"
        );
        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(vooRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Deve filtrar voos somente pela origem")
    void filtrarVoosPelaOrigem(){
        Voo voo = new Voo(
                "LA1234",
                "AZUL",
                "Belo Horizonte",
                "São Paulo",
                LocalDateTime.now().plusDays(5),
                new BigDecimal("500.00"),
                180
        );

        when(vooRepository.findAll(any(Specification.class))).thenReturn(List.of(voo));

        List<VooResponse> response = vooService.filtrarVoos(
                "Belo Horizonte",
                null,
                null
        );

        assertEquals(1, response.size());
        assertEquals("Belo Horizonte", response.get(0).origem());

        verify(vooRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Deve filtrar voos somente pelo destino")
    void filtrarVoosPeloDestino(){
        Voo voo = new Voo(
                "LA1234",
                "AZUL",
                "Belo Horizonte",
                "São Paulo",
                LocalDateTime.now().plusDays(5),
                new BigDecimal("500.00"),
                180
        );

        when(vooRepository.findAll(any(Specification.class))).thenReturn(List.of(voo));

        List<VooResponse> response = vooService.filtrarVoos(
                null,
                "São Paulo",
                null
        );

        assertEquals(1, response.size());
        assertEquals("São Paulo", response.get(0).destino());

        verify(vooRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Deve filtrar voos somente pela companhia")
    void filtrarVoosPelaCompanhia(){
        Voo voo = new Voo(
                "LA1234",
                "AZUL",
                "Belo Horizonte",
                "São Paulo",
                LocalDateTime.now().plusDays(5),
                new BigDecimal("500.00"),
                180
        );

        when(vooRepository.findAll(any(Specification.class))).thenReturn(List.of(voo));

        List<VooResponse> response = vooService.filtrarVoos(
                null,
                null,
                "AZUL"
        );

        assertEquals(1, response.size());
        assertEquals("AZUL", response.get(0).companhia());

        verify(vooRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Deve atualizar múltiplos campos do voo")
    void atualizarMultiplosCamposDoVoo(){
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

        LocalDateTime novaData = LocalDateTime.now().plusDays(15);

        AtualizarVooRequest atualizarVooRequest = AtualizarVooRequest.builder()
                .origem("Recife")
                .destino("Curitiba")
                .dataHora(novaData)
                .preco(new BigDecimal("970.00"))
                .build();

        when(vooRepository.findById(id)).thenReturn(Optional.of(voo));

        when(vooRepository.save(any(Voo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VooResponse response = vooService.atualizarVoo(id, atualizarVooRequest);

        assertEquals("Recife", response.origem());
        assertEquals("Curitiba", response.destino());
        assertEquals(novaData, response.dataHora());
        assertEquals(new BigDecimal("970.00"), response.preco());

        assertEquals("AZUL", response.companhia());

        verify(vooRepository).save(voo);
    }

    @Test
    @DisplayName("Deve atualizar o preço do voo")
    void atualizarPrecoDoVoo(){
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

        AtualizarVooRequest atualizarVooRequest = AtualizarVooRequest.builder()
                .preco(new BigDecimal("970.00"))
                .build();

        when(vooRepository.findById(id)).thenReturn(Optional.of(voo));

        when(vooRepository.save(any(Voo.class))).thenAnswer(invocation -> invocation.getArgument(0));


        VooResponse response = vooService.atualizarVoo(id, atualizarVooRequest);

        assertNotNull(response);

        assertEquals(new BigDecimal("970.00"), response.preco());

        assertEquals("AZUL", response.companhia());
        assertEquals("Belo Horizonte", response.origem());
        assertEquals("São Paulo", response.destino());

        verify(vooRepository).findById(id);
        verify(vooRepository).save(voo);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualiza um voo inexistente")
    void lancarExcecaoAoAtualizarVooInexistente(){
        Long id = 99L;

        AtualizarVooRequest atualizarVooRequest = AtualizarVooRequest.builder()
                .preco(new BigDecimal("970.00"))
                .build();

        when(vooRepository.findById(id)).thenReturn(Optional.empty());

        VooNaoEncontradoException ex = assertThrows(
                VooNaoEncontradoException.class,
                () -> vooService.atualizarVoo(id, atualizarVooRequest)
        );

        assertEquals("Voo não encontrado.", ex.getMessage());

        verify(vooRepository).findById(id);
        verify(vooRepository, never()).save(any(Voo.class));
    }

    @Test
    @DisplayName("Deve cancelar um voo válido corretamente")
    void cancelarVooCorretamente(){
        Voo voo = new Voo(
                "LA1234",
                "AZUL",
                "Belo Horizonte",
                "São Paulo",
                LocalDateTime.now().plusDays(5),
                new BigDecimal("500.00"),
                180
        );

        when(vooRepository.findByNumeroVoo("LA1234")).thenReturn(Optional.of(voo));

        when(vooRepository.save(any(Voo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VooResponse response = vooService.cancelarVoo("LA1234");

        assertNotNull(response);

        assertEquals(StatusVoo.CANCELADO, response.status());
        assertEquals(StatusVoo.CANCELADO, voo.getStatus());

        verify(vooRepository).findByNumeroVoo("LA1234");
        verify(vooRepository).save(voo);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar canelar um voo inexistente")
    void lancarExcecaoAoCancelarVooInexistente(){
        when(vooRepository.findByNumeroVoo("LA1111")).thenReturn(Optional.empty());

        VooNaoEncontradoException ex = assertThrows(
                VooNaoEncontradoException.class,
                () -> vooService.cancelarVoo("LA1111")
        );

        assertEquals("Voo não encontrado.", ex.getMessage());

        verify(vooRepository).findByNumeroVoo("LA1111");
        verify(vooRepository, never()).save(any(Voo.class));
    }

    @Test
    @DisplayName("Lançar exceção ao cancelar um voo já cancelado")
    void lancarExcecaoAoCancelarVooJaCancelado(){
        Voo voo = new Voo(
                "LA1234",
                "AZUL",
                "Belo Horizonte",
                "São Paulo",
                LocalDateTime.now().plusDays(5),
                new BigDecimal("500.00"),
                180
        );
        voo.setStatus(StatusVoo.CANCELADO);

        when(vooRepository.findByNumeroVoo("LA1234")).thenReturn(Optional.of(voo));

        VooCanceladoException ex = assertThrows(
                VooCanceladoException.class,
                () -> vooService.cancelarVoo("LA1234")
        );

        assertEquals("Voo já está cancelado.", ex.getMessage());
        assertEquals(StatusVoo.CANCELADO, voo.getStatus());

        verify(vooRepository).findByNumeroVoo("LA1234");
        verify(vooRepository, never()).save(any(Voo.class));
    }

    @Test
    @DisplayName("Lançar exceção ao cancelar voo com uma data que já passou")
    void lancarExcecaoAoCancelarVooComDataAntiga(){
        Voo voo = new Voo(
                "LA1234",
                "AZUL",
                "Belo Horizonte",
                "São Paulo",
                LocalDateTime.now().minusDays(1),
                new BigDecimal("500.00"),
                180
        );

        when(vooRepository.findByNumeroVoo("LA1234")).thenReturn(Optional.of(voo));

        VooCanceladoException ex = assertThrows(
                VooCanceladoException.class,
                () -> vooService.cancelarVoo("LA1234")
        );

        assertEquals("Não é possível cancelar um voo com a data antiga.", ex.getMessage());

        verify(vooRepository, never()).save(any(Voo.class));
    }
}
