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
    AssociadoRepository associadoRepository;

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
    void savePautaTest() {
        Mockito.when(pautaService.save(pautaModel)).thenReturn(AppTestsUtil.buildPautaModel());
    }

    @Test
    void searchByAssuntoPautaInexistsTest() {

        String assuntoPauta = "XYZ";
        Mockito.when(pautaService.existsByAssunto(assuntoPauta)).thenReturn(false);
    }

    @Test
    void searchByAssuntoPautaExistsTest() {

        String assuntoPauta = "XYZ";
        Mockito.when(pautaService.existsByAssunto(assuntoPauta)).thenReturn(true);
    }

    @Test
    void findAllPautasTest() {

        Pageable pageable = Pageable.ofSize(10);
        Mockito.when(pautaService.findAll(pageable)).thenReturn(AppTestsUtil.buildPageablePautas());
    }

    @Test
    void returnFindByIdPautaTest(){

        Long idPauta = 4L;

        Mockito.when(pautaService.findById(idPauta)).thenReturn(Optional.ofNullable(AppTestsUtil.buildPauta()));
    }


    @Test
    void errorInAddAssociateThatExistsInDataBaseAndInexistsInPautaTest(){

        AssociadoDTO assDTO = AppTestsUtil.buildAssociadoDTO();
        AssociadoModel assModel = new AssociadoModel();
        List<AssociadoModel> associadoModelList = AppTestsUtil.buildListaAssociadosVotantes();
        BeanUtils.copyProperties(assDTO, assModel);
        associadoModelList.add(assModel);

        PautaModel pautaOptional = new PautaModel();
        pautaOptional.setAssociadosVotantes(associadoModelList);

        Mockito.when(pautaService.addAssociado(assDTO, 4L)).thenThrow(new AssociadoNotFoundException("Erro ao inserir"));

    }

    @Test
    void addAssociateThatExistsInDataBaseAndInexistsInPautaTest(){

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

        //BDDMockito.when(pautaService.addAssociado(ArgumentMatchers.any(), ArgumentMatchers.anyLong())).thenReturn(Optional.of(pautaModel));

    }

}
