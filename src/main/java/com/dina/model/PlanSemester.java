package com.dina.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PlanSemester {

    /** for term of a semester (ex: FALL2026). */
    private String term;

    /** for courses in the semester. */
    private List<String> courseIds = new ArrayList<>();

    /** default constructor for deserialization. */
    public PlanSemester() {
    }

    /**
     * constructs a PlanSemester.
     *
     * @param term academic term
     * @param courseIds course ids scheduled in the term
     */
    public PlanSemester(final String term, final List<String> courseIds) {
        this.term = term;
        setCourseIds(courseIds);
    }

    /**
     * gets the term.
     *
     * @return academic term
     */
    public String getTerm() {
        return term;
    }

    /**
     * sets the term.
     *
     * @param term academic term
     */
    public void setTerm(final String term) {
        this.term = term;
    }

    /**
     * gets scheduled course ids.
     *
     * @return course ids
     */
    public List<String> getCourseIds() {
        return Collections.unmodifiableList(courseIds);
    }

    /**
     * sets scheduled course ids.
     *
     * @param courseIds course ids
     */
    public void setCourseIds(final List<String> courseIds) {
        this.courseIds = (courseIds == null)
                ? new ArrayList<>() : new ArrayList<>(courseIds);
    }
}
