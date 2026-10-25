package com.dina.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Request payload for validating a proposed semester schedule.
 */
public final class ScheduleValidationRequest {

    /** Proposed course IDs for the target term. */
    private List<String> courseIds = new ArrayList<>();

    /** List of courses already completed by the student. */
    private List<CompletedCourse> completedCourses = new ArrayList<>();

    /** Courses currently enrolled in by the student. */
    private List<String> currentEnrollment = new ArrayList<>();

    /** Target semester term to validate (e.g., FALL2026). */
    private String targetTerm;

    /** Default constructor for deserialization. */
    public ScheduleValidationRequest() {
    }

    /**
     * Constructs a ScheduleValidationRequest.
     *
     * @param courseIds proposed course IDs
     * @param completedCourses completed courses
     * @param currentEnrollment current enrollment course IDs
     * @param targetTerm target academic term
     */
    public ScheduleValidationRequest(
            final List<String> courseIds,
            final List<CompletedCourse> completedCourses,
            final List<String> currentEnrollment,
            final String targetTerm
    ) {
        if (courseIds != null) {
            this.courseIds = new ArrayList<>(courseIds);
        }
        if (completedCourses != null) {
            this.completedCourses = new ArrayList<>(completedCourses);
        }
        if (currentEnrollment != null) {
            this.currentEnrollment = new ArrayList<>(currentEnrollment);
        }
        this.targetTerm = targetTerm;
    }

    /**
     * Gets proposed course IDs.
     *
     * @return proposed course IDs
     */
    public List<String> getCourseIds() {
        return Collections.unmodifiableList(courseIds);
    }

    /**
     * Sets proposed course IDs.
     *
     * @param courseIds proposed course IDs
     */
    public void setCourseIds(final List<String> courseIds) {
        this.courseIds = courseIds == null
                ? new ArrayList<>() : new ArrayList<>(courseIds);
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
     * Gets current enrollment course IDs.
     *
     * @return current enrollment IDs
     */
    public List<String> getCurrentEnrollment() {
        return Collections.unmodifiableList(currentEnrollment);
    }

    /**
     * Sets current enrollment course IDs.
     *
     * @param currentEnrollment current enrollment IDs
     */
    public void setCurrentEnrollment(final List<String> currentEnrollment) {
        this.currentEnrollment = currentEnrollment == null
                ? new ArrayList<>() : new ArrayList<>(currentEnrollment);
    }

    /**
     * Gets target term.
     *
     * @return target term
     */
    public String getTargetTerm() {
        return targetTerm;
    }

    /**
     * Sets target term.
     *
     * @param targetTerm target term
     */
    public void setTargetTerm(final String targetTerm) {
        this.targetTerm = targetTerm;
    }
}
