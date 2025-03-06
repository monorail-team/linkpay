package backend.a105.member.repository;

import backend.a105.layer.DataAccessLayer;
import backend.a105.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@DataAccessLayer
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
