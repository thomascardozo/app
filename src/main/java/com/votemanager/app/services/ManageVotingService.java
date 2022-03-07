package com.votemanager.app.services;

import com.votemanager.app.dtos.PautaDTO;
import com.votemanager.app.exceptions.VoteNotFoundException;
import com.votemanager.app.models.*;
import com.votemanager.app.repositories.AssociadoRepository;
import com.votemanager.app.repositories.VoteRepository;
import com.votemanager.app.utils.AppUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Data
@Slf4j
public class ManageVotingService {

    final AssociadoRepository associadoRepository;
    final VoteRepository voteRepository;
    final ValidaCpfService validaCpfService;
    final PautaService pautaService;
    final EmailService emailService;

    Boolean sessaoEstaAberta = false;
    private final String ABLE_TO_VOTE = "ABLE_TO_VOTE";
    private final String UNABLE_TO_VOTE = "UNABLE_TO_VOTE";
    final static String TIME_FINISHED = "TIME_FINISHED";
    final static String VOTO_REGISTRADO_SUCESSO = "VOTO_REGISTRADO_SUCESSO";
    final static String SESSAO_FECHADA = "SESSAO_FECHADA";
    final static String VOTO_JA_REALIZADO = "VOTO_JA_REALIZADO";
    final static String PROBLEMA_PROCESSAR_VOTO = "PROBLEMA_PROCESSAR_VOTO";
    final static String VOTO_SIM = "SIM";
    final static String VOTO_NAO = "NAO";

    public ManageVotingService(AssociadoRepository associadoRepository, VoteRepository voteRepository, ValidaCpfService validaCpfService, PautaService pautaService, EmailService emailService) {
        this.pautaService = pautaService;
        this.associadoRepository = associadoRepository;
        this.voteRepository = voteRepository;
        this.validaCpfService = validaCpfService;
        this.emailService = emailService;
    }

    public String processaSessaoVotacao(PautaDTO pautaDTO, Integer timeSessao){

        String contadorInAction = "";

        Boolean associadosSessaoExistem = validaAssociadosExistemSessao(pautaDTO.getAssociadosVotantes());
        Boolean pautaExists = pautaService.existsById(pautaDTO.getId());

        if(pautaExists && associadosSessaoExistem){

            PautaModel pauta = pautaService.findById(pautaDTO.getId()).get();

            sessaoEstaAberta = pautaService.alteraStatusSessaoParaOpenedAndDates(pauta.getId(), pautaDTO.getDataSessaoPautaInicio(), pautaDTO.getDataSessaoPautaFim());

            log.info("IMPRIMINDO STATUS DA SESSÃO ::::::::>>>>>> " + pauta.getIsOpen());

            contadorInAction = AppUtils.contagemTempoSessao(timeSessao, pautaDTO.getId());

            if(contadorInAction.equalsIgnoreCase(TIME_FINISHED)){
                sessaoEstaAberta = false;
            }
            return contadorInAction;
        }

        else
            return "COUNTING";
    }


    public String realizaVoto(VoteModel voteModel) throws VoteNotFoundException{

        PautaModel pModel = pautaService.findById(voteModel.getPautaId()).get();

        if(LocalDateTime.now().isAfter(pModel.getDataSessaoPautaFim()) ){
            pModel.setIsOpen(pautaService.alteraStatusSessaoParaClosed(voteModel.getPautaId()));
        }

        Boolean statusPautaSessao = pModel.getIsOpen();

        log.info("Imprimindo cpf {} / pautaId {} / statusPautaSessao {}", voteModel.getCpf(), voteModel.getPautaId(), statusPautaSessao);

        if(statusPautaSessao && validaCpf(voteModel.getCpf()).equalsIgnoreCase(ABLE_TO_VOTE) &&
                realizouVotoNaPautaCorrente(voteModel.getCpf(), voteModel.getPautaId()) == false) {
            voteRepository.save(voteModel);
            return VOTO_REGISTRADO_SUCESSO;
        } else if(statusPautaSessao == false) {
            votingResult(pModel.getId());
            return SESSAO_FECHADA;
        } else if(!validaCpf(voteModel.getCpf()).equalsIgnoreCase(ABLE_TO_VOTE)){
            return UNABLE_TO_VOTE;
        } else if(realizouVotoNaPautaCorrente(voteModel.getCpf(), voteModel.getPautaId()) == true) {
            return VOTO_JA_REALIZADO;
        } else
            return PROBLEMA_PROCESSAR_VOTO;
    }

    private String validaCpf(String cpf) {

        return validaCpfService.isAbleToVote(cpf);

    }

    private Boolean validaAssociadosExistemSessao  (List<AssociadoModel> associadoServiceList){

        Boolean isListaAssociadosExistente = false;
        if(associadoServiceList != null){
            isListaAssociadosExistente = true;
        }

        return isListaAssociadosExistente;
    }

    private Boolean realizouVotoNaPautaCorrente(String cpf, Long idPauta){

        Optional<VoteModel> alreadyVotes = voteRepository.findByCpfAndPautaModelId(cpf,idPauta);

        if(alreadyVotes == null || alreadyVotes.isEmpty() || !alreadyVotes.isPresent())
            return false;
        else
            return true;
    }

    public String votingResult(Long id){

        PautaModel pautaModel = pautaService.findById(id).get();
        Long pautaId = pautaModel.getId();
        String assuntoVotacao = pautaModel.getAssunto();
        List<VoteModel> listVotesPauta = voteRepository.findByPautaModelId(pautaId);

        Integer sims = 0;
        Integer naos = 0;

        for (VoteModel vModel : listVotesPauta) {
            if(VOTO_SIM.equalsIgnoreCase(vModel.getEscolha().toString()))
                sims++;
            else
                naos++;

            //log.info("VALOR DA ESCOLHA >>>>>>>>>>>>>> CPF: " + vModel.getCpf() + " / ESCOLHA: " + vModel.getEscolha());

        }

        String resultadoPautaSessao = "RESULTADO DA VOTAÇÃO >>>>>>>>>>>>:\nSIM:" + sims + " / NÃO: " + naos;

        log.info(resultadoPautaSessao);

        notificaResultadoVotacaoPorEmail(resultadoPautaSessao, assuntoVotacao);

        return resultadoPautaSessao;

    }

    public EmailModel notificaResultadoVotacaoPorEmail(String resultado, String assunto){

        EmailModel montagemEmailResultado = new EmailModel();

        montagemEmailResultado.setEmailTo("tome-santos@uol.com.br");
        montagemEmailResultado.setEmailFrom("thomas.cardozo@gmail.com");
        montagemEmailResultado.setOwnerRef("owner");
        montagemEmailResultado.setSubject(assunto);
        montagemEmailResultado.setText(resultado);

        EmailModel emailModel = emailService.sendEmail(montagemEmailResultado);

        return emailModel;
    }


}
