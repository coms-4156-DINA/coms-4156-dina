package com.dina.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dina.model.Course;
import com.dina.repository.CourseRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for CourseService using Mockito test doubles.
*/
@ExtendWith(MockitoExtension.class)
public final class CourseServiceTest {

    /** Mock repository test double. */
    @Mock
    private CourseRepository courseRepository;

    /** Injected CourseService under test. */
    @InjectMocks
    private CourseService courseService;

    /**
     * Tests getAllCourses retrieves courses from repository.
     */
    @Test
    public void testGetAllCourses() {
        final Course course = new Course(
                "COMSW1004", "COMS", 1004, "Intro to Java", 3,
                Collections.emptyList()
        ).withSemestersOffered(Collections.singletonList("FALL"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
        when(courseRepository.findAllCourses()).thenReturn(
                Collections.singletonList(course));

        final List<Course> results = courseService.getAllCourses();

        assertEquals(1, results.size());
        assertEquals("COMSW1004", results.get(0).getCourseId());
        verify(courseRepository).findAllCourses();
    }

    /**
     * Tests getCourseById returns course when present.
     */
    @Test
    public void testGetCourseByIdFound() {
        final Course course = new Course(
                "COMSW4156", "COMS", 4156, "ASE", 3,
                Collections.emptyList()
        ).withSemestersOffered(Collections.singletonList("FALL"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
        when(courseRepository.findCourseById("COMSW4156")).thenReturn(
                Optional.of(course));

        final Optional<Course> result =
                courseService.getCourseById("COMSW4156");

        assertTrue(result.isPresent());
        assertEquals("COMSW4156", result.get().getCourseId());
    }

    /**
     * Tests getCourseById returns empty when not found.
     */
    @Test
    public void testGetCourseByIdNotFound() {
        when(courseRepository.findCourseById("UNKNOWN")).thenReturn(
                Optional.empty());

        final Optional<Course> result =
                courseService.getCourseById("UNKNOWN");

        assertFalse(result.isPresent());
    }

    /**
     * Tests searchCourses delegates to repository.
     */
    @Test
    public void testSearchCourses() {
        when(courseRepository.searchCourses(
                "COMS", 4000, 3, "FALL", "Software"
        )).thenReturn(Collections.emptyList());

        final List<Course> results = courseService.searchCourses(
                "COMS", 4000, 3, "FALL", "Software");

        assertTrue(results.isEmpty());
        verify(courseRepository).searchCourses(
                "COMS", 4000, 3, "FALL", "Software");
    }
}
