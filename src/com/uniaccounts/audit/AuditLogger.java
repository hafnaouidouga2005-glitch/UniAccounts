package com.uniaccounts.audit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Singleton pattern -> role: Singleton (single audit log for the whole program)
public final class AuditLogger {

    // Holds the single instance (created only when first needed, thread-safe)
    private static class Holder {
        private static final AuditLogger INSTANCE = new AuditLogger();
    }

    private final List<String> entries = new ArrayList<>();

    // Private constructor: nobody can write "new AuditLogger()"
    private AuditLogger() { }

    // Global access point to the single instance
    public static AuditLogger getInstance() {
        return Holder.INSTANCE;
    }

    public synchronized void log(String action) {
        String line = "[AUDIT #" + (entries.size() + 1) + "] " + action;
        entries.add(line);
        System.out.println(line);
    }

    public synchronized List<String> getEntries() {
        return Collections.unmodifiableList(new ArrayList<>(entries));
    }
}