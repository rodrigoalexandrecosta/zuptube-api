package br.com.zup.bootcamp.zuptubeapi.features.videoengagement;

import br.com.zup.bootcamp.zuptubeapi.mock.VideoEngagementMock;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateVideoEngagementRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VideoEngagementRestController.class)
public class VideoEngagementRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VideoEngagementService videoEngagementService;

    @Test
    void create() throws Exception {
        final String body = objectMapper.writeValueAsString(VideoEngagementMock.buildCreateVideoEngagementRequest());

        final ResultActions resultActions = mockMvc.perform(post(
                String.format("/api/v1/channels/videos/%s/video-engagements/%s", UUID.randomUUID(), UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        resultActions.andExpect(status().isCreated()).andExpect(header().exists("Location"));
        verify(videoEngagementService, times(1)).create(any(), any(), any());
    }

    @Test
    void createOverCharacterLimits() throws Exception {
        CreateVideoEngagementRequest request = VideoEngagementMock.buildCreateVideoEngagementRequest();
        request.setComment(RandomString.make(2001));
        final String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(
                String.format("/api/v1/channels/videos/%s/video-engagements/%s", UUID.randomUUID(), UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("[\"message.video.engagement.comment.length\"]"));
    }

}
