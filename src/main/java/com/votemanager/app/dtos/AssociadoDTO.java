package com.votemanager.app.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AssociadoDTO {


    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String cpf;
}
