package monorail.linkpay.wallet.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.member.domain.Member;

@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("MY")
@Entity
public final class MyWallet extends Wallet {

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private MyWallet(final Long id, final Member member) {
        super(id);
        Objects.requireNonNull(member);
        this.member = member;
    }
}
