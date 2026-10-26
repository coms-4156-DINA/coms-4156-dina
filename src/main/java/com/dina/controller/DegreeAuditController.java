package com.dina.controller;

import com.dina.model.CompletedCourse;
import com.dina.model.DegreeAuditRequest;
import com.dina.model.DegreeAuditResult;
import com.dina.service.DegreeAuditService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing Operation 3: Calculate degree progress.
*/
@RestController
@RequestMapping("/degree-audit")
public final class DegreeAuditController {

    /** Service executing degree progress evaluations. */
    private final DegreeAuditService degreeAuditService;

    /**
     * Constructs DegreeAuditController with injected service.
     *
     * @param degreeAuditService degree audit service
     */
    public DegreeAuditController(
            final DegreeAuditService degreeAuditService
    ) {
        this.degreeAuditService = degreeAuditService;
    }

    /**
     * Audits degree progress via GET request params or body.
     *
     * @param body optional JSON body
     * @param programId target degree program ID
     * @param catalogYear optional catalog year
     * @param completedCourses completed course IDs
     * @param inProgressCourses in-progress course IDs
     * @return degree audit result
     */
    @GetMapping
    public ResponseEntity<DegreeAuditResult> auditGet(
            @RequestBody(required = false) final DegreeAuditRequest body,
            @RequestParam(required = false) final String programId,
            @RequestParam(required = false) final Integer catalogYear,
            @RequestParam(required = false)
            final List<String> completedCourses,
            @RequestParam(required = false)
            final List<String> inProgressCourses
    ) {
        // A body that was sent but has no usable programId is a
        // malformed request, not a cue to silently fall back to
        // query params.
        // This if statement was added by ai sonnet 5
        if (body != null
                && (body.getProgramId() == null
                        || body.getProgramId().isBlank())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        final DegreeAuditRequest request = resolveRequest(
                body, programId, catalogYear, completedCourses,
                inProgressCourses);
        if (request.getProgramId() == null
                || request.getProgramId().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            final DegreeAuditResult result =
                    degreeAuditService.auditDegreeProgress(request);
            return ResponseEntity.ok(result);
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Audits degree progress via POST request body.
     *
     * @param request degree audit request payload
     * @return degree audit result
     */
    @PostMapping
    public ResponseEntity<DegreeAuditResult> auditPost(
            @RequestBody final DegreeAuditRequest request
    ) {
        if (request == null || request.getProgramId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            final DegreeAuditResult result =
                    degreeAuditService.auditDegreeProgress(request);
            return ResponseEntity.ok(result);
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Request parameter resolution helper
    // -------------------------------------------------------------------------
    private DegreeAuditRequest resolveRequest(
            final DegreeAuditRequest body,
            final String programId,
            final Integer catalogYear,
            final List<String> completedCourseIds,
            final List<String> inProgressCourseIds
    ) {
        if (body != null && body.getProgramId() != null) {
            return body;
        }

        final List<CompletedCourse> completed = new ArrayList<>();
        if (completedCourseIds != null) {
            for (final String cid : completedCourseIds) {
                completed.add(new CompletedCourse(cid, "PAST", "A", 3));
            }
        }
        final List<String> inProgress = inProgressCourseIds != null
                ? inProgressCourseIds : new ArrayList<>();
        return new DegreeAuditRequest(
                programId, catalogYear, completed, inProgress);
    }
}
