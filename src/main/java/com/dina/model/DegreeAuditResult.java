package com.dina.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Result output of a degree progress audit.
 */
public final class DegreeAuditResult {

    /** Identifier of the audited degree program. */
    private String programId;

    /** Total academic credits earned toward the degree. */
    private int creditsEarned;

    /** Total academic credits required for the degree. */
    private int creditsRequired;

    /** True if all degree requirements and credit quotas are met. */
    private boolean degreeCompleted;

    /** Completed requirements (names or course codes). */
    private List<String> completedRequirements = new ArrayList<>();

    /** Remaining unmet requirements. */
    private List<String> remainingRequirements = new ArrayList<>();

    /** Courses or categories still needed for graduation. */
    private List<String> coursesStillNeeded = new ArrayList<>();

    /** Default constructor for deserialization. */
    public DegreeAuditResult() {
    }

    /**
     * Constructs a DegreeAuditResult.
     *
     * @param programId program ID
     * @param creditsEarned earned credits
     * @param creditsRequired required credits
     * @param degreeCompleted degree completion flag
     * @param completedRequirements satisfied requirements
     * @param remainingRequirements unsatisfied requirements
     * @param coursesStillNeeded remaining needed courses
     */
    public DegreeAuditResult(
            final String programId,
            final int creditsEarned,
            final int creditsRequired,
            final boolean degreeCompleted,
            final List<String> completedRequirements,
            final List<String> remainingRequirements,
            final List<String> coursesStillNeeded
    ) {
        this.programId = programId;
        this.creditsEarned = creditsEarned;
        this.creditsRequired = creditsRequired;
        this.degreeCompleted = degreeCompleted;
        if (completedRequirements != null) {
            this.completedRequirements = new ArrayList<>(
                    completedRequirements);
        }
        if (remainingRequirements != null) {
            this.remainingRequirements = new ArrayList<>(
                    remainingRequirements);
        }
        if (coursesStillNeeded != null) {
            this.coursesStillNeeded = new ArrayList<>(coursesStillNeeded);
        }
    }

    /**
     * Gets program ID.
     *
     * @return program ID
     */
    public String getProgramId() {
        return programId;
    }

    /**
     * Sets program ID.
     *
     * @param programId program ID
     */
    public void setProgramId(final String programId) {
        this.programId = programId;
    }

    /**
     * Gets credits earned.
     *
     * @return credits earned
     */
    public int getCreditsEarned() {
        return creditsEarned;
    }

    /**
     * Sets credits earned.
     *
     * @param creditsEarned credits earned
     */
    public void setCreditsEarned(final int creditsEarned) {
        this.creditsEarned = creditsEarned;
    }

    /**
     * Gets credits required.
     *
     * @return credits required
     */
    public int getCreditsRequired() {
        return creditsRequired;
    }

    /**
     * Sets credits required.
     *
     * @param creditsRequired credits required
     */
    public void setCreditsRequired(final int creditsRequired) {
        this.creditsRequired = creditsRequired;
    }

    /**
     * Checks if degree completed.
     *
     * @return true if degree completed
     */
    public boolean isDegreeCompleted() {
        return degreeCompleted;
    }

    /**
     * Sets degree completion flag.
     *
     * @param degreeCompleted degree completion flag
     */
    public void setDegreeCompleted(final boolean degreeCompleted) {
        this.degreeCompleted = degreeCompleted;
    }

    /**
     * Gets completed requirements.
     *
     * @return completed requirements
     */
    public List<String> getCompletedRequirements() {
        return Collections.unmodifiableList(completedRequirements);
    }

    /**
     * Sets completed requirements.
     *
     * @param completedRequirements completed requirements
     */
    public void setCompletedRequirements(
            final List<String> completedRequirements) {
        this.completedRequirements = completedRequirements == null
                ? new ArrayList<>() : new ArrayList<>(completedRequirements);
    }

    /**
     * Gets remaining requirements.
     *
     * @return remaining requirements
     */
    public List<String> getRemainingRequirements() {
        return Collections.unmodifiableList(remainingRequirements);
    }

    /**
     * Sets remaining requirements.
     *
     * @param remainingRequirements remaining requirements
     */
    public void setRemainingRequirements(
            final List<String> remainingRequirements) {
        this.remainingRequirements = remainingRequirements == null
                ? new ArrayList<>() : new ArrayList<>(remainingRequirements);
    }

    /**
     * Gets courses still needed.
     *
     * @return courses still needed
     */
    public List<String> getCoursesStillNeeded() {
        return Collections.unmodifiableList(coursesStillNeeded);
    }

    /**
     * Sets courses still needed.
     *
     * @param coursesStillNeeded courses still needed
     */
    public void setCoursesStillNeeded(
            final List<String> coursesStillNeeded) {
        this.coursesStillNeeded = coursesStillNeeded == null
                ? new ArrayList<>() : new ArrayList<>(coursesStillNeeded);
    }
}
