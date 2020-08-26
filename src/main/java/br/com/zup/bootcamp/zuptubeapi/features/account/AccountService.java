package br.com.zup.bootcamp.zuptubeapi.features.account;

import br.com.zup.bootcamp.zuptubeapi.exception.NotFoundException;
import br.com.zup.bootcamp.zuptubeapi.model.entity.Account;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateAccountRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateAccountRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdatePasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomPasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    @Transactional
    public UUID create(final CreateAccountRequest request) {
        final String password = request.getPassword();
        this.validatePasswordStrength(password);

        final String encodedPassword = this.passwordEncoder.encode(password);
        request.setPassword(encodedPassword);
        return this.accountRepository.create(request);
    }

    public List<Account> findAll() {
        return this.accountRepository.findAll()
                .stream()
                .peek(this::removeSensitiveData)
                .collect(Collectors.toList());
    }

    public Optional<Account> findById(final UUID accountId) {
        return this.accountRepository.findById(accountId)
                .map(this::removeSensitiveData)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("message.account.not-found"));
    }

    public Optional<Account> findByEmail(String email) {
        return this.accountRepository.findByEmail(email)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("message.account.not-found"));
    }

    private boolean exists(UUID accountId) {
        return accountRepository.exists(accountId);
    }

    @Transactional
    public void update(final UUID accountId, final UpdateAccountRequest request) {
        this.findById(accountId)
                .ifPresent(account -> this.accountRepository.update(accountId, request));
    }

    @Transactional
    public void updatePassword(final UUID accountId, final UpdatePasswordRequest request) {
        this.findById(accountId)
                .ifPresent(account -> {
                    final String password = request.getPassword();
                    this.validatePasswordStrength(password);
                    final String encodedPassword = this.passwordEncoder.encode(password);
                    this.accountRepository.updatePassword(accountId, encodedPassword);
                });
    }

    @Transactional
    public void deleteOrUndeleteById(final UUID accountId, final Boolean deleteOrUndelete) {
        if (exists(accountId)) {
            this.accountRepository.deleteOrUndelete(deleteOrUndelete, accountId);
        } else {
            throw new NotFoundException("message.account.not-found");
        }
    }

    private Account removeSensitiveData(Account account) {
        account.setPassword(null);
        return account;
    }

    private void validatePasswordStrength(final String password) {
        RuleResult validationResult = this.passwordValidator.validate(new PasswordData(password));

        if (!validationResult.isValid()) {
            this.logReasonsForPasswordWeakness(validationResult);
            throw new IllegalArgumentException("message.account.password.weak");
        }
    }

    private void logReasonsForPasswordWeakness(RuleResult validationResult) {
        validationResult.getDetails()
                .stream()
                .map(RuleResultDetail::getErrorCode)
                .forEach(log::info);
    }
}
