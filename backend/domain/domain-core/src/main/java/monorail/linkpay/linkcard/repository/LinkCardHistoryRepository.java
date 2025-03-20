package monorail.linkpay.linkcard.repository;

import monorail.linkpay.linkcard.domain.LinkCardHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkCardHistoryRepository extends JpaRepository<LinkCardHistory, Long> {
}
