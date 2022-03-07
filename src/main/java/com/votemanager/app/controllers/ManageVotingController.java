package com.votemanager.app.controllers;

import com.votemanager.app.dtos.PautaDTO;
import com.votemanager.app.exceptions.VoteNotFoundException;
import com.votemanager.app.models.AssociadoModel;
import com.votemanager.app.models.PautaModel;
import com.votemanager.app.models.VoteModel;
import com.votemanager.app.services.AssociadoService;
import com.votemanager.app.services.ManageVotingService;
import com.votemanager.app.services.PautaService;
import com.votemanager.app.services.ValidaCpfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("manage-voting/v1")
@Slf4j
public class ManageVotingController {

    final PautaService pautaService;
    final AssociadoService associadoService;
    final ManageVotingService manageVoting;
    final ValidaCpfService validaCpfService;

    final static String TIME_FINISHED = "TIME_FINISHED";

    public ManageVotingController(PautaService pautaService, AssociadoService associadoService, ManageVotingService manageVoting, ValidaCpfService validaCpfService) {
        this.pautaService = pautaService;
        this.associadoService = associadoService;
        this.manageVoting = manageVoting;
        this.validaCpfService = validaCpfService;
    }

    @PostMapping("realizaVoto")
    public ResponseEntity<Object> realizaVoto(@RequestBody VoteModel voteModel) throws VoteNotFoundException {

        String votoRecebido = manageVoting.realizaVoto(voteModel);

        if(votoRecebido.equalsIgnoreCase("VOTO_REGISTRADO_SUCESSO"))
            return ResponseEntity.status(HttpStatus.CREATED).body(votoRecebido);
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).body(votoRecebido);

    }

    @GetMapping("allAssociados")
    public ResponseEntity<Page<AssociadoModel>> getAllAssociados(@PageableDefault(page = 0, size = 10,
            sort = {"id"},  direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findAllAssociados(pageable));
    }

    @PostMapping("processaSessao/{timeSessao}")
    public String processaSessao(@RequestBody @Valid PautaDTO pautaDTO,
                                 @PathVariable(value = "timeSessao")Integer timeSessao){
        log.info("Sessão Iniciada, recebendo votos.");

        pautaDTO.setDataSessaoPautaInicio(LocalDateTime.now());

        if(timeSessao > 30  && timeSessao != 60){
            pautaDTO.setDataSessaoPautaFim(pautaDTO.getDataSessaoPautaInicio().plusSeconds(timeSessao));
        } else {
            pautaDTO.setDataSessaoPautaFim(pautaDTO.getDataSessaoPautaInicio().plusSeconds(60));
            timeSessao = 60;
        }

        String resultadoContagemFinalizada = manageVoting.processaSessaoVotacao(pautaDTO, timeSessao);

        if(resultadoContagemFinalizada.equalsIgnoreCase(TIME_FINISHED)){
            manageVoting.setSessaoEstaAberta(false);
            return "Sessão realizada com sucesso e encerrada";
        } else
            return "Sessão de votação em andamento.";
    }

    @PostMapping("processaSessao")
    public String processaSessao(@RequestBody @Valid PautaDTO pautaDTO){
        log.info("Sessão Iniciada, recebendo votos.");

        pautaDTO.setDataSessaoPautaInicio(LocalDateTime.now());

        pautaDTO.setDataSessaoPautaFim(pautaDTO.getDataSessaoPautaInicio().plusSeconds(60));

        String resultadoContagemFinalizada = manageVoting.processaSessaoVotacao(pautaDTO, 60);

        if(resultadoContagemFinalizada.equalsIgnoreCase(TIME_FINISHED)){
            manageVoting.setSessaoEstaAberta(false);
            return "Sessão realizada com sucesso e encerrada";
        } else
            return "Sessão de votação em andamento.";
    }

    @GetMapping("resultadoPautaSessao/{idPauta}")
    public String resultadoPautaSessao(@PathVariable(value = "idPauta") Long idPauta){

        return  manageVoting.votingResult(idPauta);

    }

    @GetMapping("/{cpf}/valida")
    public ResponseEntity<Object> validaCpf(@PathVariable(value = "cpf")String cpf){

        return ResponseEntity.status(HttpStatus.OK).body(validaCpfService.isAbleToVote(cpf));
    }

    @GetMapping("all")
    public ResponseEntity<Page<PautaModel>> getAll(@PageableDefault(page = 0, size = 10,
            sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findAll(pageable));
    }
}
