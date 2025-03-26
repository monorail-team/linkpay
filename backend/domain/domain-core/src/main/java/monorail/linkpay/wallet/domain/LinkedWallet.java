package monorail.linkpay.wallet.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("LINKED")
@Entity
public final class LinkedWallet extends Wallet {

    @Column(nullable = false)
    private String name;

    @Builder
    private LinkedWallet(final Long id, final String name) {
        super(id);
        this.name = name;
    }
}
