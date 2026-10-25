package com.dina.controller;

import com.dina.model.DegreeProgram;
import com.dina.service.DegreeProgramService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing Operation 6: Retrieve degree requirements.
*/
@RestController
@RequestMapping("/programs")
public final class ProgramController {

    /** Service retrieving program requirements. */
    private final DegreeProgramService degreeProgramService;

    /**
     * Constructs ProgramController with injected service.
     *
     * @param degreeProgramService degree program service
     */
    public ProgramController(
            final DegreeProgramService degreeProgramService
    ) {
        this.degreeProgramService = degreeProgramService;
    }

    /**
     * Retrieves degree requirements by program identifier.
     *
     * @param programId degree program identifier (e.g., CS_BS)
     * @param catalogYear optional catalog year
     * @return degree requirements or 404 NOT_FOUND
     */
    @GetMapping("/{programId}/requirements")
    public ResponseEntity<DegreeProgram> getProgramRequirements(
            @PathVariable final String programId,
            @RequestParam(required = false) final Integer catalogYear
    ) {
        final Optional<DegreeProgram> optProgram =
                degreeProgramService.getProgramRequirements(
                        programId, catalogYear);
        return optProgram.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(
                        HttpStatus.NOT_FOUND).build());
    }
}
