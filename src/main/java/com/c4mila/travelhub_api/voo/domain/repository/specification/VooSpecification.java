package com.c4mila.travelhub_api.voo.domain.repository.specification;

import com.c4mila.travelhub_api.voo.domain.model.Voo;
import org.springframework.data.jpa.domain.Specification;

public class VooSpecification {
    public static Specification<Voo> origemIgualA(String origem){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("origem")),
                        origem.toLowerCase()
                );
    }

    public static Specification<Voo> destinoIgualA(String destino){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("destino")),
                        destino.toLowerCase()
                );
    }

    public static Specification<Voo> companhiaIgualA(String companhia){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("companhia")),
                        companhia.toLowerCase()
                );
    }
}
