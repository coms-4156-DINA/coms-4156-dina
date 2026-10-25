package com.dina.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import org.junit.jupiter.api.Test;

/**
 * Unit tests verifying domain model methods, equals, and hashCode.
 */
public final class ModelTest {

    /**
     * Tests Course equals, hashCode, getters, and setters.
     */
    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Automated model branch & equality tests
    // -------------------------------------------------------------------------
    @Test
    public void testCourseModel() {
        final Course c1 = new Course(
                "COMSW4156", "COMS", 4156, "ASE", 3, null
        );
        final Course c2 = new Course(
                "COMSW4156", "COMS", 4156, "ASE", 3,
                Collections.emptyList()
        );
        final Course c3 = new Course(
                "COMSW3157", "COMS", 3157, "AP", 4, null
        );

        assertEquals(c1, c1);
        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, null);
        assertNotEquals(c1, "DifferentType");
        assertEquals(c1.hashCode(), c2.hashCode());

        c1.setCourseId("COMSW4156");
        c1.setDepartment("COMS");
        c1.setCourseNumber(4156);
        c1.setTitle("ASE");
        c1.setCredits(3);
        c1.setPrerequisites(null);
        c1.setSemestersOffered(null);
        c1.setMeetingTimes(null);
        c1.setMinPassingGrade("C-");
        c1.setDescription("Desc");

        assertEquals("COMSW4156", c1.getCourseId());
        assertEquals("COMS", c1.getDepartment());
        assertEquals(4156, c1.getCourseNumber());
        assertEquals("ASE", c1.getTitle());
        assertEquals(3, c1.getCredits());
        assertEquals("C-", c1.getMinPassingGrade());
        assertEquals("Desc", c1.getDescription());
        assertTrue(c1.getPrerequisites().isEmpty());
        assertTrue(c1.getSemestersOffered().isEmpty());
        assertTrue(c1.getMeetingTimes().isEmpty());
    }

    /**
     * Tests CompletedCourse equals, hashCode, and accessors.
     */
    @Test
    public void testCompletedCourseModel() {
        final CompletedCourse cc1 = new CompletedCourse(
                "COMSW1004", "FALL2025", "A", 3);
        final CompletedCourse cc2 = new CompletedCourse(
                "COMSW1004", "FALL2025", "A", 3);
        final CompletedCourse cc3 = new CompletedCourse(
                "COMSW3134", "SPRING2026", "B+", 3);

        assertEquals(cc1, cc1);
        assertEquals(cc1, cc2);
        assertNotEquals(cc1, cc3);
        assertNotEquals(cc1, null);
        assertNotEquals(cc1, "Object");
        assertEquals(cc1.hashCode(), cc2.hashCode());

        cc1.setCourseId("COMSW1004");
        cc1.setTerm("FALL2025");
        cc1.setGrade("A");
        cc1.setCredits(3);

        assertEquals("COMSW1004", cc1.getCourseId());
        assertEquals("FALL2025", cc1.getTerm());
        assertEquals("A", cc1.getGrade());
        assertEquals(3, cc1.getCredits());
    }

    /**
     * Tests ValidationIssue equals, hashCode, and getters.
     */
    @Test
    public void testValidationIssueModel() {
        final ValidationIssue vi1 = new ValidationIssue(
                "CONFLICT", "COMSW4156", "COMSW1004", "Collision");
        final ValidationIssue vi2 = new ValidationIssue(
                "CONFLICT", "COMSW4156", "COMSW1004", "Collision");
        final ValidationIssue vi3 = new ValidationIssue(
                "UNKNOWN", "COMSW9999", null, "Not found");

        assertEquals(vi1, vi1);
        assertEquals(vi1, vi2);
        assertNotEquals(vi1, vi3);
        assertNotEquals(vi1, null);
        assertNotEquals(vi1, 123);
        assertEquals(vi1.hashCode(), vi2.hashCode());

        vi1.setType("CONFLICT");
        vi1.setCourseId("COMSW4156");
        vi1.setConflictingCourseId("COMSW1004");
        vi1.setMessage("Collision");

        assertEquals("CONFLICT", vi1.getType());
        assertEquals("COMSW4156", vi1.getCourseId());
        assertEquals("COMSW1004", vi1.getConflictingCourseId());
        assertEquals("Collision", vi1.getMessage());
    }

    /**
     * Tests DegreeProgram equals, hashCode, and accessors.
     */
    @Test
    public void testDegreeProgramModel() {
        final DegreeProgram dp1 = new DegreeProgram(
                "CS_BS", "BS CS", 2026, 120, null, null, "C-");
        final DegreeProgram dp2 = new DegreeProgram(
                "CS_BS", "BS CS", 2026, 120,
                Collections.emptyList(), Collections.emptyList(), "C-");
        final DegreeProgram dp3 = new DegreeProgram(
                "CS_MS", "MS CS", 2026, 30, null, null, "B-");

        assertEquals(dp1, dp1);
        assertEquals(dp1, dp2);
        assertNotEquals(dp1, dp3);
        assertNotEquals(dp1, null);
        assertNotEquals(dp1, "String");
        assertEquals(dp1.hashCode(), dp2.hashCode());

        dp1.setProgramId("CS_BS");
        dp1.setProgramName("BS CS");
        dp1.setCatalogYear(2026);
        dp1.setTotalCreditsRequired(120);
        dp1.setRequiredCourseIds(null);
        dp1.setElectiveCourseIds(null);
        dp1.setMinPassingGrade("C-");

        assertEquals("CS_BS", dp1.getProgramId());
        assertEquals("BS CS", dp1.getProgramName());
        assertEquals(2026, dp1.getCatalogYear());
        assertEquals(120, dp1.getTotalCreditsRequired());
        assertEquals("C-", dp1.getMinPassingGrade());
        assertTrue(dp1.getRequiredCourseIds().isEmpty());
        assertTrue(dp1.getElectiveCourseIds().isEmpty());
    }

    /**
     * Tests ScheduleValidationRequest and Result models.
     */
    @Test
    public void testScheduleValidationModels() {
        final ScheduleValidationRequest req = new ScheduleValidationRequest(
                null, null, null, "FALL2026");
        assertNotNull(req.getCourseIds());
        assertNotNull(req.getCompletedCourses());
        assertNotNull(req.getCurrentEnrollment());
        assertEquals("FALL2026", req.getTargetTerm());

        req.setCourseIds(null);
        req.setCompletedCourses(null);
        req.setCurrentEnrollment(null);
        req.setTargetTerm("SPRING2027");

        assertTrue(req.getCourseIds().isEmpty());
        assertTrue(req.getCompletedCourses().isEmpty());
        assertTrue(req.getCurrentEnrollment().isEmpty());
        assertEquals("SPRING2027", req.getTargetTerm());

        final ScheduleValidationResult res = new ScheduleValidationResult(
                true, 15, null);
        assertTrue(res.isValid());
        assertEquals(15, res.getTotalCredits());
        assertTrue(res.getIssues().isEmpty());

        res.setValid(false);
        res.setTotalCredits(18);
        res.setIssues(null);
        assertFalse(res.isValid());
        assertEquals(18, res.getTotalCredits());
        assertTrue(res.getIssues().isEmpty());
    }

    /**
     * Tests DegreeAuditRequest and Result models.
     */
    @Test
    public void testDegreeAuditModels() {
        final DegreeAuditRequest req = new DegreeAuditRequest(
                "CS_BS", 2026, null, null);
        assertEquals("CS_BS", req.getProgramId());
        assertEquals(2026, req.getCatalogYear());
        assertTrue(req.getCompletedCourses().isEmpty());
        assertTrue(req.getInProgressCourses().isEmpty());

        req.setProgramId("CS_MS");
        req.setCatalogYear(2025);
        req.setCompletedCourses(null);
        req.setInProgressCourses(null);
        assertEquals("CS_MS", req.getProgramId());
        assertEquals(2025, req.getCatalogYear());

        final DegreeAuditResult res = new DegreeAuditResult(
                "CS_BS", 100, 120, false, null, null, null);
        assertEquals("CS_BS", res.getProgramId());
        assertEquals(100, res.getCreditsEarned());
        assertEquals(120, res.getCreditsRequired());
        assertFalse(res.isDegreeCompleted());
        assertTrue(res.getCompletedRequirements().isEmpty());
        assertTrue(res.getRemainingRequirements().isEmpty());
        assertTrue(res.getCoursesStillNeeded().isEmpty());

        res.setProgramId("CS_MS");
        res.setCreditsEarned(30);
        res.setCreditsRequired(30);
        res.setDegreeCompleted(true);
        res.setCompletedRequirements(null);
        res.setRemainingRequirements(null);
        res.setCoursesStillNeeded(null);

        assertEquals("CS_MS", res.getProgramId());
        assertEquals(30, res.getCreditsEarned());
        assertEquals(30, res.getCreditsRequired());
        assertTrue(res.isDegreeCompleted());
        assertTrue(res.getCompletedRequirements().isEmpty());
    }
}
