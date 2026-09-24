package com.c4mila.travelhub_api.voo.domain.model;

import com.c4mila.travelhub_api.shared.persistence.AuditableEntity;
import com.c4mila.travelhub_api.voo.domain.enums.StatusVoo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "voo")
public class Voo extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O numero do voo é obrigatório")
    @Column(name = "numero_voo", nullable = false, unique = true, length = 20)
    private String numeroVoo;

    @NotBlank(message = "o nome da companhia é obrigatório")
    @Column(nullable = false, length = 100)
    private String companhia;

    @NotBlank(message = "a origem é obrigatória")
    @Column(nullable = false, length = 100)
    private String origem;

    @NotBlank(message = "o destino é obrigatório")
    @Column(nullable = false, length = 100)
    private String destino;

    @NotNull
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Min(1)
    @Column(name = "assentos_totais", nullable = false)
    private Integer assentosTotais;

    @Min(0)
    @Column(name = "assentos_disponiveis", nullable = false)
    private Integer assentosDisponiveis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusVoo status;

    protected Voo(){}

    public Voo(
            String numeroVoo,
            String companhia,
            String origem,
            String destino,
            LocalDateTime dataHora,
            BigDecimal preco,
            Integer assentosTotais) {
        this.numeroVoo = numeroVoo;
        this.companhia = companhia;
        this.origem = origem;
        this.destino = destino;
        this.dataHora = dataHora;
        this.preco = preco;
        this.assentosTotais = assentosTotais;
        this.assentosDisponiveis = assentosTotais;
    }

    @PrePersist
    private void aoPersistir(){
        if (status == null){
            status = StatusVoo.ATIVO;
        }
        if(assentosDisponiveis == null){
            assentosDisponiveis = assentosTotais;
        }
    }
}
