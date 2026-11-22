package com.dina.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// a multi-semester academic plan with ordered list of semesters.
public final class CandidatePlan {

    /** id to refer to plan in results. */
    private String planId;

    /** the semesters in chronological order. */
    private List<PlanSemester> semesters = new ArrayList<>();

    /** default constructor for deserialization. */
    public CandidatePlan() {
    }

    /**
     * constructs a CandidatePlan.
     *
     * @param planId plan identifier
     * @param semesters semesters in chronological order
     */
    public CandidatePlan(
            final String planId,
            final List<PlanSemester> semesters
    ) {
        this.planId = planId;
        setSemesters(semesters);
    }

    /**
     * gets the plan ID.
     *
     * @return plan ID
     */
    public String getPlanId() {
        return planId;
    }

    /**
     * sets the plan ID.
     *
     * @param planId plan ID
     */
    public void setPlanId(final String planId) {
        this.planId = planId;
    }

    /**
     * gets the semesters in chronological order.
     *
     * @return semesters
     */
    public List<PlanSemester> getSemesters() {
        return Collections.unmodifiableList(semesters);
    }

    /**
     * sets the semesters in chronological order.
     *
     * @param semesters semesters
     */
    public void setSemesters(final List<PlanSemester> semesters) {
        this.semesters = (semesters == null)
                ? new ArrayList<>() : new ArrayList<>(semesters);
    }
}
