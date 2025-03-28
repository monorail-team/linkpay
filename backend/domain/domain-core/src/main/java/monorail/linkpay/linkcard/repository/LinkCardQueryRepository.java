package monorail.linkpay.linkcard.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.linkcard.domain.LinkCard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkCardQueryRepository {

    private final EntityManager em;

    public Slice<LinkCard> findLinkCardsByMemberId(final String query, final Long memberId,
                                                   final Long lastId, final Pageable pageable) {
        int pageSize = pageable.getPageSize();
        List<LinkCard> linkCards = em.createQuery(query, LinkCard.class)
                .setParameter("memberId", memberId)
                .setParameter("lastId", lastId)
                .setMaxResults(pageSize + 1)
                .getResultList();
        boolean hasNext = checkHasNext(linkCards, pageSize);
        if (hasNext) {
            linkCards = linkCards.subList(0, pageSize);
        }
        return new SliceImpl<>(linkCards, pageable, hasNext);
    }

    private boolean checkHasNext(final List<LinkCard> linkCards, final int pageSize) {
        return linkCards.size() == pageSize + 1;
    }
}
