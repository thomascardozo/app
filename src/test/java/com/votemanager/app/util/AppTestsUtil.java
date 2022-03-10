package com.votemanager.app.util;

import com.votemanager.app.dtos.AssociadoDTO;
import com.votemanager.app.models.AssociadoModel;
import com.votemanager.app.models.PautaModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppTestsUtil {

    public static PautaModel buildPautaModel(){

        PautaModel pm = new PautaModel();

        pm.setId(4L);
        pm.setIsOpen(true);
        pm.setDataSessaoPautaInicio(LocalDateTime.now().minusSeconds(60));
        pm.setDataSessaoPautaFim(LocalDateTime.now());
        pm.setAssunto("Teste Assunto Pauta App");
        pm.setAssociadosVotantes(buildListaAssociadosVotantes());

        return pm;
    }

    public static List<AssociadoModel> buildListaAssociadosVotantes(){
        List<AssociadoModel> associados = new ArrayList<>();

        AssociadoModel a1 = new AssociadoModel();
        AssociadoModel a2 = new AssociadoModel();
        AssociadoModel a3 = new AssociadoModel();

        a1.setId(1L);
        a1.setCpf("789456123963");
        a1.setName("Name1");

        a2.setId(2L);
        a2.setCpf("789456123965");
        a2.setName("Name2");

        a3.setId(3L);
        a3.setCpf("789456123963");
        a3.setName("Name3");

        associados.add(a1);
        associados.add(a2);
        associados.add(a3);

        return associados;
    }

    public static AssociadoDTO buildAssociadoDTO(){
        AssociadoDTO associadoDTO = new AssociadoDTO();

        associadoDTO.setId(2L);
        associadoDTO.setName("Frank Castelioni Junior");
        associadoDTO.setCpf("69088835071");

        return associadoDTO;
    }
}
