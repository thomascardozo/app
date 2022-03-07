package com.votemanager.app.services;


import com.votemanager.app.exceptions.CPFNotAbleToVoteException;
import com.votemanager.app.exceptions.IntegrationCpfException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ValidaCpfService {


    private final RestTemplate restTemplate;
    private final Environment environment;

    private final String ABLE_TO_VOTE = "ABLE_TO_VOTE";

    @Autowired
    public ValidaCpfService(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    public String isAbleToVote(String cpf) {
        try {
            String url = environment.getProperty("cpf.service.url") + cpf;

            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            var responseBodyObj = response.getBody();
            String responseStr = responseBodyObj.toString();

            JSONObject jsonObject2 = new JSONObject(responseStr);

            return jsonObject2.optString("status");
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode()== HttpStatus.NOT_FOUND)
                throw new CPFNotAbleToVoteException("UNABLE_TO_VOTE");

            throw new IntegrationCpfException("Error to access CpfService.");
        }
    }
}
