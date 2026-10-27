package com.dina.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dina.model.CompletedCourse;
import com.dina.model.ScheduleValidationRequest;
import com.dina.model.ScheduleValidationResult;
import com.dina.model.ValidationIssue;
import com.dina.service.PlanValidationService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * WebMvc unit tests for PlanController validating semester schedules.
*/
@WebMvcTest(PlanController.class)
public final class PlanControllerTest {

    /** MockMvc test harness. */
    @Autowired
    private MockMvc mockMvc;

    /** Mocked PlanValidationService. */
    @MockBean
    private PlanValidationService planValidationService;

    /**
     * Tests POST /plans/validate with JSON body.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testValidatePost() throws Exception {
        final ScheduleValidationResult result =
                new ScheduleValidationResult(true, 3, Collections.emptyList());
        when(planValidationService.validateSchedule(any())).thenReturn(result);

        final String payload = """
                {
                    "courseIds": ["COMSW4156"],
                    "completedCourses": [],
                    "currentEnrollment": [],
                    "targetTerm": "FALL2026"
                }
                """;

        mockMvc.perform(post("/plans/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude Opus 5.5] Request/response contract tests
    // The tests below were written with AI assistance.
    // -------------------------------------------------------------------------

    /**
     * Tests GET /plans/validate is no longer supported.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testValidateGetNotSupported() throws Exception {
        mockMvc.perform(get("/plans/validate")
                        .param("courseIds", "COMSW4156")
                        .param("targetTerm", "FALL2026"))
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(planValidationService);
    }

    /**
     * Tests the JSON body is deserialized into the request passed to the
     * service, including nested completed course records.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testValidatePostForwardsRequest() throws Exception {
        when(planValidationService.validateSchedule(any())).thenReturn(
                new ScheduleValidationResult(true, 3,
                        Collections.emptyList()));

        final String payload = """
                {
                    "courseIds": ["COMSW3134"],
                    "completedCourses": [
                        {"courseId": "COMSW1004", "term": "FALL2025",
                         "grade": "B+", "credits": 3}
                    ],
                    "currentEnrollment": ["MATH1201"],
                    "targetTerm": "SPRING2026"
                }
                """;

        mockMvc.perform(post("/plans/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        final ArgumentCaptor<ScheduleValidationRequest> captor =
                ArgumentCaptor.forClass(ScheduleValidationRequest.class);
        verify(planValidationService).validateSchedule(captor.capture());
        final ScheduleValidationRequest sent = captor.getValue();

        assertEquals(List.of("COMSW3134"), sent.getCourseIds());
        assertEquals(List.of("MATH1201"), sent.getCurrentEnrollment());
        assertEquals("SPRING2026", sent.getTargetTerm());
        assertEquals(List.of(
                new CompletedCourse("COMSW1004", "FALL2025", "B+", 3)),
                sent.getCompletedCourses());
    }

    /**
     * Tests an invalid schedule still returns 200 with every issue field
     * serialized in the response body.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testValidatePostReturnsIssues() throws Exception {
        final ValidationIssue issue = new ValidationIssue(
                "UNMET_PREREQUISITE", "COMSW3134", "COMSW1004",
                "Prerequisite COMSW1004 not met for COMSW3134.");
        when(planValidationService.validateSchedule(any())).thenReturn(
                new ScheduleValidationResult(false, 3, List.of(issue)));

        mockMvc.perform(post("/plans/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseIds\": [\"COMSW3134\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.totalCredits").value(3))
                .andExpect(jsonPath("$.issues.length()").value(1))
                .andExpect(jsonPath("$.issues[0].type")
                        .value("UNMET_PREREQUISITE"))
                .andExpect(jsonPath("$.issues[0].courseId")
                        .value("COMSW3134"))
                .andExpect(jsonPath("$.issues[0].conflictingCourseId")
                        .value("COMSW1004"))
                .andExpect(jsonPath("$.issues[0].message")
                        .value("Prerequisite COMSW1004 not met for "
                                + "COMSW3134."));
    }

    /**
     * Tests malformed JSON is rejected with 400 before reaching the service.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testValidatePostMalformedJson() throws Exception {
        mockMvc.perform(post("/plans/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseIds\": [\"COMSW1004\""))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(planValidationService);
    }
}
