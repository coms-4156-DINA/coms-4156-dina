package com.dina.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Request payload for degree progress audit.
 */
public final class DegreeAuditRequest {

    /** Identifier of the target degree program. */
    private String programId;

    /** Catalog year (optional, defaults to latest). */
    private Integer catalogYear;

    /** Student's completed courses and grades. */
    private List<CompletedCourse> completedCourses = new ArrayList<>();

    /** Course IDs currently in progress (optional). */
    private List<String> inProgressCourses = new ArrayList<>();

    /** Default constructor for deserialization. */
    public DegreeAuditRequest() {
    }

    /**
     * Constructs a DegreeAuditRequest.
     *
     * @param programId target degree program ID
     * @param catalogYear applicable catalog year
     * @param completedCourses completed course records
     * @param inProgressCourses currently in-progress course IDs
     */
    public DegreeAuditRequest(
            final String programId,
            final Integer catalogYear,
            final List<CompletedCourse> completedCourses,
            final List<String> inProgressCourses
    ) {
        this.programId = programId;
        this.catalogYear = catalogYear;
        if (completedCourses != null) {
            this.completedCourses = new ArrayList<>(completedCourses);
        }
        if (inProgressCourses != null) {
            this.inProgressCourses = new ArrayList<>(inProgressCourses);
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
     * Gets catalog year.
     *
     * @return catalog year
     */
    public Integer getCatalogYear() {
        return catalogYear;
    }

    /**
     * Sets catalog year.
     *
     * @param catalogYear catalog year
     */
    public void setCatalogYear(final Integer catalogYear) {
        this.catalogYear = catalogYear;
    }

    /**
     * Gets completed courses.
     *
     * @return completed courses
     */
    public List<CompletedCourse> getCompletedCourses() {
        return Collections.unmodifiableList(completedCourses);
    }

    /**
     * Sets completed courses.
     *
     * @param completedCourses completed courses
     */
    public void setCompletedCourses(
            final List<CompletedCourse> completedCourses) {
        this.completedCourses = completedCourses == null
                ? new ArrayList<>() : new ArrayList<>(completedCourses);
    }

    /**
     * Gets in-progress course IDs.
     *
     * @return list of in-progress course IDs
     */
    public List<String> getInProgressCourses() {
        return Collections.unmodifiableList(inProgressCourses);
    }

    /**
     * Sets in-progress course IDs.
     *
     * @param inProgressCourses in-progress course IDs
     */
    public void setInProgressCourses(final List<String> inProgressCourses) {
        this.inProgressCourses = inProgressCourses == null
                ? new ArrayList<>() : new ArrayList<>(inProgressCourses);
    }
}
