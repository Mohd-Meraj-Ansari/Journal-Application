package com.app.journal.service;

import com.app.journal.entity.JournalEntry;
import com.app.journal.entity.User;
import com.app.journal.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(savedEntry);
            userService.saveUser(user);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error occured while saving " + e);
        }

    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId getId) {
        return journalEntryRepository.findById(getId);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean isRemoved = false;
        try {
            User user = userService.findByUserName(userName);
            isRemoved = user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
            if (isRemoved) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
                return isRemoved;
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("error occured while deleting the entry " + e);
        }
            return isRemoved;
    }
}
