package com.c4mila.travelhub_api.passagem.domain.model;

import com.c4mila.travelhub_api.shared.persistence.AuditableEntity;
import com.c4mila.travelhub_api.voo.domain.model.Voo;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "passagem")
public class Passagem extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "voo_id", nullable = false)
    private Voo voo;

    @Column(name = "data_compra", nullable = false)
    private LocalDateTime dataCompra;

    @Column(name = "valor_pago", nullable = false)
    private BigDecimal valorPago;

    protected Passagem(){}

    public Passagem(Voo voo, BigDecimal valorPago) {
        this.voo = voo;
        this.dataCompra = LocalDateTime.now();
        this.valorPago = voo.getPreco();
    }
}
