package com.votemanager.app.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "pauta")
@Data
public class PautaModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assunto;

    private Integer tempoPadraoPautaSessao = 60;

    private LocalDateTime dataSessaoPautaInicio;

    private LocalDateTime dataSessaoPautaFim;

    private Boolean isOpen;

    @ManyToMany
    @JoinColumn(name = "associado_id")
    private List<AssociadoModel> associadosVotantes;

}
