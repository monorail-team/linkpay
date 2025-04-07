package monorail.linkpay.fcm.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.fcm.domain.FcmToken;
import monorail.linkpay.fcm.repository.FcmTokenRepository;

import java.util.List;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@SupportLayer
@RequiredArgsConstructor
public class FcmTokenFetcher {

    private final FcmTokenRepository fcmTokenRepository;

    //todo: fix
    public FcmToken fetchByMemberId(final Long memberId) {
        return fcmTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "회원 ID에 등록된 토큰이 없습니다"));
    }

    public List<FcmToken> fetchAllByMemberId(final Long memberId) {
        return fcmTokenRepository.findAllByMemberId(memberId);
    }

}
