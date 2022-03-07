package com.votemanager.app.dtos;

import com.votemanager.app.models.AssociadoModel;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PautaDTO {

    private long id;

    @NotBlank
    private String assunto;

    private Integer tempoPadraoPautaSessao = 60;

    private LocalDateTime dataSessaoPautaInicio;

    private LocalDateTime dataSessaoPautaFim;

    private Boolean isOpen;

    @ManyToMany
    @JoinColumn(name = "associado_id")
    private List<AssociadoModel> associadosVotantes;

}
