package com.app.journal.scheduler;

import com.app.journal.entity.JournalEntry;
import com.app.journal.entity.User;
import com.app.journal.repository.UserRepositoryImpl;
import com.app.journal.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserScheduler {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Scheduled(cron = "0 * * * * *")
    public void fetchUsersAndSendMail() {
        System.out.println("Scheduler started: " + LocalDateTime.now());
        try {
            List<User> users = userRepository.getUserForSentimentAnalysis();
            System.out.println("Users fetched: " + users.size());
            for (User user : users) {
                List<JournalEntry> journalEntries = user.getJournalEntries();
                List<String> filteredEntries = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getContent()).collect(Collectors.toList());
                String entry = String.join(" ", filteredEntries);
                emailService.sendEmail(user.getEmail(), "Testing purpose", "testing purpose....");
                System.out.println("Email sent ");
            }
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED:");
            e.printStackTrace();
        }
    }
}

