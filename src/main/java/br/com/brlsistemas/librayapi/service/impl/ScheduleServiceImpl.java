package br.com.brlsistemas.librayapi.service.impl;

import br.com.brlsistemas.librayapi.model.entity.Loan;
import br.com.brlsistemas.librayapi.service.EmailService;
import br.com.brlsistemas.librayapi.service.LoanService;
import br.com.brlsistemas.librayapi.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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
