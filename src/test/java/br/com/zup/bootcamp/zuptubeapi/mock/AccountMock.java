package br.com.zup.bootcamp.zuptubeapi.mock;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Account;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateAccountRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateAccountRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdatePasswordRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.AccountResponse;
import net.bytebuddy.utility.RandomString;

import java.util.Random;
import java.util.UUID;

public final class AccountMock {

    private AccountMock() {
        throw new IllegalStateException("Class not meant for instantiation.");
    }

    public static CreateAccountRequest buildCreateAccountRequest() {
        Random random = new Random();
        return CreateAccountRequest.builder()
                .firstName("Logan")
                .lastName("Roy")
                .email(RandomString.make() + "@email.com")
                .phone("+55199" + String.format("%08d", random.nextInt(100000000)))
                .password("bootcamp@123")
                .locale("pt-BR")
                .timezone("America/Sao_Paulo")
                .build();
    }

    public static UpdateAccountRequest buildUpdateAccountRequest() {
        Random random = new Random();
        return UpdateAccountRequest.builder()
                .firstName("Kendall")
                .lastName("Roy")
                .email(RandomString.make() + "@email.com")
                .phone("+55199" + String.format("%08d", random.nextInt(100000000)))
                .locale("pt-BR")
                .timezone("America/Fortaleza")
                .build();
    }

    public static UpdatePasswordRequest buildUpdatePasswordRequest() {
        return UpdatePasswordRequest.builder()
                .password("123@bootcamp")
                .build();
    }

    public static Account buildAccount() {
        return Account.builder()
                .build();
    }

    public static AccountResponse buildAccountResponse() {
        return AccountResponse.builder()
                .id(UUID.randomUUID())
                .firstName("Siobhan")
                .lastName("Roy")
                .removed(false)
                .build();
    }
}
