package br.com.zup.bootcamp.zuptubeapi.features.subscription;

import br.com.zup.bootcamp.zuptubeapi.exception.NotFoundException;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponseByAccount;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponseByChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public UUID create(final UUID accountId, final UUID channelId) {
        return this.subscriptionRepository.create(accountId, channelId);
    }

    public Optional<SubscriptionResponse> find(final UUID accountId, final UUID channelId) {
        return this.subscriptionRepository.find(accountId, channelId)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("message.channel.subscription.not-found"));
    }

    public List<SubscriptionResponseByChannel> findAllByChannelId(final UUID channelId) {
        return this.subscriptionRepository.findAllByChannelId(channelId);
    }

    public List<SubscriptionResponseByAccount> findAllByAccountId(final UUID accountId) {
        return this.subscriptionRepository.findAllByAccountId(accountId);
    }

    @Transactional
    public void deleteOrUndelete(final UUID accountId, final UUID channelId, final Boolean deleteOrUndelete) {
        this.subscriptionRepository.deleteOrUndelete(accountId, channelId, deleteOrUndelete);
    }
}
