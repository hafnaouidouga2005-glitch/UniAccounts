package com.uniaccounts.prototype;

import com.uniaccounts.model.StudentAccount;

// Prototype pattern -> role: Client (holds the prototype and creates copies of it)
// A template = a ready-made account for a cohort (same level, major, courses)
public class AccountTemplate {

    private final StudentAccount prototype;

    public AccountTemplate(StudentAccount prototype) {
        this.prototype = prototype;
    }

    // Copy the template, then change only the student's identity
    public StudentAccount createFor(String id, String fullName, String email) {
        StudentAccount account = prototype.copy();
        account.changeIdentity(id, fullName, email);
        return account;
    }
}