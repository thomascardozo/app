package com.votemanager.app.util;

import com.votemanager.app.dtos.AssociadoDTO;
import com.votemanager.app.models.*;
import com.votemanager.app.repositories.VoteRepository;
import com.votemanager.app.services.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

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

    public static List<PautaModel> buildPautasList(){

        List<PautaModel> pautasList = new ArrayList<>();

        PautaModel p1 = new PautaModel();
        PautaModel p2 = new PautaModel();
        PautaModel p3 = new PautaModel();

        p1.setAssociadosVotantes(buildListaAssociadosVotantes());
        p1.setDataSessaoPautaInicio(LocalDateTime.now());
        p1.setDataSessaoPautaFim(LocalDateTime.now().plusSeconds(60));
        p1.setIsOpen(false);
        p1.setAssunto("Assunto Test");

        p2.setAssociadosVotantes(buildListaAssociadosVotantes());
        p2.setDataSessaoPautaInicio(LocalDateTime.now());
        p2.setDataSessaoPautaFim(LocalDateTime.now().plusSeconds(60));
        p2.setIsOpen(false);
        p2.setAssunto("Assunto Test2");

        p3.setAssociadosVotantes(buildListaAssociadosVotantes());
        p3.setDataSessaoPautaInicio(LocalDateTime.now());
        p3.setDataSessaoPautaFim(LocalDateTime.now().plusSeconds(60));
        p3.setIsOpen(false);
        p3.setAssunto("Assunto Test3");

        return pautasList;

    }

    public static Page<PautaModel> buildPageablePautas(){

        List<PautaModel> pautasList = buildPautasList();

        Page<PautaModel> pautasModels = new Page<PautaModel>() {
            @Override
            public Iterator<PautaModel> iterator() {
                return pautasList.iterator();
            }

            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<PautaModel> getContent() {
                return null;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public <U> Page<U> map(Function<? super PautaModel, ? extends U> converter) {
                return null;
            }
        };
        return pautasModels;
    }

    public static PautaModel buildPauta(){
        PautaModel pautaModel = new PautaModel();

        pautaModel.setId(4L);
        pautaModel.setAssociadosVotantes(buildListaAssociadosVotantes());
        pautaModel.setDataSessaoPautaInicio(LocalDateTime.now());
        pautaModel.setDataSessaoPautaFim(LocalDateTime.now().plusSeconds(60));
        pautaModel.setIsOpen(false);
        pautaModel.setAssunto("Assunto Test One Pauta");

        return pautaModel;
    }

    public static AssociadoModel buildAssociado(){
        AssociadoModel associadoModel = new AssociadoModel();

        associadoModel.setId(1L);
        associadoModel.setName("NameTest");
        associadoModel.setCpf("12345678936");

        return associadoModel;
    }

    public static List<AssociadoModel> buildAssociadosList(){
        List<AssociadoModel> associadosList = new ArrayList<>();

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

        associadosList.add(a1);
        associadosList.add(a2);
        associadosList.add(a3);

        return associadosList;
    }

    public static List<VoteModel> buildVoteModelList(){

        List<VoteModel> voteModels = new ArrayList<>();

        VoteModel vm1 = new VoteModel();
        VoteModel vm2 = new VoteModel();
        VoteModel vm3 = new VoteModel();

        vm1.setId(1L);
        vm1.setEscolha(EscolhaEnum.SIM);
        vm1.setPautaId(4L);
        vm1.setCpf("789456123963");

        vm2.setId(2L);
        vm2.setEscolha(EscolhaEnum.NAO);
        vm2.setPautaId(4L);
        vm2.setCpf("789456123965");

        vm3.setId(3L);
        vm3.setEscolha(EscolhaEnum.SIM);
        vm3.setPautaId(4L);
        vm3.setCpf("789456123963");



        voteModels.add(vm1);
        voteModels.add(vm2);
        voteModels.add(vm3);

        return voteModels;

    }

    public static Page<AssociadoModel> buildPageableAssociados(){

        List<AssociadoModel> associadosList = buildAssociadosList();

        Page<AssociadoModel> associadoModels = new Page<AssociadoModel>() {
            @Override
            public Iterator<AssociadoModel> iterator() {
                return associadosList.iterator();
            }

            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<AssociadoModel> getContent() {
                return null;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public <U> Page<U> map(Function<? super AssociadoModel, ? extends U> converter) {
                return null;
            }
        };
        return associadoModels;
    }

    public static VoteModel buildVoto(){

        VoteModel voteModel = new VoteModel();

        voteModel.setPautaId(4L);
        voteModel.setCpf("69088835071");
        voteModel.setEscolha(EscolhaEnum.valueOf("SIM"));
        voteModel.setId(3L);

        return voteModel;

    }


    public static EmailModel buildEmail() {

        EmailModel em = new EmailModel();

        em.setSubject("Teste");
        em.setEmailTo("xyz@email.com");
        em.setEmailFrom("abc@email.com");
        em.setText("Texto Texto Texto");
        em.setSendDateEmail(LocalDateTime.now());
        em.setOwnerRef("owner");
        em.setStatusEmail(StatusEmailEnum.SENT);

        return em;
    }
}
