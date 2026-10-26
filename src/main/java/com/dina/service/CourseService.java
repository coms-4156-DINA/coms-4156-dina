package com.dina.service;

import com.dina.model.Course;
import com.dina.repository.CourseRepository;
import java.util.ArrayList;
import java.util.Comparator;
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
     * Retrieves all available courses and sorts them by course ID.
     *
     * @return list of sorted courses
     */
    public List<Course> getAllCourses() {
        return sortById(courseRepository.findAllCourses());
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
     * Searches courses by multiple filter criteria. Results are sorted
     * by course ID.
     *
     * @param department department filter
     * @param level level filter
     * @param credits credit filter
     * @param term term filter
     * @param keyword keyword filter
     * @return list of matching courses
     * @throws IllegalArgumentException if level or credits is not positive
     */
    public List<Course> searchCourses(final String department,
            final Integer level, final Integer credits,
            final String term, final String keyword) {

        if (level != null && level <= 0) {
            throw new IllegalArgumentException(
                    "level must be a positive integer");
        }

        if (credits != null && credits <= 0) {
            throw new IllegalArgumentException(
                    "credits must be a positive integer");
        }

        return sortById(courseRepository.searchCourses(
                department, level, credits, term, keyword));
    }

    /**
     * Returns a copy of the given courses sorted by course ID.
     *
     * @param courses courses to sort
     * @return sorted list of courses
     */
    private static List<Course> sortById(final List<Course> courses) {

        final List<Course> sorted = new ArrayList<>(courses);
        sorted.sort(Comparator.comparing(Course::getCourseId,
                Comparator.nullsLast(Comparator.naturalOrder())));
                
        return sorted;
    }

}
