package com.dina.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.dina.model.CompletedCourse;
import com.dina.model.DegreeAuditRequest;
import com.dina.model.DegreeAuditResult;
import com.dina.model.DegreeProgram;
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
 * Unit tests for DegreeAuditService verifying degree audit logic.
*/
@ExtendWith(MockitoExtension.class)
public final class DegreeAuditServiceTest {

    /** Mock repository test double. */
    @Mock
    private CourseRepository courseRepository;

    /** DegreeAuditService under test. */
    private DegreeAuditService service;

    /**
     * Test setup.
     */
    @BeforeEach
    public void setUp() {
        service = new DegreeAuditService(courseRepository);
    }

    /**
     * Tests audit with unknown program throws exception.
     */
    @Test
    public void testAuditUnknownProgram() {
        when(courseRepository.findProgramById("UNKNOWN", null)).thenReturn(
                Optional.empty());

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "UNKNOWN", null, Collections.emptyList(),
                Collections.emptyList());

        assertThrows(IllegalArgumentException.class, () ->
                service.auditDegreeProgress(req));
    }

    /**
     * Tests audit when all degree requirements are fulfilled.
     */
    @Test
    public void testAuditCompleteDegree() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_MS", "MS in CS", 2026, 6,
                Arrays.asList("COMSW4156", "COMSW4118"),
                Collections.emptyList(), "B-"
        );
        when(courseRepository.findProgramById("CS_MS", 2026)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_MS", 2026,
                Arrays.asList(
                        new CompletedCourse("COMSW4156", "FALL2025", "A", 3),
                        new CompletedCourse("COMSW4118", "SPRING2026", "A", 3)
                ),
                Collections.emptyList()
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertTrue(result.isDegreeCompleted());
        assertEquals(6, result.getCreditsEarned());
        assertEquals(2, result.getCompletedRequirements().size());
        assertTrue(result.getRemainingRequirements().isEmpty());
    }

    /**
     * Tests audit when core courses are missing.
     */
    @Test
    public void testAuditMissingRequirements() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_MS", "MS in CS", 2026, 6,
                Arrays.asList("COMSW4156", "COMSW4118"),
                Collections.emptyList(), "B-"
        );
        when(courseRepository.findProgramById("CS_MS", null)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_MS", null,
                Collections.singletonList(
                        new CompletedCourse("COMSW4156", "FALL2025", "A", 3)),
                Collections.emptyList()
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertFalse(result.isDegreeCompleted());
        assertEquals(3, result.getCreditsEarned());
        assertEquals(1, result.getRemainingRequirements().size());
        assertEquals("COMSW4118", result.getRemainingRequirements().get(0));
    }

    /**
     * Tests failing grade (F) is not counted towards degree progress.
     */
    @Test
    public void testAuditFailingGradeIgnored() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_MS", "MS in CS", 2026, 6,
                Arrays.asList("COMSW4156", "COMSW4118"),
                Collections.emptyList(), "B-"
        );
        when(courseRepository.findProgramById("CS_MS", null)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_MS", null,
                Arrays.asList(
                        new CompletedCourse("COMSW4156", "FALL2025", "F", 3),
                        new CompletedCourse("COMSW4118", "SPRING2026", "A", 3)
                ),
                Collections.emptyList()
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertFalse(result.isDegreeCompleted());
        assertEquals(3, result.getCreditsEarned());
        assertTrue(result.getRemainingRequirements().contains("COMSW4156"));
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Grade-minimum enforcement test fixture
    // -------------------------------------------------------------------------

    /**
     * Tests a grade below the program's minimum does not count.
     */
    @Test
    public void testAuditGradeBelowProgramMinimum() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_MS", "MS in CS", 2026, 6,
                Arrays.asList("COMSW4156", "COMSW4118"),
                Collections.emptyList(), "B-"
        );
        when(courseRepository.findProgramById("CS_MS", null)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_MS", null,
                Arrays.asList(
                        new CompletedCourse("COMSW4156", "FALL2025", "C", 3),
                        new CompletedCourse("COMSW4118", "SPRING2026", "A", 3)
                ),
                Collections.emptyList()
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertFalse(result.isDegreeCompleted());
        assertEquals(3, result.getCreditsEarned());
        assertTrue(result.getRemainingRequirements().contains("COMSW4156"));
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Grade-minimum enforcement test fixture
    // -------------------------------------------------------------------------

    /**
     * Tests a non-letter pass grade does not satisfy a real program
     * minimum, since it can't be numerically verified against one.
     */
    @Test
    public void testAuditNonLetterGradeFailsWithMinimum() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_MS", "MS in CS", 2026, 6,
                Arrays.asList("COMSW4156", "COMSW4118"),
                Collections.emptyList(), "B-"
        );
        when(courseRepository.findProgramById("CS_MS", null)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_MS", null,
                Arrays.asList(
                        new CompletedCourse("COMSW4156", "FALL2025", "P", 3),
                        new CompletedCourse("COMSW4118", "SPRING2026", "A", 3)
                ),
                Collections.emptyList()
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertFalse(result.isDegreeCompleted());
        assertEquals(3, result.getCreditsEarned());
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Retake/duplicate-credit test fixture
    // -------------------------------------------------------------------------

    /**
     * Tests a later failing retake does not un-complete an earlier pass.
     */
    @Test
    public void testAuditRetakeKeepsEarlierPass() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_MS", "MS in CS", 2026, 6,
                Arrays.asList("COMSW4156", "COMSW4118"),
                Collections.emptyList(), "B-"
        );
        when(courseRepository.findProgramById("CS_MS", null)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_MS", null,
                Arrays.asList(
                        new CompletedCourse("COMSW4156", "FALL2025", "A", 3),
                        new CompletedCourse(
                                "COMSW4156", "SPRING2026", "C", 3),
                        new CompletedCourse("COMSW4118", "SPRING2026", "A", 3)
                ),
                Collections.emptyList()
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertTrue(result.isDegreeCompleted());
        assertEquals(6, result.getCreditsEarned());
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Retake/duplicate-credit test fixture
    // -------------------------------------------------------------------------

    /**
     * Tests a later passing retake completes a course that first failed.
     */
    @Test
    public void testAuditRetakeImprovementCounts() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_MS", "MS in CS", 2026, 6,
                Arrays.asList("COMSW4156", "COMSW4118"),
                Collections.emptyList(), "B-"
        );
        when(courseRepository.findProgramById("CS_MS", null)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_MS", null,
                Arrays.asList(
                        new CompletedCourse("COMSW4156", "FALL2025", "C", 3),
                        new CompletedCourse(
                                "COMSW4156", "SPRING2026", "A", 3),
                        new CompletedCourse("COMSW4118", "SPRING2026", "A", 3)
                ),
                Collections.emptyList()
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertTrue(result.isDegreeCompleted());
        assertEquals(6, result.getCreditsEarned());
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Retake/duplicate-credit test fixture
    // -------------------------------------------------------------------------

    /**
     * Tests a duplicate passing course entry is not double-counted.
     */
    @Test
    public void testAuditDuplicatePassingCourseNoDoubleCount() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_MS", "MS in CS", 2026, 6,
                Arrays.asList("COMSW4156", "COMSW4118"),
                Collections.emptyList(), "B-"
        );
        when(courseRepository.findProgramById("CS_MS", null)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_MS", null,
                Arrays.asList(
                        new CompletedCourse("COMSW4156", "FALL2025", "A", 3),
                        new CompletedCourse("COMSW4156", "FALL2025", "A", 3),
                        new CompletedCourse("COMSW4118", "SPRING2026", "A", 3)
                ),
                Collections.emptyList()
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertEquals(6, result.getCreditsEarned());
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Elective-category test fixture
    // -------------------------------------------------------------------------

    /**
     * Tests completed and remaining electives are both reported.
     */
    @Test
    public void testAuditElectiveTracking() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_TEST", "Test Program", 2026, 9,
                Collections.singletonList("COMSW4156"),
                Arrays.asList("COMSW4118", "COMSW4701"), "C-"
        );
        when(courseRepository.findProgramById("CS_TEST", null)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_TEST", null,
                Arrays.asList(
                        new CompletedCourse("COMSW4156", "FALL2025", "A", 3),
                        new CompletedCourse("COMSW4118", "SPRING2026", "A", 3)
                ),
                Collections.emptyList()
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertFalse(result.isDegreeCompleted());
        assertEquals(6, result.getCreditsEarned());
        assertTrue(result.getCompletedRequirements()
                .contains("ELECTIVE: COMSW4118"));
        assertTrue(result.getCoursesStillNeeded()
                .contains("Elective option: COMSW4701"));
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] In-progress-course test fixture
    // -------------------------------------------------------------------------

    /**
     * Tests an in-progress course is tagged and excluded from needs.
     */
    @Test
    public void testAuditInProgressCourseTaggedAndExcluded() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_MS", "MS in CS", 2026, 6,
                Arrays.asList("COMSW4156", "COMSW4118"),
                Collections.emptyList(), "B-"
        );
        when(courseRepository.findProgramById("CS_MS", null)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_MS", null,
                Collections.singletonList(
                        new CompletedCourse("COMSW4156", "FALL2025", "A", 3)),
                Collections.singletonList("COMSW4118")
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertFalse(result.isDegreeCompleted());
        assertTrue(result.getRemainingRequirements()
                .contains("COMSW4118 (IN PROGRESS)"));
        assertFalse(result.getCoursesStillNeeded().contains("COMSW4118"));
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Garbage-grade test fixture
    // -------------------------------------------------------------------------

    /**
     * Tests an unrecognized grade does not meet a real program minimum.
     */
    @Test
    public void testAuditGarbageGradeFailsWithMinimum() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_MS", "MS in CS", 2026, 6,
                Arrays.asList("COMSW4156", "COMSW4118"),
                Collections.emptyList(), "B-"
        );
        when(courseRepository.findProgramById("CS_MS", null)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_MS", null,
                Arrays.asList(
                        new CompletedCourse("COMSW4156", "FALL2025", "Z", 3),
                        new CompletedCourse("COMSW4118", "SPRING2026", "A", 3)
                ),
                Collections.emptyList()
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertFalse(result.isDegreeCompleted());
        assertEquals(3, result.getCreditsEarned());
        assertTrue(result.getRemainingRequirements().contains("COMSW4156"));
    }

    /**
     * Tests an unrecognized grade still counts when the program has no
     * configured minimum passing grade to verify it against.
     */
    @Test
    public void testAuditGarbageGradePassesWithoutMinimum() {
        final DegreeProgram prog = new DegreeProgram(
                "CS_NOMIN", "No Minimum Program", 2026, 3,
                Collections.singletonList("COMSW4156"),
                Collections.emptyList(), null
        );
        when(courseRepository.findProgramById("CS_NOMIN", null)).thenReturn(
                Optional.of(prog));

        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_NOMIN", null,
                Collections.singletonList(
                        new CompletedCourse("COMSW4156", "FALL2025", "Z", 3)),
                Collections.emptyList()
        );

        final DegreeAuditResult result = service.auditDegreeProgress(req);

        assertTrue(result.isDegreeCompleted());
        assertEquals(3, result.getCreditsEarned());
    }
}
