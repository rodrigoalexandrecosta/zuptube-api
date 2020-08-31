package br.com.zup.bootcamp.zuptubeapi.features.video

import br.com.zup.bootcamp.zuptubeapi.exception.NotFoundException
import br.com.zup.bootcamp.zuptubeapi.features.account.AccountService
import br.com.zup.bootcamp.zuptubeapi.features.channel.ChannelService
import br.com.zup.bootcamp.zuptubeapi.mock.AccountMock
import br.com.zup.bootcamp.zuptubeapi.mock.VideoMock
import br.com.zup.bootcamp.zuptubeapi.model.entity.Video
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("it")
class VideoServiceIT extends Specification {

    @Autowired
    private VideoService videoService

    @Autowired
    private AccountService accountService

    @Autowired
    private ChannelService channelService

    @Autowired
    private JdbcTemplate jdbcTemplate

    @Shared
    private UUID channelId

    @Before
    def init() {
        def accountId = accountService.create(AccountMock.buildCreateAccountRequest())
        channelId = channelService.findByAccountId(accountId).get().getId()
    }

    // Be aware: this test creates new dirs and files in the project root dir every
    // time it runs and/or for every project build, always using the following pattern:
    // [ ${project.basedir}/.sample/video-storage/{channelId}/{videoName} ]
    def "Create a new video with success"() {
        given: "I have a new video information and a video file."
        def multipartVideo = VideoMock.buildMultipartFileVideo()
        def request = VideoMock.buildCreateVideoRequest()

        when: "I handle the file and the request to be persistent."
        def videoId = videoService.create(channelId, multipartVideo, request)

        then: "The video file is created and stored along with the video information, and its id is returned."
        videoId != null
        videoId.getClass().isAssignableFrom(UUID.class)
    }

    def "Find a video resource with its given id and with all resources filled"() {
        given: "I have a video id."
        def multipartVideo = VideoMock.buildMultipartFileVideo()
        def request = VideoMock.buildCreateVideoRequest()
        def videoId = videoService.create(channelId, multipartVideo, request)

        when: "I try to find the video resource with its given id."
        def optionalVideo = videoService.findById(videoId)

        then: "The video resource is retrieved."
        optionalVideo.isPresent()

        and: "All necessary information is retrieved."
        Video video = optionalVideo.get()
        video.getId() == videoId
        video.getTitle() == request.getTitle()
        video.getDescription() == request.getDescription()
        video.getChannelId() == channelId
        video.getFilePath() == request.getFilePath()
        video.getVideoFile().exists() && video.getVideoFile().isFile()
    }

    def "Find all videos from a channel using a channel id"() {
        given: "I have a channel id and its two respective videos."
        def multipartVideo = VideoMock.buildMultipartFileVideo()
        def request = VideoMock.buildCreateVideoRequest()
        videoService.create(channelId, multipartVideo, request)
        videoService.create(channelId, multipartVideo, request)

        when: "I try to find all the videos in this channel using the channel id."
        def videos = videoService.findAllByChannelId(channelId)

        then: "The video resources from this channel are returned."
        videos.size() == 2
    }

    def "Delete an active video"() {
        given: "I have an active video."
        def multipartVideo = VideoMock.buildMultipartFileVideo()
        def request = VideoMock.buildCreateVideoRequest()
        def videoId = videoService.create(channelId, multipartVideo, request)

        when: "I try to soft-delete this video."
        videoService.deleteOrUndeleteById(videoId, true)
        videoService.findById(videoId)

        then: "The video is deleted."
        NotFoundException e = thrown()
        e.getMessage() == "message.video.not-found"
    }

    def "Delete a nonexisting video should throw a not found exception"() {
        given: "I have a nonexisting video id."
        def videoId = UUID.randomUUID()

        when: "I try to delete this nonexisting video."
        videoService.deleteOrUndeleteById(videoId, true)


        then: "The system throws an excpetion."
        NotFoundException e = thrown()
        e.getMessage() == "message.video.not-found"
    }

    def "Undelete a video"() {
        given: "I have a deleted video."
        def multipartVideo = VideoMock.buildMultipartFileVideo()
        def request = VideoMock.buildCreateVideoRequest()
        def videoId = videoService.create(channelId, multipartVideo, request)
        videoService.deleteOrUndeleteById(videoId, true)

        when: "I try to undelete this video."
        videoService.deleteOrUndeleteById(videoId, false)

        then: "The video is correctly restored."
        def optionalVideo = videoService.findById(videoId)
        optionalVideo.isPresent()
    }


}
