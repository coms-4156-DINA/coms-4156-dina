package com.dina.controller;

import com.dina.model.Course;
import com.dina.service.CourseService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing Operation 1: Search and retrieve courses.
*/
@RestController
@RequestMapping("/courses")
public final class CourseController {

    /** Service for course retrieval and searching. */
    private final CourseService courseService;

    /**
     * Constructs CourseController with injected CourseService.
     *
     * @param courseService course service
     */
    public CourseController(final CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Search and retrieve courses matching optional filter criteria.
     *
     * @param department optional department filter
     * @param level optional level filter
     * @param credits optional credits filter
     * @param term optional term filter
     * @param keyword optional keyword search
     * @return list of matching courses
     */
    @GetMapping
    public ResponseEntity<List<Course>> getCourses(
            @RequestParam(required = false) final String department,
            @RequestParam(required = false) final Integer level,
            @RequestParam(required = false) final Integer credits,
            @RequestParam(required = false) final String term,
            @RequestParam(required = false) final String keyword
    ) {
        final List<Course> matches = courseService.searchCourses(
                department, level, credits, term, keyword);
        return ResponseEntity.ok(matches);
    }

    /**
     * Retrieve single course details by unique course identifier.
     *
     * @param courseId unique course identifier
     * @return course details or 404 NOT_FOUND
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(
            @PathVariable final String courseId
    ) {
        final Optional<Course> optCourse =
                courseService.getCourseById(courseId);
        return optCourse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(
                        HttpStatus.NOT_FOUND).build());
    }
}
