package com.votemanager.app.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "vote")
@Data
public class VoteModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;
    private EscolhaEnum escolha;

    private Long pautaId;

    public VoteModel() {
    }

    public VoteModel(String cpf, EscolhaEnum escolha) {
        this.cpf = cpf;
        this.escolha = escolha;
    }

    public EscolhaEnum getEscolha() {
        return escolha;
    }

    public void setEscolha(EscolhaEnum escolha) {
        this.escolha = escolha;
    }
}
