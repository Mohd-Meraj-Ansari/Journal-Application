package com.app.journal.cache;

import com.app.journal.entity.ConfigJournalApp;
import com.app.journal.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

   public Map<String,String> appCache;

    @PostConstruct
    public  void init()
    {
//        System.out.println("AppCache called");
        appCache = new HashMap<>();
        List<ConfigJournalApp> all = configJournalAppRepository.findAll();
        for(ConfigJournalApp configJournalApp :all)
        {
            appCache.put(configJournalApp.getKey(),configJournalApp.getValue());
        }
//        System.out.println("CACHE: " + appCache);
//        System.out.println("DB COUNT: " + configJournalAppRepository.count());
    }
}
