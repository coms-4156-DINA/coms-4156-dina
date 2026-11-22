package com.dina.controller;

import com.dina.model.CompletedCourse;
import com.dina.model.PlanComparisonRequest;
import com.dina.model.PlanComparisonResult;
import com.dina.model.ScheduleValidationRequest;
import com.dina.model.ScheduleValidationResult;
import com.dina.service.PlanComparisonService;
import com.dina.service.PlanValidationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing Operation 2 (validate a proposed semester schedule)
 * and Operation 5 (compare alternative plans).
*/
@RestController
@RequestMapping("/plans")
public final class PlanController {

    /** minimum number of plans needed for a comparison. */
    private static final int MIN_PLANS_TO_COMPARE = 2;

    /** service validating academic plans. */
    private final PlanValidationService planValidationService;

    /** service comparing alternative plans. */
    private final PlanComparisonService planComparisonService;

    /**
     * constructs PlanController with injected services.
     *
     * @param planValidationService plan validation service
     * @param planComparisonService plan comparison service
     */
    public PlanController(
            final PlanValidationService planValidationService,
            final PlanComparisonService planComparisonService
    ) {
        this.planValidationService = planValidationService;
        this.planComparisonService = planComparisonService;
    }

    /**
     * Validates a proposed semester schedule via POST body.
     *
     * @param request JSON request payload
     * @return schedule validation result
     */
    @PostMapping("/validate")
    public ResponseEntity<ScheduleValidationResult> validatePost(
            @RequestBody final ScheduleValidationRequest request
    ) {
        final ScheduleValidationResult result =
                planValidationService.validateSchedule(request);
        return ResponseEntity.ok(result);
    }

    /**
     * compares two or more candidate plans.
     * 
     * @param request JSON request payload
     * @return comparison result, 400 if the request is malformed, or
     *         404 if the degree program does not exist
     */
    @PostMapping("/compare")
    public ResponseEntity<PlanComparisonResult> comparePost(
            @RequestBody final PlanComparisonRequest request
    ) {
        if (request.getProgramId() == null
                || request.getProgramId().isBlank()
                || request.getPlans().size() < MIN_PLANS_TO_COMPARE
                || !request.getCriteria().stream().allMatch(
                        PlanComparisonService::isSupportedCriterion)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            return ResponseEntity.ok(
                    planComparisonService.comparePlans(request));
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Request parameter resolution helper
    // -------------------------------------------------------------------------
    private ScheduleValidationRequest resolveRequest(
            final ScheduleValidationRequest body,
            final List<String> courseIds,
            final String targetTerm,
            final List<String> completedCourseIds
    ) {
        if (body != null && body.getCourseIds() != null
                && !body.getCourseIds().isEmpty()) {
            return body;
        }

        final List<String> courses = courseIds != null
                ? courseIds : new ArrayList<>();
        final List<CompletedCourse> completed = new ArrayList<>();
        if (completedCourseIds != null) {
            for (final String cid : completedCourseIds) {
                completed.add(new CompletedCourse(cid, "PAST", "A", 3));
            }
        }
        return new ScheduleValidationRequest(
                courses, completed, new ArrayList<>(), targetTerm);
    }
}
