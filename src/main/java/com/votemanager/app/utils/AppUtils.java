package com.votemanager.app.utils;

import com.rabbitmq.tools.json.JSONUtil;
import com.votemanager.app.services.PautaService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

@Data
public class AppUtils {

    final static String TIME_FINISHED = "TIME_FINISHED";
    final static String COUNTING = "COUNTING";

    public String timesOver;

    static private Boolean countInAction = true;

    public static String contagemTempoSessao(Integer seconds, Long id){

        Integer tempoPadrao = 60;

        final String[] contagemConcluida = {""};

        System.out.println("TEMPO PASSADO COMO PARAMETRO  >>>>>>>>>>>>>> " + seconds);

        if(seconds == null || seconds <= 0){
            seconds = tempoPadrao;
        }

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Integer finalTempoSessaoPersonalizado = seconds;

        Runnable runnable = new Runnable() {
            int countdownStarter = finalTempoSessaoPersonalizado;

            String contagemFinalizada = contagemConcluida[0];

            PautaService pautaService;

            public void run() {

                System.out.println(countdownStarter);
                countdownStarter--;

                if (countdownStarter < 0) {
                    System.out.println(TIME_FINISHED);
                    pautaService.alteraStatusSessaoParaClosed(id);

                    contagemConcluida[0] = retornaConclusao(countdownStarter);
                    scheduler.shutdown();
                }
            }
        };
        scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);

        return contagemConcluida[0];
    }

    private static String retornaConclusao(Integer countdown){
        if(countdown < 0)
            return TIME_FINISHED;
        else
            return COUNTING;
    }

}
