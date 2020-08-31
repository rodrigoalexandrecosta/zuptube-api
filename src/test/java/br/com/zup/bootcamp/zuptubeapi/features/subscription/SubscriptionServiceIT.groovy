package br.com.zup.bootcamp.zuptubeapi.features.subscription

import br.com.zup.bootcamp.zuptubeapi.exception.NotFoundException
import br.com.zup.bootcamp.zuptubeapi.features.account.AccountService
import br.com.zup.bootcamp.zuptubeapi.features.channel.ChannelService
import br.com.zup.bootcamp.zuptubeapi.mock.AccountMock
import br.com.zup.bootcamp.zuptubeapi.model.to.response.AccountResponse
import br.com.zup.bootcamp.zuptubeapi.model.to.response.ChannelResponse
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("it")
class SubscriptionServiceIT extends Specification {

    @Autowired
    private SubscriptionService subscriptionService

    @Autowired
    private AccountService accountService

    @Autowired
    private ChannelService channelService

    @Autowired
    private JdbcTemplate jdbcTemplate

    @Shared
    private UUID accountId

    @Shared
    private UUID channelId

    @Before
    def init() {
        accountId = accountService.create(AccountMock.buildCreateAccountRequest())
        channelId = channelService.findByAccountId(accountId).get().getId()
    }

    def "Create a new subscription with success"() {
        when: "I handle the new subscription information to be persisted."
        def subscriptionId = subscriptionService.create(accountId, channelId)

        then: "The subscription is stored and its id is returned."
        subscriptionId != null
        subscriptionId.getClass().isAssignableFrom(UUID.class)
    }

    def "Find a subscription using an account id and a channel id with all resources filled"() {
        given: "I have a stored subscription."
        def subscriptionId = subscriptionService.create(accountId, channelId)

        when: "I try to find the subscription resource."
        def optionalSubscription = subscriptionService.find(accountId, channelId)

        then: "The subscription is correctly retrieved."
        optionalSubscription.isPresent()

        and: "All necessary information are filled."
        def subscription = optionalSubscription.get()
        subscription.getId() == subscriptionId
        subscription.getAccountResponse() == new AccountResponse(accountService.findById(accountId).get())
        subscription.getChannelResponse() == new ChannelResponse(channelService.findById(channelId).get())
    }

    def "Find all subscription information relative to a channel using a channel id"() {
        given: "I have two account subscriptions into a channel."
        subscriptionService.create(accountId, channelId)
        def anotherAccountId = accountService.create(AccountMock.buildCreateAccountRequest())
        subscriptionService.create(anotherAccountId, channelId)

        when: "I try to retrive the subscription informations of this channel."
        def subscriptions = subscriptionService.findAllByChannelId(channelId)

        then: "The subscription informations are correctly returned."
        subscriptions.size() == 2
    }

    def "Find all subscription information relative to an account using an account id"() {
        given: "I have two account subscriptions from the same account into two distinct channels."
        subscriptionService.create(accountId, channelId)
        def anotherAccountId = accountService.create(AccountMock.buildCreateAccountRequest())
        subscriptionService.create(anotherAccountId, channelId)
        def anotherChannelId = channelService.findByAccountId(anotherAccountId).get().getId()
        subscriptionService.create(accountId, anotherChannelId)

        when: "I try to retrive the subscription informations of this channel."
        def subscriptions = subscriptionService.findAllByAccountId(accountId)

        then: "The subscription informations are correctly returned."
        subscriptions.size() == 2
    }

    def "Remove a subscription"() {
        given: "I have an active subscription."
        subscriptionService.create(accountId, channelId)

        when: "I try to remove this subscription."
        subscriptionService.deleteOrUndelete(accountId, channelId, true)
        def optionalSubscription = subscriptionService.find(accountId, channelId)

        then: "The subscription is correctly soft-deleted."
        NotFoundException e = thrown()
        e.getMessage() == "message.channel.subscription.not-found"
    }

    def "Undelete a deleted subscription"() {
        given: "I have a deleted subscription."
        subscriptionService.create(accountId, channelId)
        subscriptionService.deleteOrUndelete(accountId, channelId, true)

        when: "I try to undelete this subscription."
        subscriptionService.deleteOrUndelete(accountId, channelId, false)

        then: "The subscription is correctly undeleted."
        def optionalSubscription = subscriptionService.find(accountId, channelId)
        optionalSubscription.isPresent()
    }
}
