package com.dina.service;

import com.dina.model.Course;
import com.dina.repository.CourseRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service implementation for course search and catalog retrieval.
*/
@Service
public final class CourseService {

    /** Repository for accessing course data. */
    private final CourseRepository courseRepository;

    /**
     * Constructs CourseService with injected repository.
     *
     * @param courseRepository course data repository
     */
    public CourseService(final CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieves all available courses.
     *
     * @return list of courses
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAllCourses();
    }

    /**
     * Finds a course by ID.
     *
     * @param courseId course identifier
     * @return optional course
     */
    public Optional<Course> getCourseById(final String courseId) {
        return courseRepository.findCourseById(courseId);
    }

    /**
     * Searches courses by multiple filter criteria.
     *
     * @param department department filter
     * @param level level filter
     * @param credits credit filter
     * @param term term filter
     * @param keyword keyword filter
     * @return list of matching courses
     */
    public List<Course> searchCourses(
            final String department,
            final Integer level,
            final Integer credits,
            final String term,
            final String keyword
    ) {
        return courseRepository.searchCourses(
                department, level, credits, term, keyword);
    }
}
