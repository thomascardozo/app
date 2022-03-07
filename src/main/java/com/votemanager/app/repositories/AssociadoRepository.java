package com.votemanager.app.repositories;

import com.votemanager.app.models.AssociadoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends JpaRepository<AssociadoModel, Long> {
    boolean existsByCpfAndName(String cpf, String name);

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndId(String cpf, Long id);
}
