package com.votemanager.app.services;

import com.votemanager.app.repositories.EmailRepository;
import com.votemanager.app.util.AppTestsUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class EmailServiceTest {

    @InjectMocks
    EmailService emailService;

    @Mock
    EmailRepository emailRepository;

    @Test
    void whenSendEmail_successReturnEmailModel(){
        BDDMockito.when(emailService.sendEmail(AppTestsUtil.buildEmail())).thenReturn(AppTestsUtil.buildEmail());
    }

    @Test
    void whenSendNull_notSuccessEmailReturnsNull(){
        BDDMockito.when(emailService.sendEmail(null)).thenReturn(null);
    }
}
