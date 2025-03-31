package monorail.linkpay.linkcard.service;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.repository.LinkCardRepository;

@SupportLayer
@RequiredArgsConstructor
public class LinkCardFetcher {

    private final LinkCardRepository linkCardRepository;

    public void checkNotExistsByWalletId(final Long walletId) {
        if (linkCardRepository.existsByWalletId(walletId)) {
            throw new LinkPayException(INVALID_REQUEST, "해당 지갑에 링크카드가 존재합니다.");
        }
    }

    public LinkCard fetchById(final Long linkCardId) {
        return linkCardRepository.findById(linkCardId)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "링크카드 아이디에 해당하는 링크카드가 없습니다."));
    }

    public LinkCard fetchByIdForUpdate(Long linkCardId) {
        return linkCardRepository.findByIdForUpdate(linkCardId)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "링크카드 아이디에 해당하는 링크카드가 없습니다."));
    }
}
