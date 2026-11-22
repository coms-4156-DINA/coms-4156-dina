package com.dina.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// side-by-side comparison of candidate plans.
public final class PlanComparisonResult {

    /** degree program plans were evaluated against. */
    private String programId;

    /** one evaluation per plan. */
    private List<PlanEvaluation> evaluations = new ArrayList<>();

    /** best plan id for each requested criterion. */
    private Map<String, String> bestPlanByCriterion = new LinkedHashMap<>();

    /** default constructor for deserialization. */
    public PlanComparisonResult() {
    }

    /**
     * constructs PlanComparisonResult.
     *
     * @param programId degree program id
     * @param evaluations per-plan evaluations
     * @param bestPlanByCriterion best plan id per criterion
     */
    public PlanComparisonResult(
            final String programId,
            final List<PlanEvaluation> evaluations,
            final Map<String, String> bestPlanByCriterion
    ) {
        this.programId = programId;
        setEvaluations(evaluations);
        setBestPlanByCriterion(bestPlanByCriterion);
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
     * gets per-plan evaluations.
     *
     * @return evaluations
     */
    public List<PlanEvaluation> getEvaluations() {
        return Collections.unmodifiableList(evaluations);
    }

    /**
     * sets per-plan evaluations.
     *
     * @param evaluations evaluations
     */
    public void setEvaluations(final List<PlanEvaluation> evaluations) {
        this.evaluations = (evaluations == null)
                ? new ArrayList<>() : new ArrayList<>(evaluations);
    }

    /**
     * gets best plan id per criterion.
     *
     * @return map of criterion to plan id
     */
    public Map<String, String> getBestPlanByCriterion() {
        return Collections.unmodifiableMap(bestPlanByCriterion);
    }

    /**
     * sets best plan id per criterion.
     *
     * @param bestPlanByCriterion map of criterion to plan id
     */
    public void setBestPlanByCriterion(
            final Map<String, String> bestPlanByCriterion) {
        this.bestPlanByCriterion = (bestPlanByCriterion == null)
                ? new LinkedHashMap<>()
                : new LinkedHashMap<>(bestPlanByCriterion);
    }
}
