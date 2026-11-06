package com.uniaccounts;

import com.uniaccounts.audit.AuditLogger;
import com.uniaccounts.model.StudentAccount;
import com.uniaccounts.prototype.AccountTemplate;
import com.uniaccounts.repository.AccountRepository;

public class Main {
    public static void main(String[] args) {
        AccountRepository repo = AccountRepository.getInstance();

        System.out.println("=== 1) Builder: build one account ===");
        StudentAccount a1 = new StudentAccount.Builder("20240001", "Yacine Bensalem", "y.bensalem@univ.dz")
                .level("L2").major("Computer Science").scholarship(true)
                .addCourse("Algorithms").addCourse("Databases").build();
        System.out.println(a1);
        repo.save(a1);

        System.out.println("\n=== 2) Builder: validation errors ===");
        try { new StudentAccount.Builder("20240002", "Nadia", "no-at-sign").build(); }
        catch (IllegalArgumentException e) { System.out.println("Rejected: " + e.getMessage()); }
        try { new StudentAccount.Builder("20240002", "Nadia", "n@univ.dz").level("L9").build(); }
        catch (IllegalArgumentException e) { System.out.println("Rejected: " + e.getMessage()); }

        System.out.println("\n=== 3) Singleton: same instance everywhere ===");
        System.out.println("Same repository? " + (repo == AccountRepository.getInstance()));
        System.out.println("Same logger?     " + (AuditLogger.getInstance() == AuditLogger.getInstance()));
        try { repo.save(a1); }
        catch (IllegalStateException e) { System.out.println("Rejected: " + e.getMessage()); }

        System.out.println("\n=== 4) Prototype: cohort template ===");
        StudentAccount proto = new StudentAccount.Builder("TEMPLATE", "Template", "t@univ.dz")
                .level("L2").major("Computer Science")
                .addCourse("Algorithms").addCourse("Databases").addCourse("Networks").build();
        AccountTemplate l2cs = new AccountTemplate(proto);

        StudentAccount s1 = l2cs.createFor("20240010", "Aicha Benali", "aicha@univ.dz");
        StudentAccount s2 = l2cs.createFor("20240011", "Hoda Triqui", "hoda@univ.dz");
        repo.save(s1);
        repo.save(s2);

        // Deep copy proof: changing s1's courses must not affect s2 or the template
        s1.getCourses().add("Extra course");
        System.out.println("s1 courses:    " + s1.getCourses());
        System.out.println("s2 courses:    " + s2.getCourses());
        System.out.println("proto courses: " + proto.getCourses());

        System.out.println("\n=== 5) Summary ===");
        System.out.println("Accounts in repository: " + repo.count());
        System.out.println("Audit entries: " + AuditLogger.getInstance().getEntries().size());
    }
}