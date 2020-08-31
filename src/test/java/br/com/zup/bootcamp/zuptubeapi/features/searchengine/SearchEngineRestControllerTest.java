package br.com.zup.bootcamp.zuptubeapi.features.searchengine;

import br.com.zup.bootcamp.zuptubeapi.features.video.VideoService;
import br.com.zup.bootcamp.zuptubeapi.mock.VideoMock;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.VideoResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.VideoSearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchEngineRestController.class)
public class SearchEngineRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VideoService videoService;

    @Test
    void searchVideo() throws Exception {
        final VideoSearchResponse response1 = VideoMock.buildVideoSearchResponse();
        final VideoSearchResponse response2 = VideoMock.buildVideoSearchResponse();
        final List<VideoSearchResponse> responses = List.of(response1, response2);
        when(videoService.findBySearch(any())).thenReturn(responses);

        mockMvc.perform(get("/api/v1/searches/")
                .param("my search", "search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @Test
    void searchVideoInChannel() throws Exception {
        final VideoResponse response1 = VideoMock.buildVideoResponse();
        final VideoResponse response2 = VideoMock.buildVideoResponse();
        final List<VideoResponse> responses = List.of(response1, response2);
        when(videoService.findBySearchInChannel(any(), any())).thenReturn(responses);

        mockMvc.perform(get(String.format("/api/v1/searches/channels/%s", UUID.randomUUID()))
                .param("my search", "search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }
}
