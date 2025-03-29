package monorail.linkpay.member.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import monorail.linkpay.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    List<Member> findMembersByIdIn(Set<Long> memberIds);
}
