package br.com.zup.bootcamp.zuptubeapi.features.account

import br.com.zup.bootcamp.zuptubeapi.exception.NotFoundException
import br.com.zup.bootcamp.zuptubeapi.mock.AccountMock
import br.com.zup.bootcamp.zuptubeapi.model.entity.Account
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateAccountRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("it")
class AccountServiceIT extends Specification {

    @Autowired
    private AccountService accountService

    @Autowired
    private JdbcTemplate jdbcTemplate

    @Autowired
    private PasswordEncoder passwordEncoder

    def "Create a new account with success"() {
        given: "I have a new account information."
        CreateAccountRequest request = AccountMock.buildCreateAccountRequest()

        when: "I handle the new account to be persistent."
        def accountId = accountService.create(request)

        then: "The account is stored and its id is returned."
        accountId != null
        accountId.getClass().isAssignableFrom(UUID.class)
    }

    def "Try to create a new account with a weak password"() {
        given: "I have a new account information with a weak password."
        CreateAccountRequest request = AccountMock.buildCreateAccountRequest()
        request.setPassword("123")

        when: "I handle the new account to be persistent."
        def accountId = accountService.create(request)

        then: "An exception is thrown and the account is not persisted."
        IllegalArgumentException e = thrown()
        e.getMessage() == "message.account.password.weak"
        accountId == null
    }

    def "Try to create a new account with an existing email"() {
        given: "I have a new account information."
        def request = AccountMock.buildCreateAccountRequest()

        when: "I handle the account to be persisted twice."
        accountService.create(request)
        accountService.create(request)

        then: "An exception is thrown and the account is not persisted."
        DuplicateKeyException e = thrown()
        e.getMessage() == "message.account.email.unique"
    }

    def "Try to create a new account with an existing phone"() {
        given: "I have a new account information."
        def request = AccountMock.buildCreateAccountRequest()

        when: "I handle the account to be persisted twice."
        request.setPhone("+5519998765432")
        accountService.create(request)
        request = AccountMock.buildCreateAccountRequest()
        request.setPhone("+5519998765432")
        accountService.create(request)

        then: "An exception is thrown and the account is not persisted."
        DuplicateKeyException e = thrown()
        e.getMessage() == "message.account.phone.unique"
    }

    def "Find all account resources"() {
        given: "I save two accounts."
        accountService.create(AccountMock.buildCreateAccountRequest())
        accountService.create(AccountMock.buildCreateAccountRequest())

        when: "I try to find all saved account resources."
        def accounts = accountService.findAll()

        then: "The account resources are retrieved."
        accounts.size() >= 2
        accounts.get(0).getPassword() == null
    }

    def "Find an active account resource persisted in the database with given id with all resources filled"() {
        given: "I have an account id."
        def request = AccountMock.buildCreateAccountRequest()
        def accountId = accountService.create(request)

        when: "I try to find the account resource with its given id."
        def optionalAccount = accountService.findById(accountId)

        then: "The account resource is retrieved."
        optionalAccount.isPresent()

        and: "All necessary information is retrieved."
        Account account = optionalAccount.get()
        account.getId() == accountId
        account.getFirstName() == request.getFirstName()
        account.getLastName() == request.getLastName()
        account.getEmail() == request.getEmail()
        account.getPhone() == request.getPhone()
        account.getLocale() == request.getLocale()
        account.getTimezone() == request.getTimezone()
        account.getPassword() == null
    }

    def "Update an existing account"() {
        given: "I have an stored account."
        def accountId = accountService.create(AccountMock.buildCreateAccountRequest())

        when: "I try to update this account."
        def request = AccountMock.buildUpdateAccountRequest()
        accountService.update(accountId, request)

        then: "The update succeed and all required information are changed."
        def updatedAccount = accountService.findById(accountId).get()
        updatedAccount.getFirstName() == request.getFirstName()
        updatedAccount.getLastName() == request.getLastName()
        updatedAccount.getEmail() == request.getEmail()
        updatedAccount.getPhone() == request.getPhone()
    }

    def "Update an account using an exiting email must throw an exception"() {
        given: "I have two accounts."
        def firstAccountCreateRequest = AccountMock.buildCreateAccountRequest()
        def firstAccountId = accountService.create(firstAccountCreateRequest)

        def secondAccountCreateRequest = AccountMock.buildCreateAccountRequest()
        def secondAccountId = accountService.create(secondAccountCreateRequest)

        when: "I try to update the second account using the email of the first account."
        def secondAccountUpdateRequest = AccountMock.buildUpdateAccountRequest()
        secondAccountUpdateRequest.setEmail(firstAccountCreateRequest.getEmail())
        accountService.update(secondAccountId, secondAccountUpdateRequest)

        then: "An exception is thrown and the account is not updated."
        DuplicateKeyException e = thrown()
        e.getMessage() == "message.account.email.unique"
    }

    def "Update an account using an exiting phone number must throw an exception"() {
        given: "I have two accounts."
        def firstAccountCreateRequest = AccountMock.buildCreateAccountRequest()
        def firstAccountId = accountService.create(firstAccountCreateRequest)

        def secondAccountCreateRequest = AccountMock.buildCreateAccountRequest()
        def secondAccountId = accountService.create(secondAccountCreateRequest)

        when: "I try to update the second account using the phone number of the first account."
        def secondAccountUpdateRequest = AccountMock.buildUpdateAccountRequest()
        secondAccountUpdateRequest.setPhone(firstAccountCreateRequest.getPhone())
        accountService.update(secondAccountId, secondAccountUpdateRequest)

        then: "An exception is thrown and the account is not updated."
        DuplicateKeyException e = thrown()
        e.getMessage() == "message.account.phone.unique"
    }

    def "Update the password of an active account"() {
        given: "I have an active account."
        def request = AccountMock.buildCreateAccountRequest()
        def accountId = accountService.create(request)
        def account = accountService.findByEmail(request.getEmail()).get()
        def oldPassword = account.getPassword()

        when: "I try to update the password of this account."
        def updatePasswordRequest = AccountMock.buildUpdatePasswordRequest()
        accountService.updatePassword(accountId, updatePasswordRequest)
        account = accountService.findByEmail(request.getEmail()).get()

        then: "The new password is updated and stored."
        def newPassword = account.getPassword()
        oldPassword != newPassword
    }

    def "Delete an active account"() {
        given: "I have an active account."
        def accountId = accountService.create(AccountMock.buildCreateAccountRequest())

        when: "I try to soft-delete this account."
        accountService.deleteOrUndeleteById(accountId, true)
        accountService.findById(accountId)

        then: "The account is deleted."
        NotFoundException e = thrown()
        e.getMessage() == "message.account.not-found"
    }

    def "Delete a nonexisting account should throw a not found exception"() {
        given: "I have a nonexisting account id."
        def accountId = UUID.randomUUID()

        when: "I try to delete this nonexisting account."
        accountService.deleteOrUndeleteById(accountId, true)

        then: "The system throws an excpetion."
        NotFoundException e = thrown()
        e.getMessage() == "message.account.not-found"
    }

    def "Undelete an account"() {
        given: "I have a deleted account."
        def accountId = accountService.create(AccountMock.buildCreateAccountRequest())
        accountService.deleteOrUndeleteById(accountId, true)

        when: "I try to undelete this account."
        accountService.deleteOrUndeleteById(accountId, false)

        then: "The account is correctly restored."
        def optionalAccount = accountService.findById(accountId)
        optionalAccount.isPresent()
    }

}