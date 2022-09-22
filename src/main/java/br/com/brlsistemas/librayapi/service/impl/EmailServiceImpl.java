package br.com.brlsistemas.librayapi.service.impl;

import br.com.brlsistemas.librayapi.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmails(String message, List<String> mailList) {

    }
}
