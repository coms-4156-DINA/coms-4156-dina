package com.dina.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dina.model.CandidatePlan;
import com.dina.model.CompletedCourse;
import com.dina.model.DegreeAuditResult;
import com.dina.model.PlanComparisonRequest;
import com.dina.model.PlanComparisonResult;
import com.dina.model.PlanEvaluation;
import com.dina.model.PlanSemester;
import com.dina.model.ScheduleValidationRequest;
import com.dina.model.ScheduleValidationResult;
import com.dina.model.ValidationIssue;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// -------------------------------------------------------------------------
// [AI-Assisted: Claude Opus 5.5] Plan comparison service tests
// -------------------------------------------------------------------------

/**
 * Unit tests for PlanComparisonService with mocked dependencies.
 */
@ExtendWith(MockitoExtension.class)
public final class PlanComparisonServiceTest {

    /** Mocked semester validator. */
    @Mock
    private PlanValidationService planValidationService;

    /** Mocked degree auditor. */
    @Mock
    private DegreeAuditService degreeAuditService;

    /** PlanComparisonService under test. */
    private PlanComparisonService service;

    /**
     * Test setup.
     */
    @BeforeEach
    public void setUp() {
        service = new PlanComparisonService(
                planValidationService, degreeAuditService);
    }

    /**
     * Tests earlier semesters feed later ones, issues are tagged with
     * their term, missing plan IDs get defaults, and a feasible plan
     * beats an infeasible one on every default criterion.
     */
    @Test
    public void testComparePlansDefaultCriteria() {
        final ValidationIssue conflict = new ValidationIssue(
                "TIME_CONFLICT", "COMSW4156", "COMSW4118", "Conflict.");
        when(planValidationService.validateSchedule(any())).thenReturn(
                new ScheduleValidationResult(true, 3, List.of()),
                new ScheduleValidationResult(true, 3, List.of()),
                new ScheduleValidationResult(false, 6, List.of(conflict)));
        when(degreeAuditService.auditDegreeProgress(any())).thenReturn(
                new DegreeAuditResult("CS_MS", 6, 6, true,
                        List.of("COMSW4156", "COMSW4118"), List.of(),
                        List.of()),
                new DegreeAuditResult("CS_MS", 6, 6, true,
                        List.of("COMSW4156", "COMSW4118"), List.of(),
                        List.of()));

        final CandidatePlan split = new CandidatePlan("SPLIT", List.of(
                new PlanSemester("FALL2026", List.of("COMSW4156")),
                new PlanSemester("SPRING2027", List.of("COMSW4118"))));
        final CandidatePlan packed = new CandidatePlan(null, List.of(
                new PlanSemester("FALL2026",
                        List.of("COMSW4156", "COMSW4118"))));
        final CompletedCourse prior =
                new CompletedCourse("COMSW1004", "FALL2025", "A", 3);

        final PlanComparisonResult result = service.comparePlans(
                new PlanComparisonRequest("CS_MS", null, List.of(prior),
                        List.of(split, packed), null));

        final ArgumentCaptor<ScheduleValidationRequest> captor =
                ArgumentCaptor.forClass(ScheduleValidationRequest.class);
        verify(planValidationService, times(3))
                .validateSchedule(captor.capture());
        final List<ScheduleValidationRequest> sent = captor.getAllValues();
        assertEquals(List.of(prior), sent.get(0).getCompletedCourses());
        assertEquals(List.of(prior,
                new CompletedCourse("COMSW4156", "FALL2026", "A", 0)),
                sent.get(1).getCompletedCourses());
        assertEquals("SPRING2027", sent.get(1).getTargetTerm());

        final PlanEvaluation first = result.getEvaluations().get(0);
        assertTrue(first.isFeasible());
        assertEquals(List.of(3, 3), first.getCreditsPerSemester());
        assertEquals(6, first.getTotalCredits());
        assertTrue(first.isDegreeCompleted());

        final PlanEvaluation second = result.getEvaluations().get(1);
        assertEquals("PLAN_2", second.getPlanId());
        assertFalse(second.isFeasible());
        assertEquals("[FALL2026] Conflict.",
                second.getIssues().get(0).getMessage());

        assertEquals(Map.of(
                PlanComparisonService.REQUIREMENTS_SATISFIED, "SPLIT",
                PlanComparisonService.CREDIT_BALANCE, "SPLIT",
                PlanComparisonService.CONFLICTS, "SPLIT"),
                result.getBestPlanByCriterion());
    }

    /**
     * Tests requested criteria are normalized, blank plan IDs get
     * defaults, and that tie-breakers
     * (outstanding count, then credits, then spread) pick different
     * winners among feasible plans.
     */
    @Test
    public void testComparePlansRequestedCriteria() {
        when(planValidationService.validateSchedule(any())).thenReturn(
                new ScheduleValidationResult(true, 9, List.of()),
                new ScheduleValidationResult(true, 3, List.of()),
                new ScheduleValidationResult(true, 6, List.of()),
                new ScheduleValidationResult(true, 6, List.of()));
        when(degreeAuditService.auditDegreeProgress(any())).thenReturn(
                new DegreeAuditResult("CS_MS", 12, 30, false,
                        List.of(), List.of("COMSW4995"), List.of()),
                new DegreeAuditResult("CS_MS", 9, 30, false,
                        List.of(), List.of("COMSW4995"), List.of()));

        final CandidatePlan heavy = new CandidatePlan(" ", List.of(
                new PlanSemester("FALL2026", List.of("A", "B", "C")),
                new PlanSemester("SPRING2027", List.of("D"))));
        final CandidatePlan even = new CandidatePlan("EVEN", List.of(
                new PlanSemester("FALL2026", List.of("A", "B")),
                new PlanSemester("SPRING2027", List.of("C", "D"))));

        final PlanComparisonResult result = service.comparePlans(
                new PlanComparisonRequest("CS_MS", 2026, null,
                        List.of(heavy, even),
                        List.of(" credit_balance", "Requirements_Satisfied")));

        assertEquals(6, result.getEvaluations().get(0).getCreditSpread());
        assertEquals(0, result.getEvaluations().get(1).getCreditSpread());
        assertEquals(Map.of(
                PlanComparisonService.CREDIT_BALANCE, "EVEN",
                PlanComparisonService.REQUIREMENTS_SATISFIED, "PLAN_1"),
                result.getBestPlanByCriterion());
        assertEquals("CS_MS", result.getProgramId());
    }

    /**
     * Tests criterion name checking.
     */
    @Test
    public void testIsSupportedCriterion() {
        assertTrue(PlanComparisonService.isSupportedCriterion(" conflicts "));
        assertFalse(PlanComparisonService.isSupportedCriterion("COST"));
        assertFalse(PlanComparisonService.isSupportedCriterion(null));
    }
}
