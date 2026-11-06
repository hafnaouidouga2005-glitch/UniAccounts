package com.uniaccounts.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Builder pattern  -> role: Product
// Prototype pattern -> role: ConcretePrototype (copy())
public class StudentAccount implements Cloneable {

    public static final Set<String> LEVELS = Set.of("L1", "L2", "L3", "M1", "M2");

    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String level;
    private String major;
    private boolean scholarship;
    private List<String> courses;

    // Private: the only way to create an account is the Builder
    private StudentAccount(Builder b) {
        this.id = b.id;
        this.fullName = b.fullName;
        this.email = b.email;
        this.phone = b.phone;
        this.level = b.level;
        this.major = b.major;
        this.scholarship = b.scholarship;
        this.courses = new ArrayList<>(b.courses);
    }

    // Prototype: ConcretePrototype.copy() (deep copy of the courses list)
    public StudentAccount copy() {
        try {
            StudentAccount clone = (StudentAccount) super.clone();
            clone.courses = new ArrayList<>(this.courses);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    // Used after copy(): changes only the student's identity
    public void changeIdentity(String id, String fullName, String email) {
        validateIdentity(id, fullName, email);
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    private static void validateIdentity(String id, String fullName, String email) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id is required and must not be empty");
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("fullName is required");
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("email must contain '@'");
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getLevel() { return level; }
    public String getMajor() { return major; }
    public boolean hasScholarship() { return scholarship; }
    public List<String> getCourses() { return courses; }

    @Override
    public String toString() {
        return "StudentAccount{id=" + id + ", name=" + fullName + ", email=" + email
                + ", phone=" + phone + ", level=" + level + ", major=" + major
                + ", scholarship=" + scholarship + ", courses=" + courses + "}";
    }

    // Builder pattern -> role: ConcreteBuilder
    public static class Builder {
        private final String id;
        private final String fullName;
        private final String email;
        private String phone = null;
        private String level = "L1";
        private String major;
        private boolean scholarship = false;
        private final List<String> courses = new ArrayList<>();

        // Mandatory fields are in the constructor
        public Builder(String id, String fullName, String email) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
        }

        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder level(String level) { this.level = level; return this; }
        public Builder major(String major) { this.major = major; return this; }
        public Builder scholarship(boolean s) { this.scholarship = s; return this; }
        public Builder addCourse(String course) { this.courses.add(course); return this; }
        // Validation happens here, before the object exists
        public StudentAccount build() {
            validateIdentity(id, fullName, email);
            if (!LEVELS.contains(level))
                throw new IllegalArgumentException("level must be one of " + LEVELS);
            return new StudentAccount(this);
        }
    }
}