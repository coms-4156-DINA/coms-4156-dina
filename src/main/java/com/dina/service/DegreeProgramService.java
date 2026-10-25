package com.dina.service;

import com.dina.model.DegreeProgram;
import com.dina.repository.CourseRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service providing degree requirements retrieval.
*/
@Service
public final class DegreeProgramService {

    /** Repository for accessing degree programs. */
    private final CourseRepository courseRepository;

    /**
     * Constructs DegreeProgramService with repository.
     *
     * @param courseRepository course data repository
     */
    public DegreeProgramService(final CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieves degree requirements for a given program ID and catalog year.
     *
     * @param programId degree program identifier
     * @param catalogYear optional catalog year
     * @return optional containing degree program if found
     */
    public Optional<DegreeProgram> getProgramRequirements(
            final String programId,
            final Integer catalogYear
    ) {
        return courseRepository.findProgramById(programId, catalogYear);
    }
}
