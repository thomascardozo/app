package com.votemanager.app.services;


import com.votemanager.app.dtos.AssociadoDTO;
import com.votemanager.app.exceptions.AssociadoNotFoundException;
import com.votemanager.app.models.AssociadoModel;
import com.votemanager.app.models.PautaModel;
import com.votemanager.app.repositories.AssociadoRepository;
import com.votemanager.app.repositories.PautaRepository;
import com.votemanager.app.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PautaService {

    final PautaRepository pautaRepository;
    final AssociadoRepository associadoRepository;

    public PautaService(PautaRepository pautaRepository, AssociadoRepository associadoRepository) {
        this.pautaRepository = pautaRepository;
        this.associadoRepository = associadoRepository;
    }

    @Transactional
    public PautaModel save(PautaModel pautaModel) {

        return pautaRepository.save(pautaModel);
    }

    public Optional<PautaModel> addAssociado(AssociadoDTO associadoDTO, Long idPauta){

        Optional<PautaModel> pautaOptional = pautaRepository.findById(idPauta);
        Boolean associadoExistsInDataBase = associadoRepository.existsByCpfAndId(associadoDTO.getCpf(), associadoDTO.getId());
        AssociadoModel am = new AssociadoModel();

        if(associadoExistsInDataBase){
            List<AssociadoModel> listAssociadosBanco = pautaOptional.get().getAssociadosVotantes();
            BeanUtils.copyProperties(associadoDTO, am);

            if(!associateExistsInPauta(listAssociadosBanco, associadoDTO)){
                listAssociadosBanco.add(am);
                log.info("Adicionando associado id: {} a pauta", associadoDTO.getId());
            } else
            {
                log.info("Associado id: {} ja existe na pauta", associadoDTO.getId());
                return null;
            }

            pautaOptional.get().setAssociadosVotantes(listAssociadosBanco);

            return pautaOptional;
        } else{
            log.info("Associado id: {} - cpf: {} não localizado", associadoDTO.getId(), associadoDTO.getCpf());
            return null;
        }

    }

    public boolean associateExistsInPauta(List<AssociadoModel> listAssociadosBanco, AssociadoDTO associadoDTO){

        boolean associatedExistsInPauta = false;

        for (AssociadoModel assocMod : listAssociadosBanco) {
            if(associadoDTO.getCpf().equalsIgnoreCase(assocMod.getCpf()))
                associatedExistsInPauta = true;
        }
        return associatedExistsInPauta;
    }

    public boolean existsByAssunto(String assunto) {
        return pautaRepository.existsByAssunto(assunto);
    }

    public boolean existsById(Long id) {
        return pautaRepository.existsById(id);
    }

    public Page<PautaModel> findAll(Pageable pageable) {

        return pautaRepository.findAll(pageable);

    }

    public AssociadoDTO findAssociadoByIdentifier(Long associadoIdentifier) throws AssociadoNotFoundException{
        try {
            AssociadoModel response = associadoRepository.findById(associadoIdentifier)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ASSOCIATE NOT FOUND BY GIVEN ID."));

            AssociadoDTO responseDTO;
            if (response != null) {
                responseDTO = new AssociadoDTO();
                BeanUtils.copyProperties(response, responseDTO);
            } else
                throw new AssociadoNotFoundException("ASSOCIATE NOT FOUND.");
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO).getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new AssociadoNotFoundException("ASSOCIATE NOT FOUND.");
        }
    }

    public Page<AssociadoModel> findAllAssociados(Pageable pageable) {

        return associadoRepository.findAll(pageable);

    }

    public Optional<PautaModel> findById(Long id) {
        return pautaRepository.findById(id);
    }

    public Optional<PautaModel> findByAssunto(String assunto) {
        return pautaRepository.findByAssunto(assunto);
    }

    public Boolean alteraStatusSessaoParaOpenedAndDates (Long id, LocalDateTime dataInicioVotacao, LocalDateTime dataFimVotacao){

        PautaModel pm = pautaRepository.findById(id).get();
        pm.setIsOpen(true);
        pm.setDataSessaoPautaInicio(dataInicioVotacao);
        pm.setDataSessaoPautaFim(dataFimVotacao);
        pautaRepository.save(pm);

        return pm.getIsOpen();
    }

    public Boolean alteraStatusSessaoParaClosed (Long id){

        PautaModel pm = pautaRepository.findById(id).get();
        pm.setIsOpen(false);
        pautaRepository.save(pm);

        return pm.getIsOpen();
    }

    @Transactional
    public void delete(PautaModel pautaModel) {
        pautaRepository.delete(pautaModel);
    }

}
