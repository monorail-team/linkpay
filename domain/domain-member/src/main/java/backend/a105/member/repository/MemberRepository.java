package backend.a105.member.repository;

import backend.a105.exception.AppException;
import backend.a105.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static backend.a105.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public Member fetchById(final Long id) {
        return memberJpaRepository.findById(id)
            .orElseThrow(() -> new AppException(NOT_FOUND_RESOURCE, "식별자 " + id + "에 해당하는 멤버가 존재하지 않습니다."));
    }
}
