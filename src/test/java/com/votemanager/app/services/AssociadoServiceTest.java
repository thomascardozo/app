package com.votemanager.app.services;

import com.votemanager.app.models.AssociadoModel;
import com.votemanager.app.repositories.AssociadoRepository;
import com.votemanager.app.util.AppTestsUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@SpringBootTest(classes = AssociadoService.class)
public class AssociadoServiceTest {

    @Autowired
    private AssociadoService associadoService;

    @MockBean
    private AssociadoRepository associadoRepository;

    AssociadoModel associado = AppTestsUtil.buildAssociado();


    @Test
    void saveAssociadoSuccessfulTest() {
        Mockito.when(associadoService.save(associado)).thenReturn(AppTestsUtil.buildAssociado());
    }

    @Test
    void searchByCpfAndNameInexists_returnsFalseTest() {

        String cpfInexistente = "78945612374";
        String nomeInexistente = "NomeFake";

        Mockito.when(associadoService.existsByCpfAndName(cpfInexistente, nomeInexistente)).thenReturn(false);
    }

    @Test
    void searchByCpfAndNameExists_returnsTrueTest() {

        String cpfExistente = "12345678936";
        String nomeExistente = "NameTest";

        Mockito.when(associadoService.existsByCpfAndName(cpfExistente, nomeExistente)).thenReturn(true);
    }

    @Test
    void searchByCpfInexistsTest_returnsFalseTest() {

        String cpfInexistente = "12345678935";
        Mockito.when(associadoService.existsByCpf(cpfInexistente)).thenReturn(false);
    }

    @Test
    void searchByCpfExistsTest_returnsTrueTest() {

        String cpfExistente = "12345678936";
        Mockito.when(associadoService.existsByCpf(cpfExistente)).thenReturn(true);
    }

    @Test
    void findAllAssociados_successfullTest() {
        Pageable pageable = Pageable.ofSize(10);

        Mockito.when(associadoService.findAll(pageable)).thenReturn(AppTestsUtil.buildPageableAssociados());

    }

    @Test
    void returnFindById_successfullTest(){

        Long idAssociado = 2L;
        Mockito.when(associadoService.findById(idAssociado)).thenReturn(Optional.ofNullable(AppTestsUtil.buildAssociado()));
    }


}
