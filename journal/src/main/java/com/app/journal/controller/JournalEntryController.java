package com.app.journal.controller;

import com.app.journal.entity.JournalEntry;
import com.app.journal.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry)
    {
        entry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(entry);
        return new ResponseEntity<>(entry, HttpStatus.CREATED);
    }

    @GetMapping("/get-all-entries")
    public ResponseEntity<?> getAll()
    {
        List<JournalEntry> allEntries = journalEntryService.getAll();
        if(allEntries != null && !allEntries.isEmpty())
        {
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/id/{getId}")
    public ResponseEntity<?> getJournalEntry(@PathVariable ObjectId getId)
    {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(getId);
        if(journalEntry.isPresent())
        {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/id/{myid}")
    public boolean deleteJournalById(@PathVariable ObjectId myid)
    {
        journalEntryService.deleteById(myid);
        return true;
    }

    @PutMapping("/update/{myid}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myid,@RequestBody JournalEntry newEntry)
    {
        JournalEntry oldjournalEntry = journalEntryService.findById(myid).orElse(null);
        if(oldjournalEntry != null)
        {
            oldjournalEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("")? newEntry.getTitle() : oldjournalEntry.getTitle());
            oldjournalEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("")? newEntry.getContent() : oldjournalEntry.getContent());
            journalEntryService.saveEntry(oldjournalEntry);
            return new ResponseEntity<>(oldjournalEntry,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
