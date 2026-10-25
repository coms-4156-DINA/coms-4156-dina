package com.dina.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents an academic course in the course catalog.
 */
public final class Course {

    /** Unique identifier for the course (e.g., COMSW4156). */
    private String courseId;

    /** Department offering the course (e.g., COMS). */
    private String department;

    /** Numeric course level (e.g., 4156). */
    private int courseNumber;

    /** Title of the course. */
    private String title;

    /** Number of academic credits. */
    private int credits;

    /** List of prerequisite course IDs. */
    private List<String> prerequisites = new ArrayList<>();

    /** Semesters when the course is offered (e.g., FALL, SPRING). */
    private List<String> semestersOffered = new ArrayList<>();

    /** Meeting time slots (e.g., MW 10:10-11:25). */
    private List<String> meetingTimes = new ArrayList<>();

    /** Minimum grade required to satisfy course passing standard. */
    private String minPassingGrade;

    /** Description and syllabus overview. */
    private String description;

    /** Default constructor for deserialization. */
    public Course() {
    }

    /**
     * Constructs a Course with primary attributes.
     *
     * @param courseId unique course identifier
     * @param department offering department
     * @param courseNumber course number
     * @param title course title
     * @param credits credit value
     * @param prerequisites prerequisite course IDs
     */
    public Course(
            final String courseId,
            final String department,
            final int courseNumber,
            final String title,
            final int credits,
            final List<String> prerequisites
    ) {
        this.courseId = courseId;
        this.department = department;
        this.courseNumber = courseNumber;
        this.title = title;
        this.credits = credits;
        if (prerequisites != null) {
            this.prerequisites = new ArrayList<>(prerequisites);
        }
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Fluent builder helper methods
    // -------------------------------------------------------------------------

    /**
     * Sets semesters offered and returns this course.
     *
     * @param semesters list of semesters offered
     * @return this course instance
     */
    public Course withSemestersOffered(final List<String> semesters) {
        setSemestersOffered(semesters);
        return this;
    }

    /**
     * Sets meeting times and returns this course.
     *
     * @param times list of meeting time slots
     * @return this course instance
     */
    public Course withMeetingTimes(final List<String> times) {
        setMeetingTimes(times);
        return this;
    }

    /**
     * Sets min passing grade and returns this course.
     *
     * @param grade minimum passing grade
     * @return this course instance
     */
    public Course withMinPassingGrade(final String grade) {
        this.minPassingGrade = grade;
        return this;
    }

    /**
     * Sets description and returns this course.
     *
     * @param desc course description
     * @return this course instance
     */
    public Course withDescription(final String desc) {
        this.description = desc;
        return this;
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
     * @param courseId course identifier
     */
    public void setCourseId(final String courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets the department code.
     *
     * @return department code
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department code.
     *
     * @param department department code
     */
    public void setDepartment(final String department) {
        this.department = department;
    }

    /**
     * Gets the course number.
     *
     * @return course number
     */
    public int getCourseNumber() {
        return courseNumber;
    }

    /**
     * Sets the course number.
     *
     * @param courseNumber course number
     */
    public void setCourseNumber(final int courseNumber) {
        this.courseNumber = courseNumber;
    }

    /**
     * Gets the course title.
     *
     * @return course title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the course title.
     *
     * @param title course title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Gets the credit count.
     *
     * @return credit count
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the credit count.
     *
     * @param credits credit count
     */
    public void setCredits(final int credits) {
        this.credits = credits;
    }

    /**
     * Gets the list of prerequisite IDs.
     *
     * @return list of prerequisites
     */
    public List<String> getPrerequisites() {
        return Collections.unmodifiableList(prerequisites);
    }

    /**
     * Sets the prerequisite IDs.
     *
     * @param prerequisites prerequisite IDs
     */
    public void setPrerequisites(final List<String> prerequisites) {
        this.prerequisites = prerequisites == null
                ? new ArrayList<>() : new ArrayList<>(prerequisites);
    }

    /**
     * Gets the semesters offered.
     *
     * @return list of semesters offered
     */
    public List<String> getSemestersOffered() {
        return Collections.unmodifiableList(semestersOffered);
    }

    /**
     * Sets the semesters offered.
     *
     * @param semestersOffered list of semesters offered
     */
    public void setSemestersOffered(final List<String> semestersOffered) {
        this.semestersOffered = semestersOffered == null
                ? new ArrayList<>() : new ArrayList<>(semestersOffered);
    }

    /**
     * Gets meeting time slots.
     *
     * @return list of meeting time slots
     */
    public List<String> getMeetingTimes() {
        return Collections.unmodifiableList(meetingTimes);
    }

    /**
     * Sets meeting time slots.
     *
     * @param meetingTimes meeting times
     */
    public void setMeetingTimes(final List<String> meetingTimes) {
        this.meetingTimes = meetingTimes == null
                ? new ArrayList<>() : new ArrayList<>(meetingTimes);
    }

    /**
     * Gets minimum passing grade.
     *
     * @return minimum passing grade
     */
    public String getMinPassingGrade() {
        return minPassingGrade;
    }

    /**
     * Sets minimum passing grade.
     *
     * @param minPassingGrade minimum passing grade
     */
    public void setMinPassingGrade(final String minPassingGrade) {
        this.minPassingGrade = minPassingGrade;
    }

    /**
     * Gets description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description course description
     */
    public void setDescription(final String description) {
        this.description = description;
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
        final Course course = (Course) obj;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}
