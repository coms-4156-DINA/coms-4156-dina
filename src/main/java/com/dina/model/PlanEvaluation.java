package com.dina.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// metrics computed for single candidate plan during comparison.
public final class PlanEvaluation {

    /** id of the evaluated plan. */
    private String planId;

    /** true if every semester in plan passes validation. */
    private boolean feasible;

    /** total credits across all semesters of the plan. */
    private int totalCredits;

    /** credits scheduled in each semester, in plan order. */
    private List<Integer> creditsPerSemester = new ArrayList<>();

    /** difference between the heaviest and lightest semester. */
    private int creditSpread;

    /** issues found with messages prefixed by term. */
    private List<ValidationIssue> issues = new ArrayList<>();

    /** degree requirements satisfied after completing plan. */
    private List<String> requirementsFulfilled = new ArrayList<>();

    /** degree requirements still outstanding after plan. */
    private List<String> outstandingRequirements = new ArrayList<>();

    /** credits counted toward the degree after plan. */
    private int creditsTowardDegree;

    /** true if degree is complete after the plan. */
    private boolean degreeCompleted;

    /** default constructor for deserialization. */
    public PlanEvaluation() {
    }

    /**
     * gets plan id.
     *
     * @return plan id
     */
    public String getPlanId() {
        return planId;
    }

    /**
     * sets plan id.
     *
     * @param planId plan ID
     */
    public void setPlanId(final String planId) {
        this.planId = planId;
    }

    /**
     * checks feasibility.
     *
     * @return true if every semester is valid
     */
    public boolean isFeasible() {
        return feasible;
    }

    /**
     * sets feasibility.
     *
     * @param feasible feasibility flag
     */
    public void setFeasible(final boolean feasible) {
        this.feasible = feasible;
    }

    /**
     * gets total credits.
     *
     * @return total credits
     */
    public int getTotalCredits() {
        return totalCredits;
    }

    /**
     * sets total credits.
     *
     * @param totalCredits total credits
     */
    public void setTotalCredits(final int totalCredits) {
        this.totalCredits = totalCredits;
    }

    /**
     * gets credits per semester.
     *
     * @return credits per semester
     */
    public List<Integer> getCreditsPerSemester() {
        return Collections.unmodifiableList(creditsPerSemester);
    }

    /**
     * sets credits per semester.
     *
     * @param creditsPerSemester credits per semester
     */
    public void setCreditsPerSemester(
            final List<Integer> creditsPerSemester) {
        this.creditsPerSemester = (creditsPerSemester == null)
                ? new ArrayList<>() : new ArrayList<>(creditsPerSemester);
    }

    /**
     * gets credit spread.
     *
     * @return heaviest minus lightest semester credits
     */
    public int getCreditSpread() {
        return creditSpread;
    }

    /**
     * sets credit spread.
     *
     * @param creditSpread credit spread
     */
    public void setCreditSpread(final int creditSpread) {
        this.creditSpread = creditSpread;
    }

    /**
     * gets validation issues.
     *
     * @return validation issues
     */
    public List<ValidationIssue> getIssues() {
        return Collections.unmodifiableList(issues);
    }

    /**
     * sets validation issues.
     *
     * @param issues validation issues
     */
    public void setIssues(final List<ValidationIssue> issues) {
        this.issues = issues == null
                ? new ArrayList<>() : new ArrayList<>(issues);
    }

    /**
     * gets fulfilled requirements.
     *
     * @return fulfilled requirements
     */
    public List<String> getRequirementsFulfilled() {
        return Collections.unmodifiableList(requirementsFulfilled);
    }

    /**
     * sets fulfilled requirements.
     *
     * @param requirementsFulfilled fulfilled requirements
     */
    public void setRequirementsFulfilled(
            final List<String> requirementsFulfilled) {
        this.requirementsFulfilled = (requirementsFulfilled == null)
                ? new ArrayList<>() : new ArrayList<>(requirementsFulfilled);
    }

    /**
     * gets outstanding requirements.
     *
     * @return outstanding requirements
     */
    public List<String> getOutstandingRequirements() {
        return Collections.unmodifiableList(outstandingRequirements);
    }

    /**
     * sets outstanding requirements.
     *
     * @param outstandingRequirements outstanding requirements
     */
    public void setOutstandingRequirements(
            final List<String> outstandingRequirements) {
        this.outstandingRequirements = (outstandingRequirements == null)
                ? new ArrayList<>()
                : new ArrayList<>(outstandingRequirements);
    }

    /**
     * gets credits toward degree.
     *
     * @return credits toward degree
     */
    public int getCreditsTowardDegree() {
        return creditsTowardDegree;
    }

    /**
     * sets credits toward degree.
     *
     * @param creditsTowardDegree credits toward degree
     */
    public void setCreditsTowardDegree(final int creditsTowardDegree) {
        this.creditsTowardDegree = creditsTowardDegree;
    }

    /**
     * checks if the degree is complete after the plan.
     *
     * @return true if degree completed
     */
    public boolean isDegreeCompleted() {
        return degreeCompleted;
    }

    /**
     * sets degree completion flag.
     *
     * @param degreeCompleted degree completion flag
     */
    public void setDegreeCompleted(final boolean degreeCompleted) {
        this.degreeCompleted = degreeCompleted;
    }
}
