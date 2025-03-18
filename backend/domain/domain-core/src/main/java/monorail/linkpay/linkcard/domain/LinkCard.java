package monorail.linkpay.linkcard.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Table(name = "link_card")
@Getter
@EqualsAndHashCode(of = "linkCardId", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class LinkCard extends BaseEntity {

    @Id
    private Long linkCardId;
    private String cardNumber;
    private String cvcNumber;
    private LocalDateTime deletedAt;
    private CardType cardType;
    private Long linkCardDetailsId;
    private Long memberId;
    private Long linkWalletId;
    private Long myWalletId;

    @Builder
    public LinkCard(
            final Long linkCardId,
            final String cardNumber,
            final String cvcNumber,
            final LocalDateTime deletedAt,
            final CardType cardType,
            final Long linkCardDetailsId,
            final Long memberId,
            final Long linkWalletId,
            final Long myWalletId
    ) {
        this.linkCardId = linkCardId;
        this.cardNumber = cardNumber;
        this.cvcNumber = cvcNumber;
        this.deletedAt = deletedAt;
        this.cardType = cardType;
        this.linkCardDetailsId = linkCardDetailsId;
        this.memberId = memberId;
        this.linkWalletId = linkWalletId;
        this.myWalletId = myWalletId;
    }
}
