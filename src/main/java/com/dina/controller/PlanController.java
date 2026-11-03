package com.dina.controller;

import com.dina.model.ScheduleValidationRequest;
import com.dina.model.ScheduleValidationResult;
import com.dina.service.PlanValidationService;
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
}
