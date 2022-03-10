package com.votemanager.app.services;

import com.votemanager.app.dtos.PautaDTO;
import com.votemanager.app.exceptions.VoteNotFoundException;
import com.votemanager.app.models.AssociadoModel;
import com.votemanager.app.models.PautaModel;
import com.votemanager.app.models.VoteModel;
import com.votemanager.app.repositories.AssociadoRepository;
import com.votemanager.app.repositories.PautaRepository;
import com.votemanager.app.repositories.VoteRepository;
import com.votemanager.app.util.AppTestsUtil;
import org.hibernate.validator.constraints.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ManageVotingServiceTest {

    @InjectMocks
    private ManageVotingService manageVotingService;

    @Mock
    private PautaService pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    AssociadoRepository associadoRepository;

    @Mock
    private VoteService voteService;

    @Mock
    VoteRepository voteRepository;

    @Mock
    EmailService emailService;

    @Mock
    ValidaCpfService validaCpfService;

    @BeforeEach
    void setUp() throws VoteNotFoundException {

        Optional<PautaModel> pautaModelOptional = Optional.of(AppTestsUtil.buildPautaModel());
        Optional<VoteModel> voteModelOptional = Optional.of(AppTestsUtil.buildVoto());

        BDDMockito.when(pautaRepository.findById(ArgumentMatchers.any()))
                .thenReturn(pautaModelOptional);

        Optional<PautaModel> pautaModelOptionalService = Optional.of(AppTestsUtil.buildPautaModel());

        BDDMockito.when(pautaService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(pautaModelOptionalService);

        BDDMockito.when(associadoRepository.existsByCpfAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(true);

        BDDMockito.when(voteRepository.findByPautaModelId(ArgumentMatchers.anyLong()))
                .thenReturn(AppTestsUtil.buildVoteModelList());

        BDDMockito.when(voteService.findByCpfAndPautaModelId(ArgumentMatchers.any(), ArgumentMatchers.anyLong()))
                .thenReturn(AppTestsUtil.buildVoto());

        BDDMockito.when(emailService.sendEmail(AppTestsUtil.buildEmail())).thenReturn(AppTestsUtil.buildEmail());

        BDDMockito.when(voteRepository.findByCpfAndPautaModelId(ArgumentMatchers.any(), ArgumentMatchers.anyLong()))
                .thenReturn(voteModelOptional);

    }

    @Test
    void processaSessaoVotacao_comSucessoTest() {

        PautaModel pautaModel = AppTestsUtil.buildPauta();
        PautaDTO pautaDTO = new PautaDTO();

        BeanUtils.copyProperties(pautaModel, pautaDTO);
        Integer idPauta = 4;

        manageVotingService.processaSessaoVotacao(pautaDTO, ArgumentMatchers.anyInt());

    }

    @Test
    void realizaVoto_comSucessoTest() {

        VoteModel voteModel = AppTestsUtil.buildVoto();
        PautaModel pautaModel = AppTestsUtil.buildPauta();
        PautaDTO pautaDTO = new PautaDTO();

        BeanUtils.copyProperties(pautaModel, pautaDTO);
        Integer idPauta = 4;

        manageVotingService.realizaVoto(voteModel);

    }

    @Test
    void validaCpfParaVotar_ableToVoteTest(){

        BDDMockito.when(validaCpfService.isAbleToVote(ArgumentMatchers.anyString())).thenReturn("ABLE_TO_VOTE");

    }

    @Test
    void validaCpfParaVotar_unableToVoteTest(){

        BDDMockito.when(validaCpfService.isAbleToVote(ArgumentMatchers.anyString())).thenReturn("UNABLE_TO_VOTE");

    }

    @Test
    void returnTrueSeAssociadosExistentesNaSessao(){

        Assertions.assertTrue(manageVotingService.validaAssociadosExistemSessao(AppTestsUtil.buildAssociadosList()));

    }

    @Test
    void returnFalseSeAssociadosNaoExistentesNaSessao(){
        Assertions.assertFalse(manageVotingService.validaAssociadosExistemSessao(null));
    }

    @Test
    void returnTrueSeRealizouVotoNaPautaCorrente(){
        String cpf = "123789456";
        Long idPauta = 4L;
        Assertions.assertTrue(manageVotingService.realizouVotoNaPautaCorrente(cpf, idPauta));
    }

    @Test
    void returnFalseSeRealizouVotoNaPautaCorrente(){

        Assertions.assertFalse(manageVotingService.realizouVotoNaPautaCorrente(null, null));
    }

    @Test
    void returnMsgWithResultOfVoting(){
        Long idPauta = 4L;
        manageVotingService.votingResult(idPauta);
    }


}
