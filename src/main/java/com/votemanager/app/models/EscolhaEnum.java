package com.votemanager.app.models;

public enum EscolhaEnum {

    NAO("Não"),
    SIM("Sim");

    private String escolha;

    EscolhaEnum(String escolha) {
        this.escolha = escolha;
    }

    public String getEscolha() {
        return escolha;
    }

    public void setEscolha(String escolha) {
        this.escolha = escolha;
    }
}
