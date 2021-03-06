package com.votemanager.app.services;


import com.votemanager.app.exceptions.AssociadoNotFoundException;
import com.votemanager.app.models.AssociadoModel;
import com.votemanager.app.repositories.AssociadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AssociadoService {

    final AssociadoRepository associatedRepository;

    public AssociadoService(AssociadoRepository associatedRepository) {
        this.associatedRepository = associatedRepository;
    }

    @Transactional
    public AssociadoModel save(AssociadoModel associatedModel) {
        return associatedRepository.save(associatedModel);
    }

    public boolean existsByCpfAndName(String cpf, String name) throws AssociadoNotFoundException{
        return associatedRepository.existsByCpfAndName(cpf, name);
    }

    public boolean existsByCpf(String cpf) throws AssociadoNotFoundException {
        Boolean associateExistsByCpf = associatedRepository.existsByCpf(cpf);
        return associateExistsByCpf;
    }

    public Page<AssociadoModel> findAll(Pageable pageable) {
        return associatedRepository.findAll(pageable);
    }

    public Optional<AssociadoModel> findById(Long id) throws AssociadoNotFoundException{
        return associatedRepository.findById(id);
    }

    @Transactional
    public void delete(AssociadoModel associatedModel) {
        associatedRepository.delete(associatedModel);
    }

}
