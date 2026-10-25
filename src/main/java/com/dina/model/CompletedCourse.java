package com.dina.model;

import java.util.Objects;

/**
 * Represents a course completed by a student.
 */
public final class CompletedCourse {

    /** Identifier of the completed course. */
    private String courseId;

    /** Term when the course was taken (e.g., FALL2025). */
    private String term;

    /** Letter grade earned (e.g., A, B+, P). */
    private String grade;

    /** Number of credits earned. */
    private int credits;

    /** Default constructor for deserialization. */
    public CompletedCourse() {
    }

    /**
     * Constructs a completed course record.
     *
     * @param courseId course identifier
     * @param term academic term
     * @param grade letter grade
     * @param credits credit count
     */
    public CompletedCourse(
            final String courseId,
            final String term,
            final String grade,
            final int credits
    ) {
        this.courseId = courseId;
        this.term = term;
        this.grade = grade;
        this.credits = credits;
    }

    /**
     * Gets the course ID.
     *
     * @return course ID
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * Sets the course ID.
     *
     * @param courseId course ID
     */
    public void setCourseId(final String courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets the term.
     *
     * @return academic term
     */
    public String getTerm() {
        return term;
    }

    /**
     * Sets the term.
     *
     * @param term academic term
     */
    public void setTerm(final String term) {
        this.term = term;
    }

    /**
     * Gets the letter grade.
     *
     * @return letter grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Sets the letter grade.
     *
     * @param grade letter grade
     */
    public void setGrade(final String grade) {
        this.grade = grade;
    }

    /**
     * Gets credits earned.
     *
     * @return credits earned
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets credits earned.
     *
     * @param credits credits earned
     */
    public void setCredits(final int credits) {
        this.credits = credits;
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Boilerplate equals and hashCode
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CompletedCourse that = (CompletedCourse) obj;
        return credits == that.credits
                && Objects.equals(courseId, that.courseId)
                && Objects.equals(term, that.term)
                && Objects.equals(grade, that.grade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, term, grade, credits);
    }
}
