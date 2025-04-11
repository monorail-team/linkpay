package monorail.linkpay.fcm.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.fcm.domain.FcmToken;
import monorail.linkpay.fcm.repository.FcmTokenRepository;

@SupportLayer
@RequiredArgsConstructor
public class FcmTokenFetcher {

    private final FcmTokenRepository fcmTokenRepository;

    public List<FcmToken> fetchAllByMemberId(final Long memberId) {
        return fcmTokenRepository.findAllByMemberId(memberId);
    }
}
