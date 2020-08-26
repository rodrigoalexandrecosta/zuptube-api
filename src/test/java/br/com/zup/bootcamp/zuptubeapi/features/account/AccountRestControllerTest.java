package br.com.zup.bootcamp.zuptubeapi.features.account;

import br.com.zup.bootcamp.zuptubeapi.exception.NotFoundException;
import br.com.zup.bootcamp.zuptubeapi.mock.AccountMock;
import br.com.zup.bootcamp.zuptubeapi.model.entity.Account;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateAccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
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

@WebMvcTest(AccountRestController.class)
public class AccountRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    public void initTest() {
        when(accountService.create(any())).thenReturn(UUID.randomUUID());
    }

    @Test
    void create() throws Exception {
        final String body = objectMapper.writeValueAsString(AccountMock.buildCreateAccountRequest());

        final ResultActions resultActions = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        resultActions.andExpect(status().isCreated()).andExpect(header().exists("Location"));
        verify(accountService, times(1)).create(any());
    }

    @Test
    void createWithoutOrMalformedBody() throws Exception {
        final String body = "";
        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createWithoutMandatoryFields() throws Exception {
        final String body = objectMapper.writeValueAsString(CreateAccountRequest.builder().build());

        final List<String> mandatoryMessages = Stream.of
                (
                        "message.account.first-name.mandatory",
                        "message.account.last-name.mandatory",
                        "message.account.email.mandatory",
                        "message.account.phone.mandatory",
                        "message.account.password.mandatory",
                        "message.account.locale.mandatory",
                        "message.account.timezone.mandatory"
                )
                .collect(Collectors.toList());

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(objectMapper.writeValueAsString(mandatoryMessages)));
    }

    @Test
    void createOverCharacterLimits() throws Exception {
        CreateAccountRequest request = AccountMock.buildCreateAccountRequest();
        request.setFirstName(RandomString.make(256));
        request.setLastName(RandomString.make(256));
        request.setEmail(RandomString.make(256) + "@email.com");
        request.setPassword(RandomString.make(156));

        final String body = objectMapper.writeValueAsString(request);
        final List<String> lengthMessages = Stream.of
                (
                        "message.account.first-name.length",
                        "message.account.last-name.length",
                        "message.account.email.invalid-format",
                        "message.account.email.length",
                        "message.account.password.length"
                )
                .collect(Collectors.toList());

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(objectMapper.writeValueAsString(lengthMessages)));
    }

    @Test
    void createUnderCharacterLimits() throws Exception {
        CreateAccountRequest request = AccountMock.buildCreateAccountRequest();
        request.setPassword(RandomString.make(5));

        final String body = objectMapper.writeValueAsString(request);
        final List<String> lengthMessages = Stream.of("message.account.password.length").collect(Collectors.toList());

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(objectMapper.writeValueAsString(lengthMessages)));
    }

    @Test
    void createWithMalformedEmail() throws Exception {
        CreateAccountRequest request = AccountMock.buildCreateAccountRequest();
        request.setEmail("email");
        final String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("[\"message.account.email.invalid-format\"]"));
    }

    @Test
    void createWithSameEmail() throws Exception {
        final String body = objectMapper.writeValueAsString(AccountMock.buildCreateAccountRequest());
        when(accountService.create(any())).thenThrow(new DuplicateKeyException("message.account.email.unique"));

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict())
                .andExpect(content().string("message.account.email.unique"));
    }

    @Test
    void createWithSamePhone() throws Exception {

        final String body = objectMapper.writeValueAsString(AccountMock.buildCreateAccountRequest());

        when(accountService.create(any())).thenThrow(new DuplicateKeyException("message.account.phone.unique"));

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict())
                .andExpect(content().string("message.account.phone.unique"));
    }

    @Test
    void createWithMalformedAlfanumericPhone() throws Exception {
        CreateAccountRequest request = AccountMock.buildCreateAccountRequest();
        request.setPhone("+551999A87625");
        final String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("[\"message.account.phone.invalid-format\"]"));

    }

    @Test
    void createWithPhoneUnderCharacterLimit() throws Exception {
        CreateAccountRequest request = AccountMock.buildCreateAccountRequest();
        request.setPhone("+551999123456");
        final String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("[\"message.account.phone.invalid-format\"]"));
    }

    @Test
    void createWithPhoneOverCharacterLimit() throws Exception {
        CreateAccountRequest request = AccountMock.buildCreateAccountRequest();
        request.setPhone("+55199912345611");
        final String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("[\"message.account.phone.invalid-format\"]"));
    }

    @Test
    void createWithPhoneMalformedCountryCode() throws Exception {
        CreateAccountRequest request = AccountMock.buildCreateAccountRequest();
        request.setPhone("+23199912345611");
        final String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("[\"message.account.phone.invalid-format\"]"));
    }

    @Test
    void createWithMalformedLocale() throws Exception {
        CreateAccountRequest request = AccountMock.buildCreateAccountRequest();
        request.setLocale("pt_BR");
        final String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("[\"message.account.locale.invalid-format\"]"));
    }

    @Test
    void createWithMalformedTimezone() throws Exception {
        CreateAccountRequest request = AccountMock.buildCreateAccountRequest();
        request.setTimezone("America_Sao_Paulo-03");
        final String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("[\"message.account.timezone.invalid-format\"]"));
    }

    @Test
    void findById() throws Exception {
        Account account = AccountMock.buildAccount();
        final UUID accountId = UUID.randomUUID();
        account.setId(accountId);
        when(accountService.findById(any())).thenReturn(Optional.of(account));

        mockMvc.perform(get("/api/v1/accounts/" + accountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(account)));
    }

    @Test
    void findByIdWithMalFormedId() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/" + "1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void findAll() throws Exception {
        Account account1 = AccountMock.buildAccount();
        account1.setId(UUID.randomUUID());
        Account account2 = AccountMock.buildAccount();
        account2.setId(UUID.randomUUID());

        final List<Account> accounts = List.of(account1, account2);

        when(accountService.findAll()).thenReturn(accounts);

        mockMvc.perform(get("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accounts)));
    }

    @Test
    void findWithoutAccount() throws Exception {
        when(accountService.findAll()).thenReturn(Lists.emptyList());

        mockMvc.perform(get("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Lists.emptyList())));
    }

    @Test
    void update() throws Exception {
        doNothing().when(accountService).updatePassword(any(), any());
        final String body = objectMapper.writeValueAsString(AccountMock.buildUpdateAccountRequest());

        mockMvc.perform(put("/api/v1/accounts/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateInexistentAccount() throws Exception {
        when(accountService.findById(any())).thenThrow(NotFoundException.class);
        doCallRealMethod().when(accountService).update(any(), any());

        final String body = objectMapper.writeValueAsString(AccountMock.buildUpdateAccountRequest());

        mockMvc.perform(put("/api/v1/accounts/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWithSameEmail() throws Exception {
        final String body = objectMapper.writeValueAsString(AccountMock.buildUpdateAccountRequest());
        doThrow(new DuplicateKeyException("message.account.email.unique")).when(accountService).update(any(), any());

        mockMvc.perform(put("/api/v1/accounts/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict())
                .andExpect(content().string("message.account.email.unique"));

    }

    @Test
    void updateAccountSamePhone() throws Exception {
        final String body = objectMapper.writeValueAsString(AccountMock.buildUpdateAccountRequest());
        doThrow(new DuplicateKeyException("message.account.phone.unique")).when(accountService).update(any(), any());

        mockMvc.perform(put("/api/v1/accounts/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict())
                .andExpect(content().string("message.account.phone.unique"));
    }

    @Test
    void updatePassword() throws Exception {
        doNothing().when(accountService).updatePassword(any(), any());

        final String password = "my_new_password123";
        final String body = objectMapper.writeValueAsString(password);

        mockMvc.perform(put(format("/api/v1/accounts/%s:update-password", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAccount() throws Exception {
        mockMvc.perform(post(format("/api/v1/accounts/%s:delete", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNonexistentAccount() throws Exception {
        mockMvc.perform(post(format("/api/v1/accounts/%s:delete", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAccountWithMalformedId() throws Exception {
        final String id = "12345";
        mockMvc.perform(post(format("/api/v1/accounts/%s:delete", id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void undeleteAccount() throws Exception {
        mockMvc.perform(post(format("/api/v1/accounts/%s:undelete", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}