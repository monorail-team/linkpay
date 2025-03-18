package monorail.linkpay.linkedwallet.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;

import static lombok.AccessLevel.PROTECTED;

@Table(name = "linked_wallet")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "linkedWalletId", callSuper = false)
@Entity
public class LinkedWallet extends BaseEntity {

    @Id
    private Long linkedWalletId;

    @Embedded
    private Point point;
}
