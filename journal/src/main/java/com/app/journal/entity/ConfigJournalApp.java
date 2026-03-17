package com.app.journal.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("config_journal_app")
@Data
@NoArgsConstructor
public class ConfigJournalApp {

    private String key;
    private String value;
}

