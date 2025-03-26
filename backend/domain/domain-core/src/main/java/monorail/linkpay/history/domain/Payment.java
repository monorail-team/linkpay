package monorail.linkpay.history.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Table(name = "payment")
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Payment {

    @Id
    @Column(name = "payment_id")
    private Long id;

    @JoinColumn(name = "link_card_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkCard linkCard;

    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false, updatable = false))
    private Point amount;

    @Column(nullable = false, updatable = false)
    private String merchantName;

    @Column(nullable = false, updatable = false)
    private LocalDateTime historyDate;

    @Builder
    public Payment(final Long id,
                   final LinkCard linkCard,
                   final Member member,
                   final Point amount,
                   final String merchantName,
                   final LocalDateTime historyDate) {
        this.id = id;
        this.linkCard = linkCard;
        this.member = member;
        this.amount = amount;
        this.merchantName = merchantName;
        this.historyDate = historyDate;
    }
}
