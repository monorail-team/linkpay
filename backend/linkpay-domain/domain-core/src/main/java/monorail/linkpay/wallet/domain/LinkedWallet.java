package monorail.linkpay.wallet.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("LINKED")
@Entity
public class LinkedWallet extends Wallet {

    @Column(nullable = true)
    private String name;

    @Builder
    private LinkedWallet(final Long id, final String name) {
        super(id);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final LinkedWallet linkedWallet)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return getId() != null && Objects.equals(getId(), linkedWallet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public void changeName(final String newName) {
        this.name = newName;
    }
}
