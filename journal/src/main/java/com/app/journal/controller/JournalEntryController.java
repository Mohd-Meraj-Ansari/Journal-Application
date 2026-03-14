package com.app.journal.controller;

import com.app.journal.entity.JournalEntry;
import com.app.journal.entity.User;
import com.app.journal.service.JournalEntryService;
import com.app.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @PostMapping("/add-entry")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        journalEntryService.saveEntry(entry, userName);
        return new ResponseEntity<>(entry, HttpStatus.CREATED);
    }

    @GetMapping("/get-all-entries")
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User storedUser = userService.findByUserName(userName);

        if (storedUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> allEntries = storedUser.getJournalEntries();
        if (allEntries != null && !allEntries.isEmpty()) {
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/id/{getId}")
    public ResponseEntity<?> getJournalEntry(@PathVariable ObjectId getId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(getId)).collect(Collectors.toList());
        if ((!collect.isEmpty())) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(getId);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{userName}/{myid}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId myid, @PathVariable String userName) {

        journalEntryService.deleteById(myid, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{userName}/{myid}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myid, @PathVariable String userName, @RequestBody JournalEntry newEntry) {
        JournalEntry oldjournalEntry = journalEntryService.findById(myid).orElse(null);
        if (oldjournalEntry != null) {
            oldjournalEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldjournalEntry.getTitle());
            oldjournalEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldjournalEntry.getContent());
            journalEntryService.saveEntry(oldjournalEntry);
            return new ResponseEntity<>(oldjournalEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
