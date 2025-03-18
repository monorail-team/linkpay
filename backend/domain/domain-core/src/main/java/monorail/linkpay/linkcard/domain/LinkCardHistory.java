package monorail.linkpay.linkcard.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.Point;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "link_card_history")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "linkCardHistoryId", callSuper = false)
@Entity
public class LinkCardHistory {

    @Id
    private Long linkCardHistoryId;

    @Column(nullable = false)
    private String merchantName;

    @Embedded
    private Point point;

    @Column(nullable = false)
    @Enumerated(STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "link_card_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkCard linkCard;

    @Builder
    public LinkCardHistory(
            final Long linkCardHistoryId,
            final String merchantName,
            final Point point,
            final PaymentStatus paymentStatus,
            final LinkCard linkCard
    ) {
        this.linkCardHistoryId = linkCardHistoryId;
        this.merchantName = merchantName;
        this.point = point;
        this.paymentStatus = paymentStatus;
        this.createdAt = LocalDateTime.now();
        this.linkCard = linkCard;
    }
}
