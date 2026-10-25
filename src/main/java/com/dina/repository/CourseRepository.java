package com.dina.repository;

import com.dina.model.Course;
import com.dina.model.DegreeProgram;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing and querying courses and programs.
 */
public interface CourseRepository {

    /**
     * Retrieves all courses in the catalog.
     *
     * @return list of all courses
     */
    List<Course> findAllCourses();

    /**
     * Finds a course by its unique course ID.
     *
     * @param courseId unique course identifier
     * @return optional containing course if found
     */
    Optional<Course> findCourseById(String courseId);

    /**
     * Filters courses by multiple search criteria.
     *
     * @param department department filter (e.g., COMS)
     * @param level course level (e.g., 4000)
     * @param credits credit count
     * @param term semester offered (e.g., FALL)
     * @param keyword search keyword matching title/description
     * @return list of matching courses
     */
    List<Course> searchCourses(
            String department,
            Integer level,
            Integer credits,
            String term,
            String keyword
    );

    /**
     * Retrieves a degree program by program ID and catalog year.
     *
     * @param programId program ID
     * @param catalogYear catalog year (nullable)
     * @return optional containing degree program if found
     */
    Optional<DegreeProgram> findProgramById(
            String programId,
            Integer catalogYear
    );

    /**
     * Saves or updates a course in the datastore.
     *
     * @param course course to save
     */
    void saveCourse(Course course);

    /**
     * Saves or updates a degree program.
     *
     * @param program degree program to save
     */
    void saveProgram(DegreeProgram program);
}
