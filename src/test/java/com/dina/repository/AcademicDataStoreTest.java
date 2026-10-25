package com.dina.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dina.model.Course;
import com.dina.model.DegreeProgram;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for AcademicDataStore repository.
 */
public final class AcademicDataStoreTest {

    /** Repository instance under test. */
    private AcademicDataStore dataStore;

    /**
     * Initializes fresh AcademicDataStore before each test.
     */
    @BeforeEach
    public void setUp() {
        dataStore = new AcademicDataStore();
    }

    /**
     * Tests default catalog has seeded courses.
     */
    @Test
    public void testFindAllCourses() {
        final List<Course> all = dataStore.findAllCourses();
        assertNotNull(all);
        assertTrue(all.size() >= 10);
    }

    /**
     * Tests findCourseById for existing and missing course.
     */
    @Test
    public void testFindCourseById() {
        final Optional<Course> found =
                dataStore.findCourseById("COMSW4156");
        assertTrue(found.isPresent());
        assertEquals("COMS", found.get().getDepartment());

        final Optional<Course> missing =
                dataStore.findCourseById("NON_EXISTENT");
        assertFalse(missing.isPresent());
    }

    /**
     * Tests searchCourses with various filters.
     */
    @Test
    public void testSearchCourses() {
        // Filter by department
        final List<Course> comsCourses = dataStore.searchCourses(
                "COMS", null, null, null, null);
        assertFalse(comsCourses.isEmpty());
        for (final Course c : comsCourses) {
            assertEquals("COMS", c.getDepartment());
        }

        // Filter by level
        final List<Course> level4000 = dataStore.searchCourses(
                null, 4000, null, null, null);
        assertFalse(level4000.isEmpty());
        for (final Course c : level4000) {
            assertEquals(4, c.getCourseNumber() / 1000);
        }

        // Filter by keyword
        final List<Course> software = dataStore.searchCourses(
                null, null, null, null, "software");
        assertFalse(software.isEmpty());
        assertEquals("COMSW4156", software.get(0).getCourseId());
    }

    /**
     * Tests findProgramById retrieves seeded programs.
     */
    @Test
    public void testFindProgramById() {
        final Optional<DegreeProgram> csBs =
                dataStore.findProgramById("CS_BS", 2026);
        assertTrue(csBs.isPresent());
        assertEquals(120, csBs.get().getTotalCreditsRequired());

        final Optional<DegreeProgram> csMs =
                dataStore.findProgramById("CS_MS", null);
        assertTrue(csMs.isPresent());
        assertEquals(30, csMs.get().getTotalCreditsRequired());

        final Optional<DegreeProgram> missing =
                dataStore.findProgramById("CIVIL_ENG", null);
        assertFalse(missing.isPresent());
    }
}
