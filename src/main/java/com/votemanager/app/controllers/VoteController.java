package com.votemanager.app.controllers;

import com.votemanager.app.dtos.VoteDTO;
import com.votemanager.app.exceptions.VoteNotFoundException;
import com.votemanager.app.models.PautaModel;
import com.votemanager.app.models.VoteModel;
import com.votemanager.app.services.AssociadoService;
import com.votemanager.app.services.PautaService;
import com.votemanager.app.services.VoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("votes/v1")
public class VoteController {

    final AssociadoService associadoService;
    final VoteService voteService;
    final PautaService pautaService;

    public VoteController(AssociadoService associadoService, VoteService voteService, PautaService pautaService) {
        this.associadoService = associadoService;
        this.voteService = voteService;
        this.pautaService = pautaService;
    }

    @PostMapping("add")
    public ResponseEntity<Object> saveVote(@RequestBody VoteDTO voteDTO) throws VoteNotFoundException {

        if((voteDTO.getId()) != null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Conflict: voteId already exists");
        }

        var voteModel = new VoteModel();
        BeanUtils.copyProperties(voteDTO, voteModel);
        if(voteService.findByCpfAndPautaModelId(voteModel.getCpf(), voteModel.getPautaId()) == null){
            return ResponseEntity.status(HttpStatus.CREATED).body(voteService.save(voteModel));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body("Voto já existente para esta pauta / cpf");
    }

    @GetMapping("all")
    public ResponseEntity<Page<VoteModel>> getAll(@PageableDefault(page = 0, size = 10,
            sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(voteService.findAllVotes(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneVote(@PathVariable(value = "id")Long id){
        Optional<VoteModel> pautaOptional = voteService.findById(id);
        if(!pautaOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vote not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pautaOptional.get());
    }

    @GetMapping("/cpfid/{cpf}/{pautaId}")
    public ResponseEntity<Object> getVoteByCpfAndPautaModelId(@PathVariable(value = "cpf") String cpf,
                                                              @PathVariable(value = "pautaId") Long pautaId) throws VoteNotFoundException {
        VoteModel voteByCpfAndPauta = voteService.findByCpfAndPautaModelId(cpf, pautaId);
        if(voteByCpfAndPauta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vote not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(voteByCpfAndPauta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVote(@PathVariable(value = "id")Long id){
        Optional<VoteModel> voteModelOptional = voteService.findById(id);
        if(!voteModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vote not found");
        }
        voteService.delete(voteModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Vote deleted successfully");
    }
}
