package monorail.linkpay.linkcard.domain;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.Point;

@Table(name = "link_card_history")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
public class LinkCardHistory {

    @Id
    @Column(name = "link_card_history_id")
    private Long id;

    @Column(nullable = false)
    private String merchantName;

    @Embedded
    private Point point;

    @Column(nullable = false)
    @Enumerated(STRING)
    private PaymentState paymentState;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "link_card_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkCard linkCard;

    @Builder
    public LinkCardHistory(
            final Long id,
            final String merchantName,
            final Point point,
            final PaymentState paymentState,
            final LinkCard linkCard
    ) {
        this.id = id;
        this.merchantName = merchantName;
        this.point = point;
        this.paymentState = paymentState;
        this.createdAt = LocalDateTime.now();
        this.linkCard = linkCard;
    }
}
