package com.c4mila.travelhub_api.passagem.domain.repository;

import com.c4mila.travelhub_api.passagem.domain.model.Passagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassagemRepository extends JpaRepository<Passagem, Long> {
}
