package com.uniaccounts.repository;

import com.uniaccounts.audit.AuditLogger;
import com.uniaccounts.model.StudentAccount;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Singleton pattern -> role: Singleton (single central repository)
public final class AccountRepository {

    private static class Holder {
        private static final AccountRepository INSTANCE = new AccountRepository();
    }

    private final Map<String, StudentAccount> accounts = new LinkedHashMap<>();

    private AccountRepository() { }

    public static AccountRepository getInstance() {
        return Holder.INSTANCE;
    }

    // The id must be unique in the repository
    public synchronized void save(StudentAccount account) {
        if (accounts.containsKey(account.getId()))
            throw new IllegalStateException("Duplicate id: " + account.getId());
        accounts.put(account.getId(), account);
        AuditLogger.getInstance().log("Account saved: " + account.getId()
                + " (" + account.getFullName() + ")");
    }

    public synchronized StudentAccount findById(String id) {
        return accounts.get(id);
    }

    public synchronized List<StudentAccount> findAll() {
        return new ArrayList<>(accounts.values());
    }

    public synchronized int count() {
        return accounts.size();
    }
}