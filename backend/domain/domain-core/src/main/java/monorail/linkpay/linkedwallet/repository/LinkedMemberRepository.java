package monorail.linkpay.linkedwallet.repository;

import monorail.linkpay.linkedwallet.domain.LinkedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedMemberRepository extends JpaRepository<LinkedMember, Long> {
}
