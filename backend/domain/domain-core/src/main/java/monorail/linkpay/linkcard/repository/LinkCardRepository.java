package monorail.linkpay.linkcard.repository;

import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkCardRepository extends JpaRepository<LinkCard, Long> {
    Optional<LinkCard> findByMember(Member member);
}
