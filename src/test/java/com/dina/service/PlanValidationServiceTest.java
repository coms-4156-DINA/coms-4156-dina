package com.dina.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.dina.model.CompletedCourse;
import com.dina.model.Course;
import com.dina.model.ScheduleValidationRequest;
import com.dina.model.ScheduleValidationResult;
import com.dina.repository.CourseRepository;
import java.util.Arrays;
import java.util.Collections;
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
}
