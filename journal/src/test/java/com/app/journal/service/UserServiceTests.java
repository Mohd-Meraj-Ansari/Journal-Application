package com.app.journal.service;

import com.app.journal.entity.JournalEntry;
import com.app.journal.entity.User;
import com.app.journal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Test
    public void testFindByUserName() {
        assertNotNull(userRepository.findByUserName("ben"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "alice", "ben", "chris"
    })
    public void testgetAllJournalEntriesOfUser(String userName) {
        User storedUser = userService.findByUserName(userName);
        List<JournalEntry> allEntries = storedUser.getJournalEntries();
        assertFalse(allEntries.isEmpty());
    }
}
