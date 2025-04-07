package monorail.linkpay.fcm.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.fcm.domain.FcmToken;
import monorail.linkpay.fcm.repository.FcmTokenRepository;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmService {

    private final FcmTokenRepository fcmTokenRepository;
    private final IdGenerator idGenerator;
    private final MemberFetcher memberFetcher;

    @Transactional
    public void register(final Long memberId, final String token) {
        Member member = memberFetcher.fetchById(memberId);
        fcmTokenRepository.save(FcmToken.builder()
                .id(idGenerator.generate())
                .member(member)
                .token(token)
                .build());
    }
}
