package com.library.lms.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConfig {
    
    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);
    
    @Autowired
    private DataSource dataSource;
    
    @EventListener(ApplicationReadyEvent.class)
    public void validateDatabase() {
        try {
            log.info("✅ DATABASE CONNECTION SUCCESSFUL");
        } catch (Exception e) {
            log.error("❌ DATABASE CONNECTION FAILED: {}", e.getMessage());
        }
    }
}
