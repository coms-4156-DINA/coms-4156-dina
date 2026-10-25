package com.dina.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Result of validating a proposed semester schedule.
 */
public final class ScheduleValidationResult {

    /** Indicates whether the proposed plan has no blocking violations. */
    private boolean valid;

    /** Total credits planned for the target semester. */
    private int totalCredits;

    /** List of conflicts, unmet prerequisites, or limit violations. */
    private List<ValidationIssue> issues = new ArrayList<>();

    /** Default constructor for deserialization. */
    public ScheduleValidationResult() {
    }

    /**
     * Constructs a ScheduleValidationResult.
     *
     * @param valid validity flag
     * @param totalCredits total planned credits
     * @param issues list of validation issues
     */
    public ScheduleValidationResult(
            final boolean valid,
            final int totalCredits,
            final List<ValidationIssue> issues
    ) {
        this.valid = valid;
        this.totalCredits = totalCredits;
        if (issues != null) {
            this.issues = new ArrayList<>(issues);
        }
    }

    /**
     * Checks if valid.
     *
     * @return true if plan is valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets validity flag.
     *
     * @param valid validity flag
     */
    public void setValid(final boolean valid) {
        this.valid = valid;
    }

    /**
     * Gets total credits.
     *
     * @return total credits
     */
    public int getTotalCredits() {
        return totalCredits;
    }

    /**
     * Sets total credits.
     *
     * @param totalCredits total credits
     */
    public void setTotalCredits(final int totalCredits) {
        this.totalCredits = totalCredits;
    }

    /**
     * Gets validation issues.
     *
     * @return list of issues
     */
    public List<ValidationIssue> getIssues() {
        return Collections.unmodifiableList(issues);
    }

    /**
     * Sets validation issues.
     *
     * @param issues list of issues
     */
    public void setIssues(final List<ValidationIssue> issues) {
        this.issues = issues == null
                ? new ArrayList<>() : new ArrayList<>(issues);
    }
}
