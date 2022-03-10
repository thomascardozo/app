package com.votemanager.app.services;

import com.votemanager.app.models.AssociadoModel;
import com.votemanager.app.repositories.AssociadoRepository;
import com.votemanager.app.services.AssociadoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SpringBootTest(classes = AssociadoService.class)
public class AssociadoServiceTest {

    @Autowired
    private AssociadoService associadoService;

    @MockBean
    private AssociadoRepository associadoRepository;

    AssociadoModel associado = buildAssociado();


    @Test
    void saveAssociadoTest() {
        Mockito.when(associadoService.save(associado)).thenReturn(buildAssociado());
    }

    @Test
    void searchByCpfAndNameInexistsTest() {

        String cpfInexistente = "78945612374";
        String nomeInexistente = "NomeFake";

        Mockito.when(associadoService.existsByCpfAndName(cpfInexistente, nomeInexistente)).thenReturn(false);
    }

    @Test
    void searchByCpfAndNameExistsTest() {

        String cpfExistente = "12345678936";
        String nomeExistente = "NameTest";

        Mockito.when(associadoService.existsByCpfAndName(cpfExistente, nomeExistente)).thenReturn(true);
    }

    @Test
    void searchByCpfInexistsTest() {

        String cpfInexistente = "12345678935";

        Mockito.when(associadoService.existsByCpf(cpfInexistente)).thenReturn(false);
    }

    @Test
    void searchByCpfExistsTest() {

        String cpfExistente = "12345678936";

        Mockito.when(associadoService.existsByCpf(cpfExistente)).thenReturn(true);
    }

    @Test
    void findAllAssociadosTest() {
        Pageable pageable = Pageable.ofSize(10);

        Mockito.when(associadoService.findAll(pageable)).thenReturn(buildPageableAssociados());

    }

    @Test
    void returnFindByIdTest(){

        Long idAssociado = 2L;
        Optional<AssociadoModel> associadoModelOptional  = associadoService.findById(idAssociado);

        Mockito.when(associadoService.findById(idAssociado)).thenReturn(Optional.ofNullable(buildAssociado()));
    }

    private AssociadoModel buildAssociado(){
        AssociadoModel associadoModel = new AssociadoModel();

        associadoModel.setId(1L);
        associadoModel.setName("NameTest");
        associadoModel.setCpf("12345678936");

        return associadoModel;
    }

    private List<AssociadoModel> buildAssociadosList(){
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

    private Page<AssociadoModel> buildPageableAssociados(){

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

}
