package monorail.linkpay.wallet.domain;

import static java.util.Objects.requireNonNull;
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
public class MyWallet extends Wallet {

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private MyWallet(final Long id, final Member member) {
        super(id);
        requireNonNull(member);
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final MyWallet myWallet)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return getId() != null && Objects.equals(getId(), myWallet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public boolean isMyWallet(Member member) {
        return this.member.equals(member);
    }
}
