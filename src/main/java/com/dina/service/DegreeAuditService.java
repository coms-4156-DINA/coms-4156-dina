package com.dina.service;

import com.dina.model.CompletedCourse;
import com.dina.model.Course;
import com.dina.model.DegreeAuditRequest;
import com.dina.model.DegreeAuditResult;
import com.dina.model.DegreeProgram;
import com.dina.repository.CourseRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * Service calculating degree progress against curriculum rules.
*/
@Service
public final class DegreeAuditService {

    /** Repository for accessing course and degree program data. */
    private final CourseRepository courseRepository;

    /**
     * Constructs DegreeAuditService with repository.
     *
     * @param courseRepository course data repository
     */
    public DegreeAuditService(final CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Computes student's degree progress and remaining deficits.
     *
     * @param request degree audit request containing program and transcript
     * @return audit result containing completed/remaining requirements
     */
    public DegreeAuditResult auditDegreeProgress(
            final DegreeAuditRequest request
    ) {
        if (request == null || request.getProgramId() == null) {
            throw new IllegalArgumentException("Program ID cannot be null.");
        }

        final Optional<DegreeProgram> optProgram =
                courseRepository.findProgramById(
                        request.getProgramId(), request.getCatalogYear());
        if (optProgram.isEmpty()) {
            throw new IllegalArgumentException(
                    "Degree program not found: " + request.getProgramId());
        }

        final DegreeProgram program = optProgram.get();
        final Set<String> completedIds = new HashSet<>();
        final Map<String, Integer> creditsByCourseId = new HashMap<>();
        int creditsEarned = 0;

        for (final CompletedCourse completed : request.getCompletedCourses()) {
            if (completed.getCourseId() == null) {
                continue;
            }
            final String normId = completed.getCourseId().trim().toUpperCase();

            // we keep the latest passing grade of a course
            if (meetsMinimumGrade(
                    completed.getGrade(), program.getMinPassingGrade())) {
                int cred = completed.getCredits();
                if (cred <= 0) {
                    final Optional<Course> c =
                            courseRepository.findCourseById(normId);
                    cred = c.map(Course::getCredits).orElse(3);
                }
                final Integer previousCredits =
                    creditsByCourseId.put(normId, cred);
                creditsEarned += cred - (previousCredits == null
                        ? 0 : previousCredits);
                completedIds.add(normId);
            }
        }

        final Set<String> inProgressIds = new HashSet<>();
        for (final String inProgress : request.getInProgressCourses()) {
            if (inProgress != null) {
                inProgressIds.add(inProgress.trim().toUpperCase());
            }
        }

        final List<String> completedReqs = new ArrayList<>();
        final List<String> remainingReqs = new ArrayList<>();
        final List<String> coursesNeeded = new ArrayList<>();

        for (final String reqCourseId : program.getRequiredCourseIds()) {
            final String normReq = reqCourseId.trim().toUpperCase();
            if (completedIds.contains(normReq)) {
                completedReqs.add(normReq);
            } else if (inProgressIds.contains(normReq)) {
                remainingReqs.add(normReq + " (IN PROGRESS)");
            } else {
                remainingReqs.add(normReq);
                coursesNeeded.add(normReq);
            }
        }

        final List<String> remainingElectives = new ArrayList<>();
        for (final String electiveId : program.getElectiveCourseIds()) {
            final String normElective = electiveId.trim().toUpperCase();
            if (completedIds.contains(normElective)) {
                completedReqs.add("ELECTIVE: " + normElective);
            } else {
                remainingElectives.add(normElective);
            }
        }

        final boolean allCoreMet = remainingReqs.isEmpty();
        final boolean creditsMet =
                creditsEarned >= program.getTotalCreditsRequired();
        final boolean completed = allCoreMet && creditsMet;

        if (!creditsMet && allCoreMet) {
            final int deficit = program.getTotalCreditsRequired()
                    - creditsEarned;
            coursesNeeded.add("Elective credits deficit: " + deficit);
            for (final String electiveId : remainingElectives) {
                coursesNeeded.add("Elective option: " + electiveId);
            }
        }

        return new DegreeAuditResult(
                program.getProgramId(),
                creditsEarned,
                program.getTotalCreditsRequired(),
                completed,
                completedReqs,
                remainingReqs,
                coursesNeeded
        );
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Letter grade to GPA points mapping
    // I fixed the switch statements
    // -------------------------------------------------------------------------
    private Double letterGradeToPoints(final String letterGrade) {
        if (letterGrade == null) {
            return null;
        }
        return switch (letterGrade.trim().toUpperCase()) {
            case "A+", "A" -> 4.0;
            case "A-" -> 3.7;
            case "B+" -> 3.3;
            case "B" -> 3.0;
            case "B-" -> 2.7;
            case "C+" -> 2.3;
            case "C" -> 2.0;
            case "C-" -> 1.7;
            case "D+" -> 1.3;
            case "D" -> 1.0;
            case "D-" -> 0.7;
            case "F" -> 0.0;
            default -> null;
        };
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Grade vs. program minimum comparison
    // -------------------------------------------------------------------------
    private boolean meetsMinimumGrade(
            final String grade, final String minGrade) {
        if (grade == null || grade.isBlank()) {
            return false;
        }
        final String g = grade.trim().toUpperCase();
        if (g.startsWith("F") || g.equals("W") || g.equals("INC")) {
            return false;
        }
        final Double minPoints = letterGradeToPoints(minGrade);
        if (minPoints == null) {
            return true;
        }
        final Double points = letterGradeToPoints(g);
        if (points == null) {
            return false;
        }
        return points >= minPoints;
    }
}
