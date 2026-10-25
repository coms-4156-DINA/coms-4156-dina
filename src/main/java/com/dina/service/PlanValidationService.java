package com.dina.service;

import com.dina.model.CompletedCourse;
import com.dina.model.Course;
import com.dina.model.ScheduleValidationRequest;
import com.dina.model.ScheduleValidationResult;
import com.dina.model.ValidationIssue;
import com.dina.repository.CourseRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * Service evaluating semester schedules against academic and timing rules.
*/
@Service
public final class PlanValidationService {

    /** Maximum allowed credits per semester term. */
    public static final int MAX_CREDITS_PER_SEMESTER = 18;

    /** Course repository for catalog lookups. */
    private final CourseRepository courseRepository;

    /**
     * Constructs PlanValidationService with repository.
     *
     * @param courseRepository course data repository
     */
    public PlanValidationService(final CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Validates a proposed semester schedule.
     *
     * @param request schedule validation request
     * @return validation result containing validity status and issues
     */
    public ScheduleValidationResult validateSchedule(
            final ScheduleValidationRequest request
    ) {
        if (request == null || request.getCourseIds().isEmpty()) {
            final List<ValidationIssue> issues = new ArrayList<>();
            issues.add(new ValidationIssue(
                    "EMPTY_SCHEDULE", null, null,
                    "Proposed course list cannot be empty."
            ));
            return new ScheduleValidationResult(false, 0, issues);
        }

        final List<ValidationIssue> issues = new ArrayList<>();
        final List<Course> resolvedCourses = new ArrayList<>();
        final Set<String> seenIds = new HashSet<>();
        int totalCredits = 0;

        final Set<String> completedCourseIds = extractCompletedCourseIds(
                request.getCompletedCourses(), request.getCurrentEnrollment());

        for (final String courseId : request.getCourseIds()) {
            if (courseId == null || courseId.isBlank()) {
                continue;
            }
            final String normId = courseId.trim().toUpperCase();

            // Duplicate within proposed plan check
            if (!seenIds.add(normId)) {
                issues.add(new ValidationIssue(
                        "DUPLICATE_COURSE", normId, null,
                        "Course " + normId + " is duplicated in proposed plan."
                ));
                continue;
            }

            // Already completed check
            if (completedCourseIds.contains(normId)) {
                issues.add(new ValidationIssue(
                        "ALREADY_COMPLETED", normId, null,
                        "Course " + normId + " is already completed/enrolled."
                ));
            }

            // Lookup course
            final Optional<Course> optCourse =
                    courseRepository.findCourseById(normId);
            if (optCourse.isEmpty()) {
                issues.add(new ValidationIssue(
                        "UNKNOWN_COURSE", normId, null,
                        "Course " + normId + " does not exist in catalog."
                ));
                continue;
            }

            final Course course = optCourse.get();
            resolvedCourses.add(course);
            totalCredits += course.getCredits();

            // Term availability check
            checkTermAvailability(course, request.getTargetTerm(), issues);

            // Prerequisite check
            checkPrerequisites(course, completedCourseIds, issues);
        }

        // Credit limit check
        if (totalCredits > MAX_CREDITS_PER_SEMESTER) {
            issues.add(new ValidationIssue(
                    "CREDIT_LIMIT_EXCEEDED", null, null,
                    "Total credits (" + totalCredits + ") exceeds max limit ("
                            + MAX_CREDITS_PER_SEMESTER + ")."
            ));
        }

        // Time conflict check
        checkScheduleTimeConflicts(resolvedCourses, issues);

        final boolean isValid = issues.isEmpty();
        return new ScheduleValidationResult(isValid, totalCredits, issues);
    }

    private Set<String> extractCompletedCourseIds(
            final List<CompletedCourse> completed,
            final List<String> current
    ) {
        final Set<String> set = new HashSet<>();
        if (completed != null) {
            for (final CompletedCourse cc : completed) {
                if (cc.getCourseId() != null) {
                    set.add(cc.getCourseId().trim().toUpperCase());
                }
            }
        }
        if (current != null) {
            for (final String cur : current) {
                if (cur != null) {
                    set.add(cur.trim().toUpperCase());
                }
            }
        }
        return set;
    }

    private void checkTermAvailability(
            final Course course,
            final String targetTerm,
            final List<ValidationIssue> issues
    ) {
        if (targetTerm == null || targetTerm.isBlank()) {
            return;
        }
        final String upperTerm = targetTerm.toUpperCase();
        boolean offered = false;
        for (final String sem : course.getSemestersOffered()) {
            if (upperTerm.contains(sem.toUpperCase())) {
                offered = true;
                break;
            }
        }
        if (!offered) {
            issues.add(new ValidationIssue(
                    "TERM_UNAVAILABLE", course.getCourseId(), null,
                    "Course " + course.getCourseId()
                            + " is not offered in term " + targetTerm + "."
            ));
        }
    }

    private void checkPrerequisites(
            final Course course,
            final Set<String> completedIds,
            final List<ValidationIssue> issues
    ) {
        for (final String prereq : course.getPrerequisites()) {
            final String normPrereq = prereq.trim().toUpperCase();
            if (!completedIds.contains(normPrereq)) {
                issues.add(new ValidationIssue(
                        "UNMET_PREREQUISITE", course.getCourseId(), normPrereq,
                        "Prerequisite " + normPrereq + " not met for "
                                + course.getCourseId() + "."
                ));
            }
        }
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Pairwise schedule collision check
    // -------------------------------------------------------------------------
    private void checkScheduleTimeConflicts(
            final List<Course> courses,
            final List<ValidationIssue> issues
    ) {
        for (int i = 0; i < courses.size(); i++) {
            for (int j = i + 1; j < courses.size(); j++) {
                final Course c1 = courses.get(i);
                final Course c2 = courses.get(j);
                if (hasTimeConflict(c1, c2)) {
                    issues.add(new ValidationIssue(
                            "TIME_CONFLICT", c1.getCourseId(), c2.getCourseId(),
                            "Schedule conflict between " + c1.getCourseId()
                                    + " and " + c2.getCourseId() + "."
                    ));
                }
            }
        }
    }

    private boolean hasTimeConflict(final Course c1, final Course c2) {
        for (final String t1 : c1.getMeetingTimes()) {
            for (final String t2 : c2.getMeetingTimes()) {
                if (t1 != null && t1.equalsIgnoreCase(t2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
