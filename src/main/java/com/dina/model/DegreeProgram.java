package com.dina.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents academic degree requirements for a specific degree track.
 */
public final class DegreeProgram {

    /** Identifier for the degree program (e.g., CS_BS). */
    private String programId;

    /** Full title of the degree program. */
    private String programName;

    /** Applicable catalog year. */
    private int catalogYear;

    /** Total credits required to complete the degree. */
    private int totalCreditsRequired;

    /** List of mandatory core course IDs. */
    private List<String> requiredCourseIds = new ArrayList<>();

    /** List of allowable elective course IDs. */
    private List<String> electiveCourseIds = new ArrayList<>();

    /** Minimum passing grade accepted for degree credit. */
    private String minPassingGrade;

    /** Default constructor for deserialization. */
    public DegreeProgram() {
    }

    /**
     * Constructs a DegreeProgram.
     *
     * @param programId degree identifier
     * @param programName full degree name
     * @param catalogYear catalog year
     * @param totalCreditsRequired required credits
     * @param requiredCourseIds mandatory course IDs
     * @param electiveCourseIds elective course IDs
     * @param minPassingGrade minimum passing grade
     */
    public DegreeProgram(
            final String programId,
            final String programName,
            final int catalogYear,
            final int totalCreditsRequired,
            final List<String> requiredCourseIds,
            final List<String> electiveCourseIds,
            final String minPassingGrade
    ) {
        this.programId = programId;
        this.programName = programName;
        this.catalogYear = catalogYear;
        this.totalCreditsRequired = totalCreditsRequired;
        if (requiredCourseIds != null) {
            this.requiredCourseIds = new ArrayList<>(requiredCourseIds);
        }
        if (electiveCourseIds != null) {
            this.electiveCourseIds = new ArrayList<>(electiveCourseIds);
        }
        this.minPassingGrade = minPassingGrade;
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
     * Gets program name.
     *
     * @return program name
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets program name.
     *
     * @param programName program name
     */
    public void setProgramName(final String programName) {
        this.programName = programName;
    }

    /**
     * Gets catalog year.
     *
     * @return catalog year
     */
    public int getCatalogYear() {
        return catalogYear;
    }

    /**
     * Sets catalog year.
     *
     * @param catalogYear catalog year
     */
    public void setCatalogYear(final int catalogYear) {
        this.catalogYear = catalogYear;
    }

    /**
     * Gets total credits required.
     *
     * @return total credits required
     */
    public int getTotalCreditsRequired() {
        return totalCreditsRequired;
    }

    /**
     * Sets total credits required.
     *
     * @param totalCreditsRequired total credits
     */
    public void setTotalCreditsRequired(final int totalCreditsRequired) {
        this.totalCreditsRequired = totalCreditsRequired;
    }

    /**
     * Gets required course IDs.
     *
     * @return list of required course IDs
     */
    public List<String> getRequiredCourseIds() {
        return Collections.unmodifiableList(requiredCourseIds);
    }

    /**
     * Sets required course IDs.
     *
     * @param requiredCourseIds required course IDs
     */
    public void setRequiredCourseIds(final List<String> requiredCourseIds) {
        this.requiredCourseIds = requiredCourseIds == null
                ? new ArrayList<>() : new ArrayList<>(requiredCourseIds);
    }

    /**
     * Gets elective course IDs.
     *
     * @return list of elective course IDs
     */
    public List<String> getElectiveCourseIds() {
        return Collections.unmodifiableList(electiveCourseIds);
    }

    /**
     * Sets elective course IDs.
     *
     * @param electiveCourseIds elective course IDs
     */
    public void setElectiveCourseIds(final List<String> electiveCourseIds) {
        this.electiveCourseIds = electiveCourseIds == null
                ? new ArrayList<>() : new ArrayList<>(electiveCourseIds);
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
        final DegreeProgram that = (DegreeProgram) obj;
        return Objects.equals(programId, that.programId)
                && catalogYear == that.catalogYear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(programId, catalogYear);
    }
}
