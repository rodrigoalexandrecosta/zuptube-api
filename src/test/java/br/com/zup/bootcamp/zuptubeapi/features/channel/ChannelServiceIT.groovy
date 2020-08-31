package br.com.zup.bootcamp.zuptubeapi.features.channel

import br.com.zup.bootcamp.zuptubeapi.exception.NotFoundException
import br.com.zup.bootcamp.zuptubeapi.mock.AccountMock
import br.com.zup.bootcamp.zuptubeapi.mock.ChannelMock
import br.com.zup.bootcamp.zuptubeapi.model.entity.Channel
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("it")
class ChannelServiceIT extends Specification {

    @Autowired
    private ChannelService channelService


    @Autowired
    private JdbcTemplate jdbcTemplate

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate

    @Shared
    private UUID accountId

    // Adding values directly into the table so that account creation using
    // AccountService does not automatically creates a respective new channel.
    @Before
    def init() {
        def CREATE_ACCOUNT_QUERY = "INSERT INTO account " +
                "(first_name, last_name, email, phone, password, locale, timezone) " +
                "VALUES " +
                "(:firstName, :lastName, :email, :phone, :password, :locale, :timezone) " +
                "RETURNING id"

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", "Naomi")
                .addValue("lastName", "Nagata")
                .addValue("email", "nagatanaomi@email.com")
                .addValue("phone", "+5519987654321")
                .addValue("password", "123@bootcamp")
                .addValue("locale", "pt-BR")
                .addValue("timezone", "America/Sao_Paulo")

        accountId = namedParameterJdbcTemplate.queryForObject(CREATE_ACCOUNT_QUERY, params, UUID.class)
    }

    // Cleaning up the tables after each test so that channel creation using the
    // same account id and same request info does not trigger DuplicateKeyException.
    @AfterEach
    def cleanup() {
        jdbcTemplate.update("DELETE FROM channel")
        jdbcTemplate.update("DELETE FROM account")
    }

    def "Create a new channel with success"() {
        given: "I have a new channel information."
        Channel channel = new Channel(accountId, AccountMock.buildCreateAccountRequest())

        when: "I handle the new channel to be persistent."
        def channelId = channelService.create(channel)

        then: "The channel is stored and its id is returned."
        channelId != null
        channelId.getClass().isAssignableFrom(UUID.class)
    }

    def "Find an active channel resource with its associated account id and with all resouces filled"() {
        given: "I have a stored channel and its associated account id."
        def accountRequest = AccountMock.buildCreateAccountRequest()
        Channel channel = new Channel(accountId, accountRequest)
        channelService.create(channel)

        when: "I try to find the channel resouce with its given id."
        def optionalChannel = channelService.findByAccountId(accountId)

        then: "The channel resource is retrieved."
        optionalChannel.isPresent()

        and: "All necessary information is returned."
        def storedChannel = optionalChannel.get()
        storedChannel.getName() == channel.getName()
        storedChannel.getDescription() == channel.getDescription()
        storedChannel.getAccountId() == channel.getAccountId()
    }

    def "Delete an active channel"() {
        given: "I have a stored channel and its associated account id."
        def accountRequest = AccountMock.buildCreateAccountRequest()
        Channel channel = new Channel(accountId, accountRequest)
        channelService.create(channel)

        when: "I try to soft-delete this channel."
        channelService.deleteOrUndeleteByAccountId(accountId, true)
        channelService.findByAccountId(accountId)

        then: "The channel is deleted."
        NotFoundException e = thrown()
        e.getMessage() == "message.channel.not-found"
    }

    def "Delete a nonexisting account should throw a not found exception"() {
        given: "I have a nonexisting account id."
        def inexistentAccountId = UUID.randomUUID()

        when: "I try to delete this nonexisting account."
        channelService.deleteOrUndeleteByAccountId(inexistentAccountId, true)

        then: "The system throws an excpetion."
        NotFoundException e = thrown()
        e.getMessage() == "message.channel.not-found"
    }

    def "Undelete an inactive channel"() {
        given: "I have a deleted channel."
        channelService.create(new Channel(accountId, AccountMock.buildCreateAccountRequest()))
        channelService.deleteOrUndeleteByAccountId(accountId, true)

        when: "I try to undelete this channel."
        channelService.deleteOrUndeleteByAccountId(accountId, false)

        then: "The channel is correctly restored."
        def optionalChannel = channelService.findByAccountId(accountId)
        optionalChannel.isPresent()
    }

    def "Update an existing channel"() {
        given: "I have a stored channel."
        def accountRequest = AccountMock.buildCreateAccountRequest()
        Channel channel = new Channel(accountId, accountRequest)
        def channelId = channelService.create(channel)

        when: "I try to update this channel."
        def request = ChannelMock.buildUpdateChannelRequest()
        channelService.updateById(channelId, request)

        then: "The update succeed and all required informations are changed."
        def updatedChannel = channelService.findByAccountId(accountId).get()
        updatedChannel.getName() == request.getName()
        updatedChannel.getDescription() == request.getDescription()
        updatedChannel.getAccountId() == accountId
    }
}
