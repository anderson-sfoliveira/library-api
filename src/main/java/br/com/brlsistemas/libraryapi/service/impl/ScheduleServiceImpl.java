package br.com.brlsistemas.libraryapi.service.impl;

import br.com.brlsistemas.libraryapi.model.entity.Loan;
import br.com.brlsistemas.libraryapi.service.EmailService;
import br.com.brlsistemas.libraryapi.service.LoanService;
import br.com.brlsistemas.libraryapi.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private static final String CRON_LATE_LOANS = "0 0 0 1/1 * *";

    @Value("${application.mail.lateloans.message}")
    private String message;

    private final LoanService loanService;
    private final EmailService emailService;

    @Override
    @Scheduled(cron = CRON_LATE_LOANS) // http://www.cronmaker.com/
    public void sendMailToLateLoans() {
        List<Loan> allLateLoans = loanService.getAllLateLoans();
        List<String> mailList = allLateLoans.stream()
                .map(loan -> loan.getCustomerEmail())
                .collect(Collectors.toList());

        emailService.sendEmails(message, mailList);
    }
}
