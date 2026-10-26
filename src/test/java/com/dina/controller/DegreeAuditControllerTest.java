package com.dina.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dina.model.DegreeAuditRequest;
import com.dina.model.DegreeAuditResult;
import com.dina.service.DegreeAuditService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * WebMvc unit tests for DegreeAuditController endpoints.
*/
@WebMvcTest(DegreeAuditController.class)
public final class DegreeAuditControllerTest {

    /** MockMvc test client. */
    @Autowired
    private MockMvc mockMvc;

    /** Mocked DegreeAuditService. */
    @MockBean
    private DegreeAuditService degreeAuditService;

    /**
     * Tests GET /degree-audit with valid query parameters.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testAuditGetSuccess() throws Exception {
        final DegreeAuditResult result = new DegreeAuditResult(
                "CS_BS", 120, 120, true,
                Collections.singletonList("COMSW4156"),
                Collections.emptyList(),
                Collections.emptyList()
        );
        when(degreeAuditService.auditDegreeProgress(any())).thenReturn(result);

        mockMvc.perform(get("/degree-audit")
                        .param("programId", "CS_BS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.programId").value("CS_BS"))
                .andExpect(jsonPath("$.degreeCompleted").value(true));
    }

    /**
     * Tests GET /degree-audit with missing programId returns 400 Bad Request.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testAuditGetMissingProgramId() throws Exception {
        mockMvc.perform(get("/degree-audit"))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] In-progress query param test fixture
    // -------------------------------------------------------------------------

    /**
     * Tests GET /degree-audit wires inProgressCourses query params
     * through to the service.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testAuditGetInProgressCoursesWired() throws Exception {
        final DegreeAuditResult result = new DegreeAuditResult(
                "CS_BS", 0, 120, false,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );
        when(degreeAuditService.auditDegreeProgress(any())).thenReturn(result);

        mockMvc.perform(get("/degree-audit")
                        .param("programId", "CS_BS")
                        .param("inProgressCourses", "COMSW4118"))
                .andExpect(status().isOk());

        final ArgumentCaptor<DegreeAuditRequest> captor =
                ArgumentCaptor.forClass(DegreeAuditRequest.class);
        verify(degreeAuditService).auditDegreeProgress(captor.capture());
        assertTrue(captor.getValue().getInProgressCourses()
                .contains("COMSW4118"));
    }

    // -------------------------------------------------------------------------
    // [AI-Assisted: Claude 5 Sonnet] Malformed-body 400 test fixture
    // -------------------------------------------------------------------------

    /**
     * Tests GET /degree-audit with a body missing programId returns
     * 400 instead of silently falling back to query params.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testAuditGetBodyMissingProgramIdIsBadRequest()
            throws Exception {
        final String payload = """
                {
                    "catalogYear": 2026
                }
                """;

        mockMvc.perform(get("/degree-audit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests POST /degree-audit with JSON body.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testAuditPostSuccess() throws Exception {
        final DegreeAuditResult result = new DegreeAuditResult(
                "CS_MS", 30, 30, true,
                Collections.singletonList("COMSW4156"),
                Collections.emptyList(),
                Collections.emptyList()
        );
        when(degreeAuditService.auditDegreeProgress(any())).thenReturn(result);

        final String payload = """
                {
                    "programId": "CS_MS",
                    "catalogYear": 2026,
                    "completedCourses": []
                }
                """;

        mockMvc.perform(post("/degree-audit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.programId").value("CS_MS"));
    }
}
