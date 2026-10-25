package com.dina.model;

import java.util.Objects;

/**
 * Represents a specific conflict or unmet prerequisite in a plan.
 */
public final class ValidationIssue {

    /** Type of validation issue (e.g., UNMET_PREREQUISITE). */
    private String type;

    /** Course ID causing or associated with the issue. */
    private String courseId;

    /** Secondary course ID involved in a conflict (if applicable). */
    private String conflictingCourseId;

    /** Human-readable explanation of why the check failed. */
    private String message;

    /** Default constructor for deserialization. */
    public ValidationIssue() {
    }

    /**
     * Constructs a ValidationIssue.
     *
     * @param type category of issue
     * @param courseId associated course ID
     * @param conflictingCourseId conflicting course ID
     * @param message explanatory message
     */
    public ValidationIssue(
            final String type,
            final String courseId,
            final String conflictingCourseId,
            final String message
    ) {
        this.type = type;
        this.courseId = courseId;
        this.conflictingCourseId = conflictingCourseId;
        this.message = message;
    }

    /**
     * Gets issue type.
     *
     * @return issue type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets issue type.
     *
     * @param type issue type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Gets associated course ID.
     *
     * @return course ID
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * Sets associated course ID.
     *
     * @param courseId course ID
     */
    public void setCourseId(final String courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets conflicting course ID.
     *
     * @return conflicting course ID
     */
    public String getConflictingCourseId() {
        return conflictingCourseId;
    }

    /**
     * Sets conflicting course ID.
     *
     * @param conflictingCourseId conflicting course ID
     */
    public void setConflictingCourseId(final String conflictingCourseId) {
        this.conflictingCourseId = conflictingCourseId;
    }

    /**
     * Gets explanation message.
     *
     * @return explanation message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets explanation message.
     *
     * @param message explanation message
     */
    public void setMessage(final String message) {
        this.message = message;
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
        final ValidationIssue that = (ValidationIssue) obj;
        return Objects.equals(type, that.type)
                && Objects.equals(courseId, that.courseId)
                && Objects.equals(
                    conflictingCourseId, that.conflictingCourseId)
                && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, courseId, conflictingCourseId, message);
    }
}
