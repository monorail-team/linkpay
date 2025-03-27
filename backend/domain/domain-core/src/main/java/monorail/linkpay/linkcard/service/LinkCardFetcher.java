package monorail.linkpay.linkcard.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.repository.LinkCardRepository;
import monorail.linkpay.member.domain.Member;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@SupportLayer
@RequiredArgsConstructor
public class LinkCardFetcher {

    private final LinkCardRepository linkCardRepository;

    public LinkCard fetchById(Long linkCardId) {
        return linkCardRepository.findById(linkCardId)
                .orElseThrow(() -> new LinkPayException(
                        NOT_FOUND_RESOURCE,
                        "링크카드 아이디에 해당하는 링크카드가 없습니다."));
    }

    public LinkCard fetchByOwnerId(Long linkCardId, Member member) {
        LinkCard linkCard = linkCardRepository.findById(linkCardId)
                .orElseThrow(() -> new LinkPayException(
                        NOT_FOUND_RESOURCE,
                        "링크카드 아이디에 해당하는 링크카드가 없습니다."));

        if (!linkCard.getMember().equals(member)) {
            throw new LinkPayException(INVALID_REQUEST, "카드의 소유자가 아닙니다.");
        }
        return linkCard;
    }
}
