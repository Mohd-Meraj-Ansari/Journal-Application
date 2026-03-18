package com.app.journal.scheduler;

import com.app.journal.entity.JournalEntry;
import com.app.journal.entity.User;
import com.app.journal.enums.Sentiment;
import com.app.journal.repository.UserRepositoryImpl;
import com.app.journal.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserScheduler {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

//   @Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendMail() {
        System.out.println("Scheduler started: " + LocalDateTime.now());
        try {
            List<User> users = userRepository.getUserForSentimentAnalysis();
            System.out.println("Users fetched: " + users.size());
            for (User user : users) {
                List<JournalEntry> journalEntries = user.getJournalEntries();
                List<Sentiment> filteredEntries = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
                Map<Sentiment,Integer> sentimentCount = new HashMap<>();

                for(Sentiment sentiment: filteredEntries){
                    if(sentiment !=null)
                        sentimentCount.put(sentiment, sentimentCount.getOrDefault(sentiment,0)+1);
                }
                Sentiment mostFrequent=null;
                int maxCount = 0;
                for (Map.Entry<Sentiment, Integer> entry : sentimentCount.entrySet()) {
                    if (entry.getValue() > maxCount) {
                        maxCount = entry.getValue();
                        mostFrequent = entry.getKey();
                    }
                }

                if (mostFrequent != null) {
                    emailService.sendEmail(user.getEmail(), "Sentiment Report for last 7 days ", "In the past week you are "+mostFrequent.toString());
                    System.out.println("Email sent ");
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED:");
            e.printStackTrace();
        }
    }
}

