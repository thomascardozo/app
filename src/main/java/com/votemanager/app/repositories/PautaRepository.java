package com.votemanager.app.repositories;

import com.votemanager.app.models.PautaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PautaRepository extends JpaRepository <PautaModel, Long> {
    boolean existsByAssunto(String assunto);

    Optional<PautaModel> findByAssunto(String assunto);
}
