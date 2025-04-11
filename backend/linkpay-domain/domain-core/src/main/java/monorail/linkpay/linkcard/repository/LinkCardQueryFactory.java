package monorail.linkpay.linkcard.repository;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.LinkPayException;

@Getter
@RequiredArgsConstructor
public enum LinkCardQueryFactory {

    OWNED("""
            SELECT lc
              FROM LinkCard lc
              JOIN FETCH lc.member m
              JOIN FETCH lc.wallet w
             WHERE lc.member.id = :memberId
               AND lc.cardType = 'OWNED'
               AND (:lastId IS NULL OR lc.id < :lastId)
             ORDER BY lc.id DESC
            """),
    LINKED("""
            SELECT lc
              FROM LinkCard lc
              JOIN FETCH lc.member m
              JOIN FETCH lc.wallet w
             WHERE lc.member.id = :memberId
               AND lc.cardType = 'SHARED'
               AND (:lastId IS NULL OR lc.id < :lastId)
             ORDER BY lc.id DESC
            """),
    SHARED("""
            SELECT lc
              FROM LinkCard lc
              JOIN LinkedMember lm
                ON lm.linkedWallet.id = lc.wallet.id
              JOIN FETCH lc.member
              JOIN FETCH lc.wallet
             WHERE lm.member.id = :memberId
               AND lm.role = 'CREATOR'
               AND lc.member.id != :memberId
               AND lc.cardType = 'SHARED'
               AND (:lastId IS NULL OR lc.id < :lastId)
             ORDER BY lc.id DESC
            """);

    private final String query;

    public static String from(final String type) {
        return Arrays.stream(values())
                .filter(c -> c.name().equalsIgnoreCase(type))
                .findAny()
                .map(LinkCardQueryFactory::getQuery)
                .orElseThrow(() -> new LinkPayException(INVALID_REQUEST, "잘못된 카드 타입 값입니다."));
    }
}
