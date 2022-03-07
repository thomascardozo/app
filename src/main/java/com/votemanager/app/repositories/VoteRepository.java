package com.votemanager.app.repositories;

import com.votemanager.app.models.PautaModel;
import com.votemanager.app.models.VoteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<VoteModel, Long> {

    @Query(value = "select v "
            + "from vote v "
            + "where v.cpf = :cpf "
            + "and v.pautaId = :pautaId")
    Optional<VoteModel> findByCpfAndPautaModelId(String cpf, Long pautaId);


    @Query(value = "select v "
            + "from vote v "
            + "where v.pautaId = :pautaId")
    List<VoteModel> findByPautaModelId(Long pautaId);

}
