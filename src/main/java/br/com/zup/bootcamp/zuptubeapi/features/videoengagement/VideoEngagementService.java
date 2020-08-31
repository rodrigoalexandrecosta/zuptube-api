package br.com.zup.bootcamp.zuptubeapi.features.videoengagement;

import br.com.zup.bootcamp.zuptubeapi.exception.NotFoundException;
import br.com.zup.bootcamp.zuptubeapi.model.entity.VideoEngagement;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateVideoEngagementRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateVideoEngagementRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoEngagementService {

    private final VideoEngagementRepository videoEngagementRepository;

    @Transactional
    public UUID create(final UUID accountId, final UUID videoId, final CreateVideoEngagementRequest request) {
        return this.videoEngagementRepository.create(accountId, videoId, request);
    }

    public Optional<VideoEngagement> findById(final UUID videoEngagementId) {
        return this.videoEngagementRepository.findById(videoEngagementId)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("message.video.engagement.not-found"));
    }

    public Optional<VideoEngagement> findByAccountIdAndVideoId(final UUID accountId, final UUID videoId) {
        return this.videoEngagementRepository.findByAccountIdAndVideoId(accountId, videoId)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("message.video.engagement.not-found"));
    }

    public List<VideoEngagement> findAllByVideoId(final UUID videoId) {
        return this.videoEngagementRepository.findAllByVideoId(videoId);
    }

    public List<VideoEngagement> findAllByAccountId(final UUID videoId) {
        return this.videoEngagementRepository.findAllByAccountId(videoId);
    }

    private boolean existsById(final UUID videoEngagementId) {
        return this.videoEngagementRepository.existsById(videoEngagementId);
    }

    private boolean existsByAccountIdAndVideoId(final UUID accountId, final UUID videoId) {
        return this.videoEngagementRepository.existsByAccountIdAndVideoId(accountId, videoId);
    }

    @Transactional
    public void updateById(final UUID videoEngagementId, final UpdateVideoEngagementRequest request) {
        if (existsById(videoEngagementId)) {
            this.videoEngagementRepository.updateById(videoEngagementId, request);
        } else {
            throw new NotFoundException("message.video.engagement.not-found");
        }
    }

    @Transactional
    public void updateByAccountIdAndVideoId(final UUID accountId,final UUID videoId,
                                            final UpdateVideoEngagementRequest request) {
        if (existsByAccountIdAndVideoId(accountId, videoId)) {
            this.videoEngagementRepository.updateByAccountIdAndVideoId(accountId, videoId, request);
        } else {
            throw new NotFoundException("message.video.engagement.not-found");
        }
    }
}
