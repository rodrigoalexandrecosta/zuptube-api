package br.com.zup.bootcamp.zuptubeapi.features.video;

import br.com.zup.bootcamp.zuptubeapi.mock.VideoMock;
import br.com.zup.bootcamp.zuptubeapi.model.entity.Video;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateVideoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VideoRestController.class)
public class VideoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VideoService videoService;

    @Test
    void create() throws Exception {
        final String body = objectMapper.writeValueAsString(VideoMock.buildCreateVideoRequest());

        final ResultActions resultActions = mockMvc.perform(multipart(String.format("/api/v1/channels/%s/videos", UUID.randomUUID()))
                .file(new MockMultipartFile("video", "video.mp4",
                        MediaType.MULTIPART_FORM_DATA_VALUE, "test".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        resultActions.andExpect(status().isCreated()).andExpect(header().exists("Location"));
        verify(videoService, times(1)).create(any(), any(), any());
    }

    @Test
    void createWithoutMandatoryFields() throws Exception {
        CreateVideoRequest request = CreateVideoRequest.builder().build();
        final String body = objectMapper.writeValueAsString(request);

        final List<String> mandatoryMessages = Stream.of(
                "message.video.title.mandatory",
                "message.video.description.mandatory")
                .collect(Collectors.toList());

        mockMvc.perform(multipart(String.format("/api/v1/channels/%s/videos", UUID.randomUUID()))
                .file(new MockMultipartFile("video", "video.mp4",
                        MediaType.MULTIPART_FORM_DATA_VALUE, "test".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(objectMapper.writeValueAsString(mandatoryMessages)));
    }

    @Test
    void createOverCharacterLimits() throws Exception {
        CreateVideoRequest request = VideoMock.buildCreateVideoRequest();
        request.setTitle(RandomString.make(256));
        request.setDescription(RandomString.make(2001));
        request.setFilePath(RandomString.make(2001));
        final String body = objectMapper.writeValueAsString(request);

        final List<String> lengthMessages = Stream.of(
                "message.video.title.length",
                "message.video.description.length")
                .collect(Collectors.toList());

        mockMvc.perform(multipart(String.format("/api/v1/channels/%s/videos", UUID.randomUUID()))
                .file(new MockMultipartFile("video", "video.mp4",
                        MediaType.MULTIPART_FORM_DATA_VALUE, "test".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(objectMapper.writeValueAsString(lengthMessages)));
    }

    @Test
    void findById() throws Exception {
        Video video = VideoMock.buildVideo();
        final UUID videoId = UUID.randomUUID();
        video.setId(videoId);
        when(videoService.findById(any())).thenReturn(Optional.of(video));

        mockMvc.perform(get(String.format("/api/v1/channels/videos/%s", videoId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(video)));
    }

    @Test
    void findAllByChannelId() throws Exception {
        Video video1 = VideoMock.buildVideo();
        Video video2 = VideoMock.buildVideo();
        final List<Video> videos = List.of(video1, video2);

        when(videoService.findAllByChannelId(any())).thenReturn(videos);

        mockMvc.perform(get(String.format("/api/v1/channels/%s/videos", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(videos)));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post(format("/api/v1/channels/videos/%s:delete", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNonexistingVideo() throws Exception {
        mockMvc.perform(post(format("/api/v1/channels/videos/%s:delete", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteVideoWithMalformedId() throws Exception {
        final String id = "12345";
        mockMvc.perform(post(format("/api/v1/channels/videos/%s:delete", id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void undelete() throws Exception {
        mockMvc.perform(post(format("/api/v1/channels/videos/%s:undelete", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
