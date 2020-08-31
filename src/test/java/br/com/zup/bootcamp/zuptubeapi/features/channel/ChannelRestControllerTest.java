package br.com.zup.bootcamp.zuptubeapi.features.channel;

import br.com.zup.bootcamp.zuptubeapi.mock.ChannelMock;
import br.com.zup.bootcamp.zuptubeapi.model.entity.Channel;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateChannelRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChannelRestController.class)
public class ChannelRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChannelService channelService;

    @Test
    void findById() throws Exception {
        Channel channel = ChannelMock.buildChannel();
        when(channelService.findById(any())).thenReturn(Optional.of(channel));

        mockMvc.perform(get(String.format("/api/v1/channels/%s", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(channel)));
    }

    @Test
    void findByAccountId() throws Exception {
        Channel channel = ChannelMock.buildChannel();
        when(channelService.findByAccountId(any())).thenReturn(Optional.of(channel));

        mockMvc.perform(get(String.format("/api/v1/channels/%s:find-channel", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(channel)));
    }

    @Test
    void updateChannel() throws Exception {
        doNothing().when(channelService).updateById(any(), any());
        final String body = objectMapper.writeValueAsString(ChannelMock.buildUpdateChannelRequest());

        mockMvc.perform(put(String.format("/api/v1/channels/%s", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateChannelWithoutMandatoryFields() throws Exception {
        final String body = objectMapper.writeValueAsString(UpdateChannelRequest.builder().build());
        final List<String> mandatoryMessages = Stream.of(
                "message.channel.name.mandatory",
                "message.channel.description.mandatory")
                .collect(Collectors.toList());

        mockMvc.perform(put(String.format("/api/v1/channels/%s", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(objectMapper.writeValueAsString(mandatoryMessages)));
    }

    @Test
    void updateChannelOverCharacterLimits() throws Exception {
        UpdateChannelRequest request = ChannelMock.buildUpdateChannelRequest();
        request.setName(RandomString.make(256));
        request.setDescription(RandomString.make(1001));

        final String body = objectMapper.writeValueAsString(request);
        final List<String> lengthMessages = Stream.of(
                "message.channel.name.length",
                "message.channel.description.length")
                .collect(Collectors.toList());

        mockMvc.perform(put(String.format("/api/v1/channels/%s", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(objectMapper.writeValueAsString(lengthMessages)));
    }
}
