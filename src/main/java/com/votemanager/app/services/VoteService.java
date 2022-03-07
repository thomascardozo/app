package com.votemanager.app.services;

import com.votemanager.app.exceptions.VoteNotFoundException;
import com.votemanager.app.models.AssociadoModel;
import com.votemanager.app.models.PautaModel;
import com.votemanager.app.models.VoteModel;
import com.votemanager.app.repositories.AssociadoRepository;
import com.votemanager.app.repositories.PautaRepository;
import com.votemanager.app.repositories.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VoteService {

    final PautaRepository pautaRepository;
    final AssociadoRepository associadoRepository;
    final VoteRepository voteRepository;

    private Integer tempoSessaoPersonalizado;

    public VoteService(PautaRepository pautaRepository, AssociadoRepository associadoRepository,
                       VoteRepository voteRepository) {

        this.pautaRepository = pautaRepository;
        this.associadoRepository = associadoRepository;
        this.voteRepository = voteRepository;
    }

    public  VoteModel findByCpfAndPautaModelId(String cpf, Long pautaId) throws VoteNotFoundException {
            Optional<VoteModel> response = voteRepository.findByCpfAndPautaModelId(cpf, pautaId);

            if(!response.isEmpty() || response == null){

                log.info("IMPRIMINDO VOTO RECUPERADO >>>>>>>>>>>> " + response.toString());

                return response.get();

            }
        return null;
    }

    public Page<VoteModel> findAllVotes(Pageable pageable) {

       return voteRepository.findAll(pageable);
    }

    @Transactional
    public VoteModel save(VoteModel voteModel) {
        return voteRepository.save(voteModel);
    }

    public Optional<VoteModel> findById(Long id) {
        return voteRepository.findById(id);
    }

    @Transactional
    public void delete(VoteModel voteModel) {
        voteRepository.delete(voteModel);
    }

}
