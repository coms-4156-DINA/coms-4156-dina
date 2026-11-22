package com.dina.service;

import com.dina.model.CandidatePlan;
import com.dina.model.CompletedCourse;
import com.dina.model.DegreeAuditRequest;
import com.dina.model.DegreeAuditResult;
import com.dina.model.PlanComparisonRequest;
import com.dina.model.PlanComparisonResult;
import com.dina.model.PlanEvaluation;
import com.dina.model.PlanSemester;
import com.dina.model.ScheduleValidationRequest;
import com.dina.model.ScheduleValidationResult;
import com.dina.model.ValidationIssue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * service evaluating candidate plans side by side.
 *
 * each plan validated semester by semester, courses from
 * earlier semesters treated as completed for later ones.
 */
@Service
public final class PlanComparisonService {

    /** fewest outstanding degree requirements wins. */
    public static final String REQUIREMENTS_SATISFIED =
            "REQUIREMENTS_SATISFIED";

    /** most even credit load across semesters wins. */
    public static final String CREDIT_BALANCE = "CREDIT_BALANCE";

    /** fewest validation issues wins. */
    public static final String CONFLICTS = "CONFLICTS";

    /** assumed grade for planned courses. */
    private static final String PLANNED_GRADE = "A";

    /** puts feasible plans ahead of infeasible ones. */
    private static final Comparator<PlanEvaluation> FEASIBLE_FIRST =
            Comparator.comparing(e -> !e.isFeasible());

    /** ranking for each supported criterion. */
    private static final Map<String, Comparator<PlanEvaluation>> RANKINGS =
            buildRankings();

    /** service validating a single semester schedule. */
    private final PlanValidationService planValidationService;

    /** service auditing degree progress. */
    private final DegreeAuditService degreeAuditService;

    /**
     * constructs PlanComparisonService with the services it builds on.
     *
     * @param planValidationService semester schedule validator
     * @param degreeAuditService degree progress auditor
     */
    public PlanComparisonService(
            final PlanValidationService planValidationService,
            final DegreeAuditService degreeAuditService
    ) {
        this.planValidationService = planValidationService;
        this.degreeAuditService = degreeAuditService;
    }

    /**
     * checks whether a criterion name is supported (case-insensitive).
     *
     * @param criterion criterion name
     * @return true if the criterion can be used for ranking
     */
    public static boolean isSupportedCriterion(final String criterion) {
        return criterion != null
                && RANKINGS.containsKey(criterion.trim().toUpperCase());
    }

    /**
     * evaluates every candidate plan and picks the best per criterion.
     *
     * @param request comparison request with transcript and plans
     * @return per-plan metrics and best plan id per criterion
     * @throws IllegalArgumentException if the program is not found
     */
    public PlanComparisonResult comparePlans(
            final PlanComparisonRequest request
    ) {
        final List<PlanEvaluation> evaluations = new ArrayList<>();
        int index = 0;
        for (final CandidatePlan plan : request.getPlans()) {
            index++;
            final String planId = (plan.getPlanId() == null)
                    || plan.getPlanId().isBlank()
                    ? "PLAN_" + index : plan.getPlanId();
            evaluations.add(evaluatePlan(planId, plan, request));
        }

        final List<String> criteria = new ArrayList<>();
        for (final String criterion : request.getCriteria()) {
            criteria.add(criterion.trim().toUpperCase());
        }
        if (criteria.isEmpty()) {
            criteria.addAll(RANKINGS.keySet());
        }

        final Map<String, String> best = new LinkedHashMap<>();
        for (final String criterion : criteria) {
            best.put(criterion,
                    findBest(evaluations, RANKINGS.get(criterion)));
        }
        return new PlanComparisonResult(
                request.getProgramId(), evaluations, best);
    }

    private PlanEvaluation evaluatePlan(
            final String planId,
            final CandidatePlan plan,
            final PlanComparisonRequest request
    ) {
        // transcript grows; later semesters see
        // earlier planned courses as satisfied prerequisites.
        final List<CompletedCourse> history =
                new ArrayList<>(request.getCompletedCourses());
        final List<Integer> credits = new ArrayList<>();
        final List<ValidationIssue> issues = new ArrayList<>();

        for (final PlanSemester semester : plan.getSemesters()) {
            final ScheduleValidationResult result =
                    planValidationService.validateSchedule(
                            new ScheduleValidationRequest(
                                    semester.getCourseIds(), history,
                                    new ArrayList<>(), semester.getTerm()));
            credits.add(result.getTotalCredits());
            for (final ValidationIssue issue : result.getIssues()) {
                issues.add(new ValidationIssue(
                        issue.getType(), issue.getCourseId(),
                        issue.getConflictingCourseId(),
                        "[" + semester.getTerm() + "] "
                                + issue.getMessage()));
            }
            for (final String courseId : semester.getCourseIds()) {
                history.add(new CompletedCourse(
                        courseId, semester.getTerm(), PLANNED_GRADE, 0));
            }
        }

        final DegreeAuditResult audit =
                degreeAuditService.auditDegreeProgress(
                        new DegreeAuditRequest(
                                request.getProgramId(),
                                request.getCatalogYear(),
                                history, new ArrayList<>()));

        final PlanEvaluation evaluation = new PlanEvaluation();
        evaluation.setPlanId(planId);
        evaluation.setFeasible(issues.isEmpty());
        evaluation.setTotalCredits(
                credits.stream().mapToInt(Integer::intValue).sum());
        evaluation.setCreditsPerSemester(credits);
        evaluation.setCreditSpread(
                credits.stream().mapToInt(Integer::intValue).max().orElse(0)
                - credits.stream().mapToInt(Integer::intValue)
                        .min().orElse(0));
        evaluation.setIssues(issues);
        evaluation.setRequirementsFulfilled(
                audit.getCompletedRequirements());
        evaluation.setOutstandingRequirements(
                audit.getRemainingRequirements());
        evaluation.setCreditsTowardDegree(audit.getCreditsEarned());
        evaluation.setDegreeCompleted(audit.isDegreeCompleted());
        return evaluation;
    }

    private static String findBest(
            final List<PlanEvaluation> evaluations,
            final Comparator<PlanEvaluation> ranking
    ) {
        // ties go to the plan listed first in the request.
        PlanEvaluation best = evaluations.get(0);
        for (final PlanEvaluation candidate : evaluations) {
            if (ranking.compare(candidate, best) < 0) {
                best = candidate;
            }
        }
        return best.getPlanId();
    }

    private static Map<String, Comparator<PlanEvaluation>> buildRankings() {
        final Map<String, Comparator<PlanEvaluation>> rankings =
                new LinkedHashMap<>();
        rankings.put(REQUIREMENTS_SATISFIED, FEASIBLE_FIRST
                .thenComparingInt(e -> e.getOutstandingRequirements().size())
                .thenComparing(Comparator.comparingInt(
                        PlanEvaluation::getCreditsTowardDegree).reversed()));
        rankings.put(CREDIT_BALANCE, FEASIBLE_FIRST
                .thenComparingInt(PlanEvaluation::getCreditSpread));
        rankings.put(CONFLICTS,
                Comparator.comparingInt(e -> e.getIssues().size()));
        return rankings;
    }
}
