package com.votemanager.app.services;

import com.votemanager.app.dtos.AssociadoDTO;
import com.votemanager.app.exceptions.AssociadoNotFoundException;
import com.votemanager.app.models.AssociadoModel;
import com.votemanager.app.models.PautaModel;
import com.votemanager.app.repositories.AssociadoRepository;
import com.votemanager.app.repositories.PautaRepository;
import com.votemanager.app.util.AppTestsUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class PautaServiceTest {

    @InjectMocks
    private PautaService pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @BeforeEach
    void setUp(){

        Optional<PautaModel> pautaModelOptional = Optional.of(AppTestsUtil.buildPautaModel());

        BDDMockito.when(pautaRepository.findById(ArgumentMatchers.any()))
                .thenReturn(pautaModelOptional);

        BDDMockito.when(associadoRepository.existsByCpfAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(true);

    }

    PautaModel pautaModel = AppTestsUtil.buildPautaModel();

    @Test
    void savePauta_successfullTest() {
        Mockito.when(pautaService.save(pautaModel)).thenReturn(AppTestsUtil.buildPautaModel());
    }

    @Test
    void searchByAssuntoPautaInexists_returnFalseTest() {

        String assuntoPauta = "XYZ";
        Mockito.when(pautaService.existsByAssunto(assuntoPauta)).thenReturn(false);
    }

    @Test
    void searchByAssuntoPautaExists_returnTrueTest() {

        String assuntoPauta = "XYZ";
        Mockito.when(pautaService.existsByAssunto(assuntoPauta)).thenReturn(true);
    }

    @Test
    void returnsFindAllPautas_successfullTest() {

        Pageable pageable = Pageable.ofSize(10);
        Mockito.when(pautaService.findAll(pageable)).thenReturn(AppTestsUtil.buildPageablePautas());
    }

    @Test
    void returnsFindByIdPauta_successfullTest(){

        Long idPauta = 4L;
        Mockito.when(pautaService.findById(idPauta)).thenReturn(Optional.ofNullable(AppTestsUtil.buildPauta()));
    }

    @Test
    void throwExceptionInAddAssociateThatExistsInDataBase_AndInexistsInPautaTest(){

        AssociadoDTO assDTO = AppTestsUtil.buildAssociadoDTO();
        AssociadoModel assModel = new AssociadoModel();

        Mockito.when(pautaService.addAssociado(assDTO, 4L)).thenThrow(new AssociadoNotFoundException("Erro ao inserir"));

    }

    @Test
    void addAssociateThatExistsInDataBase_AndInexistsInPautaTest(){

        AssociadoDTO assDTO = AppTestsUtil.buildAssociadoDTO();
        AssociadoModel assModel = new AssociadoModel();
        List<AssociadoModel> associadoModelList = AppTestsUtil.buildListaAssociadosVotantes();
        BeanUtils.copyProperties(assDTO, assModel);
        associadoModelList.add(assModel);

        PautaModel pautaModel = new PautaModel();
        pautaModel.setAssociadosVotantes(associadoModelList);

        Assertions.assertThat(pautaService.addAssociado(assDTO, 4L))
                .isNotNull()
                .isNotEmpty();

    }

}
