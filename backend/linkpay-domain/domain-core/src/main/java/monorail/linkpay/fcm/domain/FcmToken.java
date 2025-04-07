package monorail.linkpay.fcm.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.member.domain.Member;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import static lombok.AccessLevel.PROTECTED;

@Table(name = "fcm_token")
@Getter
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE fcm_token SET deleted_at = CURRENT_TIMESTAMP WHERE fcm_token = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class FcmToken extends BaseEntity {

    @Id
    @Column(name = "fcm_token_id")
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String token;

    @Builder
    private FcmToken(Long id, Member member, String token) {
        this.id = id;
        this.member = member;
        this.token = token;
    }
}
