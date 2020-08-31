package br.com.zup.bootcamp.zuptubeapi.features.channel;

import br.com.zup.bootcamp.zuptubeapi.exception.NotFoundException;
import br.com.zup.bootcamp.zuptubeapi.model.entity.Channel;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateChannelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;

    @Transactional
    public UUID create(final Channel channel) {
        return this.channelRepository.create(channel);
    }

    public Optional<Channel> findById(final UUID channelId) {
        return this.channelRepository.findById(channelId)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("message.channel.not-found"));
    }

    public Optional<Channel> findByAccountId(final UUID accountId) {
        return this.channelRepository.findByAccountId(accountId)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("message.channel.not-found"));
    }

    public List<Channel> findAll() {
        return this.channelRepository.findAll();
    }

    private boolean existsByAccountId(final UUID accountId) {
        return this.channelRepository.existsByAccountId(accountId);
    }

    @Transactional
    public void deleteOrUndeleteByAccountId(final UUID accountId, final Boolean deleteOrUndelete) {
        if (existsByAccountId(accountId)) {
            this.channelRepository.deleteOrUndeleteByAccountId(accountId, deleteOrUndelete);
        } else {
            throw new NotFoundException("message.channel.not-found");
        }
    }

    @Transactional
    public void updateById(final UUID channelId, final UpdateChannelRequest request) {
        this.findById(channelId)
                .ifPresent(channel -> this.channelRepository.updateById(channelId, request));
    }

    @Transactional
    public void updateByAccountId(final UUID accountId, final UpdateChannelRequest request) {
        this.findByAccountId(accountId)
                .ifPresent(channel -> this.channelRepository.updateByAccountId(accountId, request));
    }
}
