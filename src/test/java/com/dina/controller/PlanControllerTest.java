package com.dina.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dina.model.ScheduleValidationResult;
import com.dina.service.PlanValidationService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
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
     * Tests GET /plans/validate with query parameters.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testValidateGet() throws Exception {
        final ScheduleValidationResult result =
                new ScheduleValidationResult(true, 3, Collections.emptyList());
        when(planValidationService.validateSchedule(any())).thenReturn(result);

        mockMvc.perform(get("/plans/validate")
                        .param("courseIds", "COMSW4156")
                        .param("targetTerm", "FALL2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.totalCredits").value(3));
    }

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
}
