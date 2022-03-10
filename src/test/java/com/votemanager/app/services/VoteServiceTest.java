package com.votemanager.app.services;

import com.votemanager.app.exceptions.VoteNotFoundException;
import com.votemanager.app.models.PautaModel;
import com.votemanager.app.models.VoteModel;
import com.votemanager.app.repositories.AssociadoRepository;
import com.votemanager.app.repositories.PautaRepository;
import com.votemanager.app.repositories.VoteRepository;
import com.votemanager.app.util.AppTestsUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class VoteServiceTest {

    @InjectMocks
    private VoteService voteService;

    @Mock
    VoteRepository voteRepository;
    @Mock
    PautaRepository pautaRepository;
    @Mock
    AssociadoRepository associadoRepository;

    @BeforeEach
    void setUp(){

        Optional<VoteModel> voteModelOptional = Optional.of(AppTestsUtil.buildVoto());

        BDDMockito.when(voteRepository.findByCpfAndPautaModelId(ArgumentMatchers.any(), ArgumentMatchers.anyLong()))
                .thenReturn(voteModelOptional);

        BDDMockito.when(voteRepository.findByCpfAndPautaModelId(null, null))
                .thenReturn(null);

        BDDMockito.when(associadoRepository.existsByCpfAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(true);

    }


    @Test
    void returnVoteModelByCpfAndPautaModelId_successfullTest() throws VoteNotFoundException {

        String cpf = "123789456";
        Long idPauta = 4L;

        voteService.findByCpfAndPautaModelId("cpf", idPauta);

    }

    @Test
    void returnVoteModelNullByCpfAndPautaModelId_notSuccessfullTest() throws VoteNotFoundException {

        Assertions.assertNull(voteRepository.findByCpfAndPautaModelId(null, null));

    }

    @Test
    void returnAllVotesOfVotingPauta_successfullTest(){

        Pageable pageable = Pageable.ofSize(10);
        Mockito.when(voteService.findAllVotes(pageable)).thenReturn(AppTestsUtil.buildPageableVotes());
    }

}
