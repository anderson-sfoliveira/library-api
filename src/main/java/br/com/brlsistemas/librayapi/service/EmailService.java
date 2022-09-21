package br.com.brlsistemas.librayapi.service;

import java.util.List;

public interface EmailService {
    void sendEmails(String message, List<String> mailList);
}
