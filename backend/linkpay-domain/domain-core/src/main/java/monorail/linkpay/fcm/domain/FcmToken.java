package monorail.linkpay.fcm.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.member.domain.Member;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

import static lombok.AccessLevel.PROTECTED;

@Table(
        name = "fcm_token",
        indexes = {
                @Index(name = "idx_token", columnList = "token"),
                @Index(name = "idx_device_id", columnList = "device_id")
        }
)
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

    @Column(unique = true, nullable = false)
    private String token;

    @Column(unique = true, nullable = false)
    private String deviceId;

    private Instant expiresAt;

    @Builder
    private FcmToken(Long id, Member member, String token, String deviceId, Instant expiresAt) {
        this.id = id;
        this.member = member;
        this.token = token;
        this.deviceId = deviceId;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public void update(Member member, String token, String deviceId, Instant expiresAt) {
        this.member = member;
        this.token = token;
        this.deviceId = deviceId;
        this.expiresAt = expiresAt;
    }
}
