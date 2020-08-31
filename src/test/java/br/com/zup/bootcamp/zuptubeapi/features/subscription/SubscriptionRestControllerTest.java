package br.com.zup.bootcamp.zuptubeapi.features.subscription;

import br.com.zup.bootcamp.zuptubeapi.mock.SubscriptionMock;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponseByAccount;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponseByChannel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionRestController.class)
public class SubscriptionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubscriptionService subscriptionService;

    @Test
    void create() throws Exception {
        final ResultActions resultActions = mockMvc.perform(post(
                String.format("/api/v1/channels/subscriptions/%s/%s", UUID.randomUUID(), UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated()).andExpect(header().exists("Location"));
        verify(subscriptionService, times(1)).create(any(), any());
    }

    @Test
    void find() throws Exception {
        final SubscriptionResponse response = SubscriptionMock.buildSubscriptionResponse();

        when(subscriptionService.find(any(), any())).thenReturn(Optional.of(response));

        mockMvc.perform(get(
                String.format("/api/v1/channels/subscriptions/%s/%s", UUID.randomUUID(), UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void findAllByChannel() throws Exception {
        final SubscriptionResponseByChannel response1 = SubscriptionMock.buildSubscriptionResponseByChannel();
        final SubscriptionResponseByChannel response2 = SubscriptionMock.buildSubscriptionResponseByChannel();
        final List<SubscriptionResponseByChannel> responses = List.of(response1, response2);
        when(subscriptionService.findAllByChannelId(any())).thenReturn(responses);

        mockMvc.perform(get(
                String.format("/api/v1/channels/subscriptions/%s:find-by-channel", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @Test
    void findAllByAccount() throws Exception {
        final SubscriptionResponseByAccount response1 = SubscriptionMock.buildSubscriptionResponseByAccount();
        final SubscriptionResponseByAccount response2 = SubscriptionMock.buildSubscriptionResponseByAccount();
        final List<SubscriptionResponseByAccount> responses = List.of(response1, response2);
        when(subscriptionService.findAllByAccountId(any())).thenReturn(responses);

        mockMvc.perform(get(
                String.format("/api/v1/channels/subscriptions/%s:find-by-account", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post(format("/api/v1/channels/subscriptions/%s/%s:delete", UUID.randomUUID(), UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void undelete() throws Exception {
        mockMvc.perform(post(format("/api/v1/channels/subscriptions/%s/%s:undelete", UUID.randomUUID(), UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
