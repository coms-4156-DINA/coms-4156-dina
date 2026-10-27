package com.dina.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.dina.model.CompletedCourse;
import com.dina.model.Course;
import com.dina.model.ScheduleValidationRequest;
import com.dina.model.ScheduleValidationResult;
import com.dina.model.ValidationIssue;
import com.dina.repository.CourseRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for PlanValidationService verifying schedule constraints.
*/
@ExtendWith(MockitoExtension.class)
public final class PlanValidationServiceTest {

    /** Mock repository test double. */
    @Mock
    private CourseRepository courseRepository;

    /** PlanValidationService under test. */
    private PlanValidationService service;

    /**
     * Test setup.
     */
    @BeforeEach
    public void setUp() {
        service = new PlanValidationService(courseRepository);
    }

    /**
     * Tests empty request returns invalid.
     */
    @Test
    public void testEmptyScheduleRequest() {
        final ScheduleValidationResult result = service.validateSchedule(null);
        assertFalse(result.isValid());
        assertEquals("EMPTY_SCHEDULE", result.getIssues().get(0).getType());
    }

    /**
     * Tests valid plan where all prereqs are met and no conflicts exist.
     */
    @Test
    public void testValidPlan() {
        final Course c1 = new Course(
                "COMSW3134", "COMS", 3134, "Data Structures", 3,
                Collections.singletonList("COMSW1004")
        ).withSemestersOffered(Collections.singletonList("FALL"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
        when(courseRepository.findCourseById("COMSW3134")).thenReturn(
                Optional.of(c1));

        final ScheduleValidationRequest request =
                new ScheduleValidationRequest(
                        Collections.singletonList("COMSW3134"),
                        Collections.singletonList(
                                new CompletedCourse(
                                        "COMSW1004", "FALL2025", "A", 3)),
                        Collections.emptyList(),
                        "FALL2026"
                );

        final ScheduleValidationResult result =
                service.validateSchedule(request);

        assertTrue(result.isValid());
        assertEquals(3, result.getTotalCredits());
        assertTrue(result.getIssues().isEmpty());
    }

    /**
     * Tests unknown course in plan flags UNKNOWN_COURSE issue.
     */
    @Test
    public void testUnknownCourse() {
        when(courseRepository.findCourseById("COMSW9999")).thenReturn(
                Optional.empty());

        final ScheduleValidationRequest request =
                new ScheduleValidationRequest(
                        Collections.singletonList("COMSW9999"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        "FALL2026"
                );

        final ScheduleValidationResult result =
                service.validateSchedule(request);

        assertFalse(result.isValid());
        assertEquals("UNKNOWN_COURSE", result.getIssues().get(0).getType());
    }

    /**
     * Tests duplicate course in schedule flags DUPLICATE_COURSE.
     */
    @Test
    public void testDuplicateCourse() {
        final Course c1 = new Course(
                "COMSW1004", "COMS", 1004, "Intro", 3,
                Collections.emptyList()
        ).withSemestersOffered(Collections.singletonList("FALL"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
        when(courseRepository.findCourseById("COMSW1004")).thenReturn(
                Optional.of(c1));

        final ScheduleValidationRequest request =
                new ScheduleValidationRequest(
                        Arrays.asList("COMSW1004", "COMSW1004"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        "FALL2026"
                );

        final ScheduleValidationResult result =
                service.validateSchedule(request);

        assertFalse(result.isValid());
        assertEquals("DUPLICATE_COURSE", result.getIssues().get(0).getType());
    }

    /**
     * Tests unmet prerequisite flags UNMET_PREREQUISITE.
     */
    @Test
    public void testUnmetPrerequisite() {
        final Course c1 = new Course(
                "COMSW3134", "COMS", 3134, "Data Structures", 3,
                Collections.singletonList("COMSW1004")
        ).withSemestersOffered(Collections.singletonList("FALL"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
        when(courseRepository.findCourseById("COMSW3134")).thenReturn(
                Optional.of(c1));

        final ScheduleValidationRequest request =
                new ScheduleValidationRequest(
                        Collections.singletonList("COMSW3134"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        "FALL2026"
                );

        final ScheduleValidationResult result =
                service.validateSchedule(request);

        assertFalse(result.isValid());
        assertEquals(
                "UNMET_PREREQUISITE", result.getIssues().get(0).getType());
    }

    /**
     * Tests schedule collision flags TIME_CONFLICT.
     */
    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Schedule collision & limit test cases
    // -------------------------------------------------------------------------
    @Test
    public void testScheduleCollision() {
        final Course c1 = new Course(
                "COMSW1004", "COMS", 1004, "Intro", 3,
                Collections.emptyList()
        ).withSemestersOffered(Collections.singletonList("FALL"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
        final Course c2 = new Course(
                "COMSW4156", "COMS", 4156, "ASE", 3,
                Collections.emptyList()
        ).withSemestersOffered(Collections.singletonList("FALL"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
        when(courseRepository.findCourseById("COMSW1004")).thenReturn(
                Optional.of(c1));
        when(courseRepository.findCourseById("COMSW4156")).thenReturn(
                Optional.of(c2));

        final ScheduleValidationRequest request =
                new ScheduleValidationRequest(
                        Arrays.asList("COMSW1004", "COMSW4156"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        "FALL2026"
                );

        final ScheduleValidationResult result =
                service.validateSchedule(request);

        assertFalse(result.isValid());
        assertEquals("TIME_CONFLICT", result.getIssues().get(0).getType());
    }

    /**
     * Tests term unavailability flags TERM_UNAVAILABLE.
     */
    @Test
    public void testTermUnavailable() {
        final Course c1 = new Course(
                "COMSW1004", "COMS", 1004, "Intro", 3,
                Collections.emptyList()
        ).withSemestersOffered(Collections.singletonList("SPRING"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
        when(courseRepository.findCourseById("COMSW1004")).thenReturn(
                Optional.of(c1));

        final ScheduleValidationRequest request =
                new ScheduleValidationRequest(
                        Collections.singletonList("COMSW1004"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        "FALL2026"
                );

        final ScheduleValidationResult result =
                service.validateSchedule(request);

        assertFalse(result.isValid());
        assertEquals("TERM_UNAVAILABLE", result.getIssues().get(0).getType());
    }

    /**
     * Tests credit limit exceeded flags CREDIT_LIMIT_EXCEEDED.
     */
    @Test
    public void testCreditLimitExceeded() {
        final Course c1 = new Course(
                "COMSW1004", "COMS", 1004, "Big Course", 20,
                Collections.emptyList()
        ).withSemestersOffered(Collections.singletonList("FALL"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
        when(courseRepository.findCourseById("COMSW1004")).thenReturn(
                Optional.of(c1));

        final ScheduleValidationRequest request =
                new ScheduleValidationRequest(
                        Collections.singletonList("COMSW1004"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        "FALL2026"
                );

        final ScheduleValidationResult result =
                service.validateSchedule(request);

        assertFalse(result.isValid());
        assertEquals(
                "CREDIT_LIMIT_EXCEEDED", result.getIssues().get(0).getType());
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude Opus 5.5] Additional validation edge-case tests
    // The tests below were written with AI assistance.
    // -------------------------------------------------------------------------

    /**
     * Builds a catalog course with a single offered term and meeting time.
     *
     * @param courseId course identifier
     * @param credits credit value
     * @param prereqs prerequisite course IDs
     * @param term semester the course is offered in
     * @param time meeting time string
     * @return course for use in repository stubs
     */
    private static Course course(
            final String courseId,
            final int credits,
            final List<String> prereqs,
            final String term,
            final String time
    ) {
        return new Course(
                courseId, "COMS", 1000, "Title " + courseId, credits, prereqs
        ).withSemestersOffered(Collections.singletonList(term))
         .withMeetingTimes(Collections.singletonList(time))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
    }

    /**
     * Builds a request with no transcript for the given courses and term.
     *
     * @param courseIds proposed course IDs
     * @param targetTerm target term
     * @return schedule validation request
     */
    private static ScheduleValidationRequest request(
            final List<String> courseIds, final String targetTerm) {
        return new ScheduleValidationRequest(
                courseIds,
                Collections.emptyList(),
                Collections.emptyList(),
                targetTerm
        );
    }

    /**
     * Extracts the issue types from a validation result, in order.
     *
     * @param result validation result
     * @return list of issue type codes
     */
    private static List<String> issueTypes(
            final ScheduleValidationResult result) {
        return result.getIssues().stream()
                .map(ValidationIssue::getType)
                .toList();
    }

    /**
     * Tests null and blank course IDs are skipped rather than flagged.
     */
    @Test
    public void testNullAndBlankCourseIdsSkipped() {
        when(courseRepository.findCourseById("COMSW1004")).thenReturn(
                Optional.of(course("COMSW1004", 3, Collections.emptyList(),
                        "FALL", "MW 10:10-11:25")));

        final ScheduleValidationResult result = service.validateSchedule(
                request(Arrays.asList(null, "   ", "COMSW1004"), "FALL2026"));

        assertTrue(result.isValid());
        assertEquals(3, result.getTotalCredits());
    }

    /**
     * Tests a course already on the transcript flags ALREADY_COMPLETED.
     */
    @Test
    public void testAlreadyCompletedCourse() {
        when(courseRepository.findCourseById("COMSW1004")).thenReturn(
                Optional.of(course("COMSW1004", 3, Collections.emptyList(),
                        "FALL", "MW 10:10-11:25")));

        final ScheduleValidationRequest req = new ScheduleValidationRequest(
                Collections.singletonList("COMSW1004"),
                Collections.singletonList(
                        new CompletedCourse("COMSW1004", "FALL2025", "B", 3)),
                Collections.emptyList(),
                "FALL2026"
        );
        final ScheduleValidationResult result = service.validateSchedule(req);

        assertFalse(result.isValid());
        assertEquals(List.of("ALREADY_COMPLETED"), issueTypes(result));
        assertEquals("COMSW1004", result.getIssues().get(0).getCourseId());
    }

    /**
     * Tests a prerequisite in current enrollment satisfies the requirement.
     */
    @Test
    public void testPrerequisiteSatisfiedByCurrentEnrollment() {
        when(courseRepository.findCourseById("COMSW3134")).thenReturn(
                Optional.of(course("COMSW3134", 3,
                        Collections.singletonList("COMSW1004"),
                        "FALL", "TR 13:10-14:25")));

        final ScheduleValidationRequest req = new ScheduleValidationRequest(
                Collections.singletonList("COMSW3134"),
                Collections.emptyList(),
                Collections.singletonList("COMSW1004"),
                "FALL2026"
        );
        final ScheduleValidationResult result = service.validateSchedule(req);

        assertTrue(result.isValid());
    }

    /**
     * Tests every missing prerequisite produces its own issue.
     */
    @Test
    public void testEachMissingPrerequisiteReported() {
        when(courseRepository.findCourseById("COMSW3261")).thenReturn(
                Optional.of(course("COMSW3261", 3,
                        Arrays.asList("COMSW3134", "COMSW3203"),
                        "FALL", "MW 14:40-15:55")));

        final ScheduleValidationRequest req = new ScheduleValidationRequest(
                Collections.singletonList("COMSW3261"),
                Collections.singletonList(
                        new CompletedCourse("COMSW3134", "SPRING2026", "A", 3)),
                Collections.emptyList(),
                "FALL2026"
        );
        final ScheduleValidationResult result = service.validateSchedule(req);

        assertFalse(result.isValid());
        assertEquals(List.of("UNMET_PREREQUISITE"), issueTypes(result));
        assertEquals("COMSW3261", result.getIssues().get(0).getCourseId());
        assertEquals("COMSW3203",
                result.getIssues().get(0).getConflictingCourseId());
    }

    /**
     * Tests a null target term skips the term availability check.
     */
    @Test
    public void testNullTargetTermSkipsTermCheck() {
        when(courseRepository.findCourseById("COMSW1004")).thenReturn(
                Optional.of(course("COMSW1004", 3, Collections.emptyList(),
                        "SPRING", "MW 10:10-11:25")));

        final ScheduleValidationResult result = service.validateSchedule(
                request(Collections.singletonList("COMSW1004"), null));

        assertTrue(result.isValid());
    }

    /**
     * Tests a schedule at exactly the credit limit is still valid.
     */
    @Test
    public void testCreditLimitBoundaryAllowed() {
        when(courseRepository.findCourseById("COMSW1004")).thenReturn(
                Optional.of(course("COMSW1004",
                        PlanValidationService.MAX_CREDITS_PER_SEMESTER,
                        Collections.emptyList(), "FALL", "MW 10:10-11:25")));

        final ScheduleValidationResult result = service.validateSchedule(
                request(Collections.singletonList("COMSW1004"), "FALL2026"));

        assertTrue(result.isValid());
        assertEquals(PlanValidationService.MAX_CREDITS_PER_SEMESTER,
                result.getTotalCredits());
    }

    /**
     * Tests courses with different meeting times do not conflict.
     */
    @Test
    public void testDifferentMeetingTimesNoConflict() {
        when(courseRepository.findCourseById("COMSW1004")).thenReturn(
                Optional.of(course("COMSW1004", 3, Collections.emptyList(),
                        "FALL", "MW 10:10-11:25")));
        when(courseRepository.findCourseById("MATH1201")).thenReturn(
                Optional.of(course("MATH1201", 3, Collections.emptyList(),
                        "FALL", "MW 08:40-09:55")));

        final ScheduleValidationResult result = service.validateSchedule(
                request(Arrays.asList("COMSW1004", "MATH1201"), "FALL2026"));

        assertTrue(result.isValid());
        assertEquals(6, result.getTotalCredits());
    }

    /**
     * Tests an unknown course does not stop validation of other courses
     * and is excluded from the credit total.
     */
    @Test
    public void testUnknownCourseDoesNotStopValidation() {
        when(courseRepository.findCourseById("COMSW9999")).thenReturn(
                Optional.empty());
        when(courseRepository.findCourseById("COMSW3134")).thenReturn(
                Optional.of(course("COMSW3134", 3,
                        Collections.singletonList("COMSW1004"),
                        "FALL", "TR 13:10-14:25")));

        final ScheduleValidationResult result = service.validateSchedule(
                request(Arrays.asList("COMSW9999", "COMSW3134"), "FALL2026"));

        assertFalse(result.isValid());
        assertEquals(List.of("UNKNOWN_COURSE", "UNMET_PREREQUISITE"),
                issueTypes(result));
        assertEquals(3, result.getTotalCredits());
    }

    /**
     * Tests all problems in one schedule are collected, with whole-schedule
     * checks reported after per-course checks.
     */
    @Test
    public void testMultipleIssuesCollected() {
        when(courseRepository.findCourseById("COMSW1004")).thenReturn(
                Optional.of(course("COMSW1004", 10, Collections.emptyList(),
                        "SPRING", "MW 10:10-11:25")));
        when(courseRepository.findCourseById("COMSW4156")).thenReturn(
                Optional.of(course("COMSW4156", 10,
                        Collections.singletonList("COMSW3157"),
                        "FALL", "MW 10:10-11:25")));

        final ScheduleValidationResult result = service.validateSchedule(
                request(Arrays.asList("COMSW1004", "COMSW4156"), "FALL2026"));

        assertFalse(result.isValid());
        assertEquals(20, result.getTotalCredits());
        assertEquals(List.of(
                "TERM_UNAVAILABLE",
                "UNMET_PREREQUISITE",
                "CREDIT_LIMIT_EXCEEDED",
                "TIME_CONFLICT"), issueTypes(result));
    }
}
