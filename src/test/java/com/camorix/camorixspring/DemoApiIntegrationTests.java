package com.camorix.camorixspring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("demo")
class DemoApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void servesDemoDashboard() throws Exception {
        mockMvc.perform(get("/demo.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("LOCAL DEMO")))
                .andExpect(content().string(containsString("FISHEYE CAMERA")));
    }

    @Test
    void loadsSeedPatientsWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].roomNumber").exists())
                .andExpect(jsonPath("$[0].bedId").exists());
    }

    @Test
    void acceptsCameraFrameAndStoresFallEvent() throws Exception {
        String frameJson = """
                {
                  "capturedAt": "2026-07-23 12:00:00",
                  "width": 1280,
                  "height": 720,
                  "maxSeverity": "FALL",
                  "roomNumber": "TEST-ROOM",
                  "fallCount": 1,
                  "dangerCount": 0,
                  "personList": [
                    {
                      "severity": "FALL",
                      "poseType": "LYING",
                      "centerX": 640,
                      "centerY": 520,
                      "width": 210,
                      "height": 100
                    }
                  ],
                  "itemList": [
                    {
                      "itemType": "BED",
                      "centerX": 640,
                      "centerY": 400,
                      "width": 420,
                      "height": 220,
                      "horz": "RIGHT",
                      "identifier": "BED-1"
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/realtime/frame")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(frameJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/realtime/event"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomNumber").value("TEST-ROOM"))
                .andExpect(jsonPath("$[0].severity").value("FALL"));
    }
}
