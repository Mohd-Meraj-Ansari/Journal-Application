package com.app.journal.controller;

import com.app.journal.entity.JournalEntry;
import com.app.journal.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @PostMapping()
    public JournalEntry createEntry(@RequestBody JournalEntry entry)
    {
        entry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(entry);
        return entry;
    }

    @GetMapping("/get-all-entries")
    public List<JournalEntry> getAll()
    {
        return journalEntryService.getAll();
    }

    @GetMapping("/id/{getId}")
    public JournalEntry getJournalEntry(@PathVariable ObjectId getId)
    {
        return journalEntryService.findById(getId).orElse(null);
    }

    @DeleteMapping("/id/{myid}")
    public boolean deleteJournalById(@PathVariable ObjectId myid)
    {
        journalEntryService.deleteById(myid);
        return true;
    }

    @PutMapping("/update/{myid}")
    public JournalEntry updateJournalById(@PathVariable ObjectId myid,@RequestBody JournalEntry newEntry)
    {
        JournalEntry oldjournalEntry = journalEntryService.findById(myid).orElse(null);
        if(oldjournalEntry != null)
        {
            oldjournalEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("")? newEntry.getTitle() : oldjournalEntry.getTitle());
            oldjournalEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("")? newEntry.getContent() : oldjournalEntry.getContent());
        }
        journalEntryService.saveEntry(oldjournalEntry);
        return newEntry;
    }
}
