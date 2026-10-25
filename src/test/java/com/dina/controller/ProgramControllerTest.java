package com.dina.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dina.model.DegreeProgram;
import com.dina.service.DegreeProgramService;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * WebMvc unit tests for ProgramController retrieving degree requirements.
*/
@WebMvcTest(ProgramController.class)
public final class ProgramControllerTest {

    /** MockMvc test harness. */
    @Autowired
    private MockMvc mockMvc;

    /** Mocked DegreeProgramService. */
    @MockBean
    private DegreeProgramService degreeProgramService;

    /**
     * Tests GET /programs/{programId}/requirements when program is found.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testGetProgramRequirementsFound() throws Exception {
        final DegreeProgram prog = new DegreeProgram(
                "CS_BS", "BS CS", 2026, 120,
                Collections.singletonList("COMSW1004"),
                Collections.emptyList(), "C-"
        );
        when(degreeProgramService.getProgramRequirements("CS_BS", null))
                .thenReturn(Optional.of(prog));

        mockMvc.perform(get("/programs/CS_BS/requirements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.programId").value("CS_BS"))
                .andExpect(jsonPath("$.totalCreditsRequired").value(120));
    }

    /**
     * Tests GET /programs/{programId}/requirements when program not found.
     *
     * @throws Exception if request fails
     */
    @Test
    public void testGetProgramRequirementsNotFound() throws Exception {
        when(degreeProgramService.getProgramRequirements("UNKNOWN", null))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/programs/UNKNOWN/requirements"))
                .andExpect(status().isNotFound());
    }
}
