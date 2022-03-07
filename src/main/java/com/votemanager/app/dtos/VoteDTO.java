package com.votemanager.app.dtos;

import com.votemanager.app.models.EscolhaEnum;
import com.votemanager.app.models.PautaModel;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class VoteDTO {

    private Long id;
    private String cpf;
    private EscolhaEnum escolha;
    private Long pautaId;

    public EscolhaEnum getEscolha() {
        return escolha;
    }

    public void setEscolha(EscolhaEnum escolha) {
        this.escolha = escolha;
    }
}
