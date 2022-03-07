package com.votemanager.app.controllers;


import com.votemanager.app.dtos.AssociadoDTO;
import com.votemanager.app.dtos.PautaDTO;
import com.votemanager.app.exceptions.AssociadoNotFoundException;
import com.votemanager.app.models.AssociadoModel;
import com.votemanager.app.models.PautaModel;
import com.votemanager.app.services.AssociadoService;
import com.votemanager.app.services.PautaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("pautas")
@Slf4j
public class PautaController {

    final PautaService pautaService;
    final AssociadoService associadoService;

    public PautaController(PautaService pautaService, AssociadoService associadoService) {
        this.pautaService = pautaService;
        this.associadoService = associadoService;
    }

    @PostMapping
    public ResponseEntity<Object> savePauta(@Valid @RequestBody PautaDTO pautaDTO){

        PautaModel pautaModel = new PautaModel();

            if(pautaService.existsByAssunto(pautaDTO.getAssunto())){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Conflict: Assunto is already created");
            }

            BeanUtils.copyProperties(pautaDTO, pautaModel);

            return ResponseEntity.status(HttpStatus.CREATED).body(pautaService.save(pautaModel));

    }

    @PostMapping("addAssociado/{idPauta}")
    public ResponseEntity<Object> addAssociadoPauta(@Valid @RequestBody AssociadoDTO associadoDTO,
                                                    @PathVariable(value = "idPauta")Long idPauta){

        Optional<PautaModel> pautaModelOptionalAssociatedAdded = pautaService.addAssociado(associadoDTO, idPauta);

        if(pautaModelOptionalAssociatedAdded == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Problemas ao inserir na base de dados, verificar se associado existe.");
        else
            return ResponseEntity.status(HttpStatus.CREATED).body(pautaService.save(pautaModelOptionalAssociatedAdded.get()));
    }

    @GetMapping("all")
    public ResponseEntity<Page<PautaModel>> getAll(@PageableDefault(page = 0, size = 10,
            sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findAll(pageable));
    }

    @GetMapping("allAssociados")
    public ResponseEntity<Page<AssociadoModel>> getAllAssociados(@PageableDefault(page = 0, size = 10,
            sort = "id", direction = Sort.Direction.ASC) Pageable pageable){

        Page<AssociadoModel> result = pautaService.findAllAssociados(pageable);

        if(result.equals(null)){
            throw new AssociadoNotFoundException("ASSOCIATE NOT FOUND");
        }
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findAllAssociados(pageable));
    }

    @GetMapping("associados/{id}")
    public ResponseEntity<Object> getAssociado(@PageableDefault(page = 0, size = 10,
            sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                      @PathVariable(value = "id")Long id){

        AssociadoDTO associadoDTO = pautaService.findAssociadoByIdentifier(id);

        if(associadoDTO != null)
            return ResponseEntity.status(HttpStatus.OK).body(pautaService.findAssociadoByIdentifier(id));
        else
            return ResponseEntity.status(HttpStatus.OK).body("Associated not found");

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOnePauta(@PathVariable(value = "id")Long id){
        Optional<PautaModel> pautaOptional = pautaService.findById(id);
        if(!pautaOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pauta not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pautaOptional.get());
    }

    @GetMapping("/assunto/{assunto}")
    public ResponseEntity<Object> getOnePautaByAssunto(@PathVariable(value = "assunto") String assunto){
        Optional<PautaModel> pautaByAssuntoOptional = pautaService.findByAssunto(assunto);
        if(!pautaByAssuntoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pauta not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pautaByAssuntoOptional.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePauta(@PathVariable(value = "id")Long id,
                                                   @RequestBody @Valid PautaDTO pautaDTO){
        Optional<PautaModel> pautaModelOptional = pautaService.findById(id);
        if(!pautaModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pauta not found");
        }

        var pautaModel = pautaModelOptional.get();

        pautaModel.setAssunto(pautaDTO.getAssunto());
        pautaModel.setIsOpen(pautaDTO.getIsOpen());
        pautaModel.setTempoPadraoPautaSessao(pautaDTO.getTempoPadraoPautaSessao());
        pautaModel.setAssociadosVotantes(pautaDTO.getAssociadosVotantes());
        pautaModel.setDataSessaoPautaInicio(LocalDateTime.now());
        pautaModel.setDataSessaoPautaFim(LocalDateTime.now().plusSeconds(pautaDTO.getTempoPadraoPautaSessao()));

        return ResponseEntity.status(HttpStatus.OK).body(pautaService.save(pautaModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePauta(@PathVariable(value = "id")Long id){
        Optional<PautaModel> pautaModelOptional = pautaService.findById(id);
        if(!pautaModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pauta not found");
        }
        pautaService.delete(pautaModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Pauta deleted successfully");
    }

}
