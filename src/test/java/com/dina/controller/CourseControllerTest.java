package com.dina.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dina.model.Course;
import com.dina.service.CourseService;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * API and WebMvc unit tests for CourseController endpoints.
*/
@WebMvcTest(CourseController.class)
public final class CourseControllerTest {

    /** MockMvc test client. */
    @Autowired
    private MockMvc mockMvc;

    /** Mocked CourseService test double. */
    @MockBean
    private CourseService courseService;

    /**
     * Tests GET /courses returns list of courses.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testGetCourses() throws Exception {
        final Course course = new Course(
                "COMSW4156", "COMS", 4156, "ASE", 3,
                Collections.emptyList()
        ).withSemestersOffered(Collections.singletonList("FALL"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
        when(courseService.searchCourses(
                null, null, null, null, null
        )).thenReturn(Collections.singletonList(course));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value("COMSW4156"))
                .andExpect(jsonPath("$[0].credits").value(3));
    }

    /**
     * Tests GET /courses/{courseId} when course is found.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testGetCourseByIdFound() throws Exception {
        final Course course = new Course(
                "COMSW4156", "COMS", 4156, "ASE", 3,
                Collections.emptyList()
        ).withSemestersOffered(Collections.singletonList("FALL"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Desc");
        when(courseService.getCourseById("COMSW4156")).thenReturn(
                Optional.of(course));

        mockMvc.perform(get("/courses/COMSW4156"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("ASE"));
    }

    /**
     * Tests GET /courses/{courseId} when course is missing returns 404.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testGetCourseByIdNotFound() throws Exception {
        when(courseService.getCourseById("NON_EXISTENT")).thenReturn(
                Optional.empty());

        mockMvc.perform(get("/courses/NON_EXISTENT"))
                .andExpect(status().isNotFound());
    }
}
