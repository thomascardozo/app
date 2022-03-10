package com.votemanager.app.services;

import com.votemanager.app.dtos.PautaDTO;
import com.votemanager.app.exceptions.VoteNotFoundException;
import com.votemanager.app.models.PautaModel;
import com.votemanager.app.models.VoteModel;
import com.votemanager.app.repositories.AssociadoRepository;
import com.votemanager.app.repositories.PautaRepository;
import com.votemanager.app.repositories.VoteRepository;
import com.votemanager.app.util.AppTestsUtil;
import org.hibernate.validator.constraints.Email;
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

    @BeforeEach
    void setUp() throws VoteNotFoundException {

        Optional<PautaModel> pautaModelOptional = Optional.of(AppTestsUtil.buildPautaModel());
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
    }

    @Test
    void processaSessaoVotacaoComSucessoTest() {

        PautaModel pautaModel = AppTestsUtil.buildPauta();
        PautaDTO pautaDTO = new PautaDTO();

        BeanUtils.copyProperties(pautaModel, pautaDTO);
        Integer idPauta = 4;

        manageVotingService.processaSessaoVotacao(pautaDTO, ArgumentMatchers.anyInt());

    }

    @Test
    void realizaVotoComSucessoTest() throws VoteNotFoundException {

        VoteModel voteModel = AppTestsUtil.buildVoto();
        PautaModel pautaModel = AppTestsUtil.buildPauta();
        PautaDTO pautaDTO = new PautaDTO();

        BeanUtils.copyProperties(pautaModel, pautaDTO);
        Integer idPauta = 4;

        manageVotingService.realizaVoto(voteModel);

    }
}
