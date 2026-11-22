package com.dina.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PlanComparisonRequest {

    /** degree program plans are evaluated against. */
    private String programId;

    /** catalog year. */
    private Integer catalogYear;

    /** courses student has completed. */
    private List<CompletedCourse> completedCourses = new ArrayList<>();

    /** candidate plans to compare. */
    private List<CandidatePlan> plans = new ArrayList<>();

    /** comparison criteria to rank by (optional, defaults to all). */
    private List<String> criteria = new ArrayList<>();

    /** default constructor for deserialization. */
    public PlanComparisonRequest() {
    }

    /**
     * constructs a PlanComparisonRequest.
     *
     * @param programId degree program ID
     * @param catalogYear catalog year
     * @param completedCourses completed course records
     * @param plans candidate plans
     * @param criteria comparison criteria
     */
    public PlanComparisonRequest(
            final String programId,
            final Integer catalogYear,
            final List<CompletedCourse> completedCourses,
            final List<CandidatePlan> plans,
            final List<String> criteria
    ) {
        this.programId = programId;
        this.catalogYear = catalogYear;
        setCompletedCourses(completedCourses);
        setPlans(plans);
        setCriteria(criteria);
    }

    /**
     * gets program id.
     *
     * @return program id
     */
    public String getProgramId() {
        return programId;
    }

    /**
     * sets program id.
     *
     * @param programId program id
     */
    public void setProgramId(final String programId) {
        this.programId = programId;
    }

    /**
     * gets catalog year.
     *
     * @return catalog year
     */
    public Integer getCatalogYear() {
        return catalogYear;
    }

    /**
     * sets catalog year.
     *
     * @param catalogYear catalog year
     */
    public void setCatalogYear(final Integer catalogYear) {
        this.catalogYear = catalogYear;
    }

    /**
     * gets completed courses.
     *
     * @return completed courses
     */
    public List<CompletedCourse> getCompletedCourses() {
        return Collections.unmodifiableList(completedCourses);
    }

    /**
     * sets completed courses.
     *
     * @param completedCourses completed courses
     */
    public void setCompletedCourses(
            final List<CompletedCourse> completedCourses) {
        this.completedCourses = (completedCourses == null)
                ? new ArrayList<>() : new ArrayList<>(completedCourses);
    }

    /**
     * gets candidate plans.
     *
     * @return candidate plans
     */
    public List<CandidatePlan> getPlans() {
        return Collections.unmodifiableList(plans);
    }

    /**
     * sets candidate plans.
     *
     * @param plans candidate plans
     */
    public void setPlans(final List<CandidatePlan> plans) {
        this.plans = (plans == null)
                ? new ArrayList<>() : new ArrayList<>(plans);
    }

    /**
     * gets comparison criteria.
     *
     * @return comparison criteria
     */
    public List<String> getCriteria() {
        return Collections.unmodifiableList(criteria);
    }

    /**
     * sets comparison criteria.
     *
     * @param criteria comparison criteria
     */
    public void setCriteria(final List<String> criteria) {
        this.criteria = (criteria == null)
                ? new ArrayList<>() : new ArrayList<>(criteria);
    }
}
