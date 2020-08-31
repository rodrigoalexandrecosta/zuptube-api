package br.com.zup.bootcamp.zuptubeapi.features.videoengagement

import br.com.zup.bootcamp.zuptubeapi.features.account.AccountService
import br.com.zup.bootcamp.zuptubeapi.features.channel.ChannelService
import br.com.zup.bootcamp.zuptubeapi.features.video.VideoService
import br.com.zup.bootcamp.zuptubeapi.mock.AccountMock
import br.com.zup.bootcamp.zuptubeapi.mock.VideoEngagementMock
import br.com.zup.bootcamp.zuptubeapi.mock.VideoMock
import br.com.zup.bootcamp.zuptubeapi.model.entity.VideoEngagement
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("it")
class VideoEngagementServiceIT extends Specification {

    @Autowired
    private VideoEngagementService videoEngagementService

    @Autowired
    private AccountService accountService

    @Autowired
    private ChannelService channelService

    @Autowired
    private VideoService videoService

    @Autowired
    private JdbcTemplate jdbcTemplate


    @Shared
    private UUID accountId


    @Shared
    private UUID videoId

    @Before
    def init() {
        accountId = accountService.create(AccountMock.buildCreateAccountRequest())
        def channelId = channelService.findByAccountId(accountId).get().getId()

        def multipartVideo = VideoMock.buildMultipartFileVideo()
        def videoRequest = VideoMock.buildCreateVideoRequest()
        videoId = videoService.create(channelId, multipartVideo, videoRequest)
    }


    def "Create a new video engagement with success"() {
        given: "I have a new video engagement information alongside with relatives account and video ids."
        def request = VideoEngagementMock.buildCreateVideoEngagementRequest()

        when: "I handle the new video engagement to be persistent."
        def videoEngagementId = videoEngagementService.create(accountId, videoId, request)

        then: "The video engagement is stored and its id is returned."
        videoEngagementId != null
        videoEngagementId.getClass().isAssignableFrom(UUID.class)
    }

    def "Find a stored video engagement information using its id"() {
        given: "I have a stored video engagement."
        def request = VideoEngagementMock.buildCreateVideoEngagementRequest()
        def videoEngagementId = videoEngagementService.create(accountId, videoId, request)

        when: "I try to find this video engagement using its given id."
        def optionalVideoEngagement = videoEngagementService.findById(videoEngagementId)

        then: "The video engagement is returned."
        optionalVideoEngagement.isPresent()

        and: "All necessary information is retrieved."
        VideoEngagement videoEngagement = optionalVideoEngagement.get()
        videoEngagement.getComment() == request.getComment()
        videoEngagement.isLiked() == request.getLiked()
        videoEngagement.getAccountId() == accountId
        videoEngagement.getVideoId() == videoId
        videoEngagement.getId() == videoEngagementId
    }

    def "Find a stored video engagement information using the related account id and video id"() {
        given: "I have a stored video engagement."
        def request = VideoEngagementMock.buildCreateVideoEngagementRequest()
        def videoEngagementId = videoEngagementService.create(accountId, videoId, request)

        when: "I try to find this video engagement using its account id and video id."
        def optionalVideoEngagement = videoEngagementService.findByAccountIdAndVideoId(accountId, videoId)

        then: "The video engagement is returned."
        optionalVideoEngagement.isPresent()

        and: "All necessary information is retrieved."
        VideoEngagement videoEngagement = optionalVideoEngagement.get()
        videoEngagement.getComment() == request.getComment()
        videoEngagement.isLiked() == request.getLiked()
        videoEngagement.getAccountId() == accountId
        videoEngagement.getVideoId() == videoId
        videoEngagement.getId() == videoEngagementId
    }

    def "Find all video engagement information using a video id"() {
        given: "I have two video engagements at the same video."
        videoEngagementService.create(accountId, videoId, VideoEngagementMock.buildCreateVideoEngagementRequest())
        def anotherAccountId = accountService.create(AccountMock.buildCreateAccountRequest())
        videoEngagementService.create(anotherAccountId, videoId, VideoEngagementMock.buildCreateVideoEngagementRequest())

        when: "I try to find all video engagements using the video id."
        def videoEngagements = videoEngagementService.findAllByVideoId(videoId)

        then: "The video engagements informations are returned."
        videoEngagements.size() == 2
    }

    def "Find all video engagement information of an account using an account id"() {
        given: "I have two video engagements from the same user account."
        videoEngagementService.create(accountId, videoId, VideoEngagementMock.buildCreateVideoEngagementRequest())

        def channelId = channelService.findByAccountId(accountId).get().getId()
        def multipartVideo = VideoMock.buildMultipartFileVideo()
        def videoRequest = VideoMock.buildCreateVideoRequest()
        def anotherVideoId = videoService.create(channelId, multipartVideo, videoRequest)

        videoEngagementService.create(accountId, anotherVideoId, VideoEngagementMock.buildCreateVideoEngagementRequest())

        when: "I try to find all video engagements using the video id."
        def videoEngagements = videoEngagementService.findAllByAccountId(accountId)

        then: "The video engagements informations are returned."
        videoEngagements.size() == 2
    }

    def "Update an existing video engagement using its id"() {
        given: "I have a stored video engagement information."
        def videoEngagementId = videoEngagementService.create(accountId, videoId,
                VideoEngagementMock.buildCreateVideoEngagementRequest())

        when: "I try to update this video engagement information using its id."
        def request = VideoEngagementMock.buildUpdateVideoEngagementRequest()
        videoEngagementService.updateById(videoEngagementId, request)

        then: "The update succeed and all required information are changed."
        def updatedVideoEngagement = videoEngagementService.findById(videoEngagementId).get()
        updatedVideoEngagement.getComment() == request.getComment()
        updatedVideoEngagement.isLiked() == request.getLiked()
        updatedVideoEngagement.getAccountId() == accountId
        updatedVideoEngagement.getVideoId() == videoId
    }

    def "Update an existing video engagement using the related account id and video id"() {
        given: "I have a stored video engagement information."
        def videoEngagementId = videoEngagementService.create(accountId, videoId,
                VideoEngagementMock.buildCreateVideoEngagementRequest())

        when: "I try to update this video engagement information using the account id and video id."
        def request = VideoEngagementMock.buildUpdateVideoEngagementRequest()
        videoEngagementService.updateByAccountIdAndVideoId(accountId, videoId, request)

        then: "The update succeed and all required information are changed."
        def updatedVideoEngagement = videoEngagementService.findById(videoEngagementId).get()
        updatedVideoEngagement.getComment() == request.getComment()
        updatedVideoEngagement.isLiked() == request.getLiked()
        updatedVideoEngagement.getAccountId() == accountId
        updatedVideoEngagement.getVideoId() == videoId
        updatedVideoEngagement.getId() == videoEngagementId
    }
}
