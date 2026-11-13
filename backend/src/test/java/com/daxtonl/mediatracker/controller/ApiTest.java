package com.daxtonl.mediatracker.controller;

import com.daxtonl.mediatracker.repository.TrackerRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// End-to-end HTTP tests: the automated version of api.http.
// Not @Transactional on purpose: each request gets its own transaction, like a real client.
@SpringBootTest
@AutoConfigureMockMvc
class ApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TrackerRepository trackerRepository;

    private long trackerId;
    private long mediaId;

    @BeforeEach
    void setUp() throws Exception {
        trackerId = idOf(postJson("/api/trackers", "{ \"name\": \"Games\" }")
                .andExpect(status().isCreated()));
        mediaId = idOf(postJson("/api/trackers/" + trackerId + "/media",
                "{ \"name\": \"Celeste\", \"type\": \"GAME\", \"length\": null, \"priority\": 2 }")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Celeste"))
                .andExpect(jsonPath("$.status").value("WAITLIST")));
    }

    @AfterEach
    void tearDown() {
        trackerRepository.deleteAll();   // cascades to media and logs
    }

    // ---------- Trackers ----------

    @Test
    void getTracker() throws Exception {
        mvc.perform(get("/api/trackers/" + trackerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Games"))
                .andExpect(jsonPath("$.mediaCount").value(1));
    }

    @Test
    void getMissingTrackerReturns404() throws Exception {
        mvc.perform(get("/api/trackers/999")).andExpect(status().isNotFound());
    }

    @Test
    void nonNumericTrackerIdReturns400() throws Exception {
        mvc.perform(get("/api/trackers/abc")).andExpect(status().isBadRequest());
    }

    // ---------- Adding and deleting media ----------

    @Test
    void addingSameNameAgainReturns409() throws Exception {
        postJson("/api/trackers/" + trackerId + "/media", "{ \"name\": \"Celeste\", \"type\": \"GAME\" }")
                .andExpect(status().isConflict());
    }

    @Test
    void addingToMissingTrackerReturns404() throws Exception {
        postJson("/api/trackers/999/media", "{ \"name\": \"Hades\", \"type\": \"GAME\" }")
                .andExpect(status().isNotFound());
    }

    @Test
    void addingInvalidTypeReturns400() throws Exception {
        postJson("/api/trackers/" + trackerId + "/media", "{ \"name\": \"Hades\", \"type\": \"PODCAST\" }")
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteMediaThenDeleteAgain() throws Exception {
        mvc.perform(delete("/api/media/" + mediaId)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/media/" + mediaId)).andExpect(status().isNotFound());

        // The tracker's media count dropped
        mvc.perform(get("/api/trackers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mediaCount").value(0));
    }

    // ---------- Reading media ----------

    @Test
    void listTrackersMedia() throws Exception {
        mvc.perform(get("/api/trackers/" + trackerId + "/media"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Celeste"));
    }

    @Test
    void listMediaForMissingTrackerReturns404() throws Exception {
        mvc.perform(get("/api/trackers/999/media")).andExpect(status().isNotFound());
    }

    // ---------- Filtering media ----------

    @Test
    void filterByStatusTypeAndRating() throws Exception {
        long hades = idOf(postJson("/api/trackers/" + trackerId + "/media",
                "{ \"name\": \"Hades\", \"type\": \"GAME\" }"));
        patchJson("/api/media/" + hades, "{ \"rating\": 9, \"status\": \"FINISHED\" }")
                .andExpect(status().isOk());
        postJson("/api/trackers/" + trackerId + "/media", "{ \"name\": \"Dune\", \"type\": \"BOOK\" }")
                .andExpect(status().isCreated());

        String url = "/api/trackers/" + trackerId + "/media";
        mvc.perform(get(url).param("status", "WAITLIST"))
                .andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get(url).param("type", "GAME"))
                .andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get(url).param("type", "GAME").param("status", "WAITLIST"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Celeste"));
        mvc.perform(get(url).param("minRating", "8"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Hades"));
        mvc.perform(get(url).param("maxRating", "8"))
                .andExpect(jsonPath("$.length()").value(0));   // unrated media are excluded
    }

    @Test
    void invalidFiltersReturn400() throws Exception {
        String url = "/api/trackers/" + trackerId + "/media";
        mvc.perform(get(url).param("status", "BORED")).andExpect(status().isBadRequest());
        mvc.perform(get(url).param("type", "PODCAST")).andExpect(status().isBadRequest());
        mvc.perform(get(url).param("minRating", "11")).andExpect(status().isBadRequest());
        mvc.perform(get(url).param("minRating", "8").param("maxRating", "3"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMediaIncludesLogs() throws Exception {
        logViewing("{ \"viewedOn\": \"2026-11-10\", \"viewProgress\": 5 }")
                .andExpect(status().isCreated());

        mvc.perform(get("/api/media/" + mediaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Celeste"))
                .andExpect(jsonPath("$.logs.length()").value(1))
                .andExpect(jsonPath("$.logs[0].viewProgress").value(5));
    }

    @Test
    void getMissingMediaReturns404() throws Exception {
        mvc.perform(get("/api/media/999")).andExpect(status().isNotFound());
    }

    // ---------- Editing media ----------

    @Test
    void editRatingAndStatus() throws Exception {
        patchJson("/api/media/" + mediaId, "{ \"rating\": 9, \"status\": \"HOLD\" }")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(9))
                .andExpect(jsonPath("$.status").value("HOLD"))
                .andExpect(jsonPath("$.name").value("Celeste"));   // unchanged
    }

    @Test
    void ratingOutOfRangeReturns400() throws Exception {
        patchJson("/api/media/" + mediaId, "{ \"rating\": 15 }").andExpect(status().isBadRequest());
    }

    @Test
    void blankNameReturns400() throws Exception {
        patchJson("/api/media/" + mediaId, "{ \"name\": \"   \" }").andExpect(status().isBadRequest());
    }

    // ---------- Viewing logs ----------

    @Test
    void logViewingSetsStatusToViewing() throws Exception {
        logViewing("{ \"viewedOn\": \"2026-11-10\", \"viewProgress\": 5 }")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("VIEWING"))
                .andExpect(jsonPath("$.totalProgress").value(5));
    }

    @Test
    void logViewingInFutureReturns400() throws Exception {
        logViewing("{ \"viewedOn\": \"2099-01-01\", \"viewProgress\": 5 }")
                .andExpect(status().isBadRequest());
    }

    @Test
    void logWithMissingProgressReturns400() throws Exception {
        logViewing("{ \"viewedOn\": \"2026-11-10\" }").andExpect(status().isBadRequest());
    }

    @Test
    void removeLatestLogThenRemoveWhenNoneLeft() throws Exception {
        logViewing("{ \"viewedOn\": \"2026-11-10\", \"viewProgress\": 5 }")
                .andExpect(status().isCreated());

        mvc.perform(delete("/api/media/" + mediaId + "/logs/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProgress").value(0));
        mvc.perform(delete("/api/media/" + mediaId + "/logs/latest"))
                .andExpect(status().isNotFound());
    }

    // ---------- Helpers ----------

    private ResultActions postJson(String url, String json) throws Exception {
        return mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(json));
    }

    private ResultActions patchJson(String url, String json) throws Exception {
        return mvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON).content(json));
    }

    private ResultActions logViewing(String json) throws Exception {
        return postJson("/api/media/" + mediaId + "/logs", json);
    }

    private static long idOf(ResultActions result) throws Exception {
        String body = result.andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(body, "$.id")).longValue();
    }
}
