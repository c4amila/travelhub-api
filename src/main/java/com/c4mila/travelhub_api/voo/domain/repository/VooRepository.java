package com.c4mila.travelhub_api.voo.domain.repository;

import com.c4mila.travelhub_api.voo.domain.model.Voo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VooRepository extends JpaRepository<Voo, Long> {
    boolean existsByNumeroVoo(String numeroVoo);
}
