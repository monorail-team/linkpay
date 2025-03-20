package monorail.linkpay.linkcard.repository;

import monorail.linkpay.linkcard.domain.LinkCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkCardRepository extends JpaRepository<LinkCard, Long> {
}
