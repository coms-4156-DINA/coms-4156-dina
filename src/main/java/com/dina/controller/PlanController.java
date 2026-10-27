package com.dina.controller;

import com.dina.model.CompletedCourse;
import com.dina.model.ScheduleValidationRequest;
import com.dina.model.ScheduleValidationResult;
import com.dina.service.PlanValidationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing Operation 2: Validate proposed semester schedule.
*/
@RestController
@RequestMapping("/plans")
public final class PlanController {

    /** Service validating academic plans. */
    private final PlanValidationService planValidationService;

    /**
     * Constructs PlanController with injected validation service.
     *
     * @param planValidationService plan validation service
     */
    public PlanController(
            final PlanValidationService planValidationService
    ) {
        this.planValidationService = planValidationService;
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
