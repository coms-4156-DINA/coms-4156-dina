package com.dina.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.dina.model.Course;
import com.dina.repository.AcademicDataStore;
import com.dina.repository.CourseRepository;
import java.util.Arrays;
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

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude Opus 5.5] test cases for CourseService search
    // -------------------------------------------------------------------------

    /**
     * Tests searchCourses returns results sorted by course ID.
     */
    @Test
    public void testSearchCoursesSortedById() {
        final Course later = new Course(
                "COMSW4156", "COMS", 4156, "ASE", 3,
                Collections.emptyList());
        final Course earlier = new Course(
                "COMSW1004", "COMS", 1004, "Intro", 3,
                Collections.emptyList());
        when(courseRepository.searchCourses(
                null, null, null, null, null
        )).thenReturn(Arrays.asList(later, earlier));

        final List<Course> results = courseService.searchCourses(
                null, null, null, null, null);

        assertEquals("COMSW1004", results.get(0).getCourseId());
        assertEquals("COMSW4156", results.get(1).getCourseId());
    }

    /**
     * Tests searchCourses rejects a non-positive level.
     */
    @Test
    public void testSearchCoursesInvalidLevel() {
        assertThrows(IllegalArgumentException.class,
                () -> courseService.searchCourses(
                        null, 0, null, null, null));
        verifyNoInteractions(courseRepository);
    }

    /**
     * Tests searchCourses rejects a non-positive credit count.
     */
    @Test
    public void testSearchCoursesInvalidCredits() {
        assertThrows(IllegalArgumentException.class,
                () -> courseService.searchCourses(
                        null, null, -3, null, null));
        verifyNoInteractions(courseRepository);
    }

    /**
     * Tests searchCourses against the seeded catalog end to end.
     */
    @Test
    public void testSearchCoursesWithSeededCatalog() {
        final CourseService realService =
                new CourseService(new AcademicDataStore());

        final List<Course> results = realService.searchCourses(
                "coms", 4000, 3, "FALL", "software");

        assertEquals(1, results.size());
        assertEquals("COMSW4156", results.get(0).getCourseId());
        assertEquals(Collections.singletonList("COMSW3157"),
                results.get(0).getPrerequisites());
    }
}
