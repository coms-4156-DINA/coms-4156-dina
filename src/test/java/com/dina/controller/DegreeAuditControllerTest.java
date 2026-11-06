package com.dina.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dina.model.DegreeAuditRequest;
import com.dina.model.DegreeAuditResult;
import com.dina.service.DegreeAuditService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
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
