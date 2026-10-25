package com.dina.repository;

import com.dina.model.Course;
import com.dina.model.DegreeProgram;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/**
 * In-memory thread-safe implementation of CourseRepository.
*/
@Repository
public final class AcademicDataStore implements CourseRepository {

    /** Map of course ID to Course object. */
    private final Map<String, Course> courses = new ConcurrentHashMap<>();

    /** Map of program ID and year compound key to DegreeProgram. */
    private final Map<String, DegreeProgram> programs =
            new ConcurrentHashMap<>();

    /**
     * Initializes repository and populates initial academic catalog data.
     */
    public AcademicDataStore() {
        populateDefaultCourses();
        populateDefaultPrograms();
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Seed course catalog dictionary data
    // -------------------------------------------------------------------------
    private void populateDefaultCourses() {
        saveCourse(new Course(
                "COMSW1004", "COMS", 1004,
                "Introduction to Computer Science in Java", 3,
                Collections.emptyList()
        ).withSemestersOffered(Arrays.asList("FALL", "SPRING"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Basic programming concepts and algorithms."));

        saveCourse(new Course(
                "COMSW3134", "COMS", 3134,
                "Data Structures in Java", 3,
                Collections.singletonList("COMSW1004")
        ).withSemestersOffered(Arrays.asList("FALL", "SPRING"))
         .withMeetingTimes(Collections.singletonList("TR 13:10-14:25"))
         .withMinPassingGrade("C-")
         .withDescription("Data structures, hash tables, trees, graphs."));

        saveCourse(new Course(
                "COMSW3157", "COMS", 3157,
                "Advanced Programming in C and Systems", 4,
                Collections.singletonList("COMSW3134")
        ).withSemestersOffered(Arrays.asList("FALL", "SPRING"))
         .withMeetingTimes(Collections.singletonList("MW 16:10-17:25"))
         .withMinPassingGrade("C-")
         .withDescription("C programming, systems, and memory management."));

        saveCourse(new Course(
                "COMSW3203", "COMS", 3203,
                "Discrete Mathematics", 4,
                Collections.singletonList("COMSW1004")
        ).withSemestersOffered(Arrays.asList("FALL", "SPRING"))
         .withMeetingTimes(Collections.singletonList("TR 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Logic, sets, proofs, and combinatorics."));

        saveCourse(new Course(
                "COMSW3261", "COMS", 3261,
                "Computer Science Theory", 3,
                Arrays.asList("COMSW3134", "COMSW3203")
        ).withSemestersOffered(Arrays.asList("FALL", "SPRING"))
         .withMeetingTimes(Collections.singletonList("MW 14:40-15:55"))
         .withMinPassingGrade("C-")
         .withDescription("Regular expressions, grammars, Turing machines."));

        saveCourse(new Course(
                "COMSW3827", "COMS", 3827,
                "Fundamentals of Computer Systems", 3,
                Collections.singletonList("COMSW1004")
        ).withSemestersOffered(Arrays.asList("FALL", "SPRING"))
         .withMeetingTimes(Collections.singletonList("TR 14:40-15:55"))
         .withMinPassingGrade("C-")
         .withDescription("Logic design, processors, pipelines, and buses."));

        saveCourse(new Course(
                "COMSW4118", "COMS", 4118,
                "Operating Systems", 3,
                Arrays.asList("COMSW3157", "COMSW3827")
        ).withSemestersOffered(Arrays.asList("FALL", "SPRING"))
         .withMeetingTimes(Collections.singletonList("TR 16:10-17:25"))
         .withMinPassingGrade("C-")
         .withDescription("Concurrency, scheduling, virtualization."));

        saveCourse(new Course(
                "COMSW4156", "COMS", 4156,
                "Advanced Software Engineering", 3,
                Collections.singletonList("COMSW3157")
        ).withSemestersOffered(Arrays.asList("FALL", "SPRING"))
         .withMeetingTimes(Collections.singletonList("MW 10:10-11:25"))
         .withMinPassingGrade("C-")
         .withDescription("Service architecture, testing, CI/CD, teamwork."));

        saveCourse(new Course(
                "COMSW4701", "COMS", 4701,
                "Artificial Intelligence", 3,
                Collections.singletonList("COMSW3134")
        ).withSemestersOffered(Arrays.asList("FALL", "SPRING"))
         .withMeetingTimes(Collections.singletonList("TR 11:40-12:55"))
         .withMinPassingGrade("C-")
         .withDescription("Search, knowledge, and machine learning."));

        saveCourse(new Course(
                "MATH1201", "MATH", 1201,
                "Calculus III", 3,
                Collections.emptyList()
        ).withSemestersOffered(Arrays.asList("FALL", "SPRING"))
         .withMeetingTimes(Collections.singletonList("MW 08:40-09:55"))
         .withMinPassingGrade("C-")
         .withDescription("Multivariable calculus and vector functions."));
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Seed degree requirement program trees
    // -------------------------------------------------------------------------
    private void populateDefaultPrograms() {
        saveProgram(new DegreeProgram(
                "CS_BS",
                "Bachelor of Science in Computer Science",
                2026,
                120,
                Arrays.asList(
                        "COMSW1004", "COMSW3134", "COMSW3157",
                        "COMSW3203", "COMSW3261", "COMSW3827", "COMSW4156"
                ),
                Arrays.asList("COMSW4118", "COMSW4701", "MATH1201"),
                "C-"
        ));

        saveProgram(new DegreeProgram(
                "CS_MS",
                "Master of Science in Computer Science",
                2026,
                30,
                Arrays.asList("COMSW4156", "COMSW4118"),
                Collections.singletonList("COMSW4701"),
                "B-"
        ));
    }

    @Override
    public List<Course> findAllCourses() {
        return new ArrayList<>(courses.values());
    }

    @Override
    public Optional<Course> findCourseById(final String courseId) {
        if (courseId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(courses.get(courseId.toUpperCase()));
    }

    @Override
    public List<Course> searchCourses(
            final String department,
            final Integer level,
            final Integer credits,
            final String term,
            final String keyword
    ) {
        final List<Course> results = new ArrayList<>();
        for (final Course course : courses.values()) {
            if (matchesSearch(course, department, level, credits,
                    term, keyword)) {
                results.add(course);
            }
        }
        return results;
    }

    private boolean matchesSearch(
            final Course course,
            final String department,
            final Integer level,
            final Integer credits,
            final String term,
            final String keyword
    ) {
        if (department != null && !department.isBlank()
                && !course.getDepartment().equalsIgnoreCase(department)) {
            return false;
        }
        if (level != null && !matchesLevel(course.getCourseNumber(), level)) {
            return false;
        }
        if (credits != null && course.getCredits() != credits) {
            return false;
        }
        if (term != null && !term.isBlank()
                && !containsTerm(course.getSemestersOffered(), term)) {
            return false;
        }
        if (keyword != null && !keyword.isBlank()) {
            final String lower = keyword.toLowerCase();
            final boolean titleMatch = course.getTitle() != null
                    && course.getTitle().toLowerCase().contains(lower);
            final boolean descMatch = course.getDescription() != null
                    && course.getDescription().toLowerCase().contains(lower);
            final boolean idMatch = course.getCourseId() != null
                    && course.getCourseId().toLowerCase().contains(lower);
            if (!titleMatch && !descMatch && !idMatch) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesLevel(final int courseNumber, final int level) {
        return (courseNumber / 1000) == (level / 1000);
    }

    private boolean containsTerm(
            final List<String> semesters, final String term) {
        final String normalized = term.toUpperCase();
        for (final String sem : semesters) {
            if (normalized.contains(sem) || sem.contains(normalized)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<DegreeProgram> findProgramById(
            final String programId,
            final Integer catalogYear
    ) {
        if (programId == null) {
            return Optional.empty();
        }
        final String normalized = programId.toUpperCase();
        if (catalogYear != null) {
            final String key = normalized + "_" + catalogYear;
            if (programs.containsKey(key)) {
                return Optional.of(programs.get(key));
            }
        }
        for (final Map.Entry<String, DegreeProgram> entry
                : programs.entrySet()) {
            if (entry.getValue().getProgramId().equalsIgnoreCase(normalized)) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveCourse(final Course course) {
        if (course != null && course.getCourseId() != null) {
            courses.put(course.getCourseId().toUpperCase(), course);
        }
    }

    @Override
    public void saveProgram(final DegreeProgram program) {
        if (program != null && program.getProgramId() != null) {
            final String key = program.getProgramId().toUpperCase()
                    + "_" + program.getCatalogYear();
            programs.put(key, program);
        }
    }
}
