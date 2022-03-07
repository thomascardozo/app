package com.votemanager.app.controllers;

import com.votemanager.app.dtos.AssociadoDTO;
import com.votemanager.app.models.AssociadoModel;
import com.votemanager.app.services.AssociadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("associados")
public class AssociadoController {

    final AssociadoService associadoService;

    public AssociadoController(AssociadoService associadoService) {
        this.associadoService = associadoService;
    }

    @PostMapping
    public ResponseEntity<Object> saveAssociado(@RequestBody @Valid AssociadoDTO associatedDTO){

        if(associadoService.existsByCpf(associatedDTO.getCpf())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Conflict: CPF / Id is already in use");
        }

        var associadoModel = new AssociadoModel();
        BeanUtils.copyProperties(associatedDTO, associadoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(associadoService.save(associadoModel));
    }

    @GetMapping
    public ResponseEntity<Page<AssociadoModel>> getAll(@PageableDefault(page = 0, size = 10,
            sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(associadoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneAssociated(@PathVariable(value = "id")Long id){

        Optional<AssociadoModel> associadoOptional = associadoService.findById(id);
        if(!associadoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associated not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(associadoOptional.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAssociated(@PathVariable(value = "id")Long id,
                                                   @RequestBody @Valid AssociadoDTO associadoDTO){
        Optional<AssociadoModel> associatedModelOptional = associadoService.findById(id);
        if(!associatedModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associated not found");
        }

        var associadoModel = associatedModelOptional.get();
        associadoModel.setCpf(associadoDTO.getCpf());
        associadoModel.setName(associadoDTO.getName());

        return ResponseEntity.status(HttpStatus.OK).body(associadoService.save(associadoModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAssociated(@PathVariable(value = "id")Long id){

        Optional<AssociadoModel> associatedModelOptional = associadoService.findById(id);
        if(!associatedModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associated not found");
        }
        associadoService.delete(associatedModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Associated deleted successfully");
    }

}
