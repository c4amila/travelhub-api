package com.c4mila.travelhub_api.voo.domain.repository;

import com.c4mila.travelhub_api.voo.domain.model.Voo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VooRepository extends JpaRepository<Voo, Long>, JpaSpecificationExecutor<Voo> {
    boolean existsByNumeroVoo(String numeroVoo);

    Optional<Voo> findByNumeroVoo(String numeroVoo);
}
