package monorail.linkpay.store.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "store")
@Getter
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE store SET deleted_at = CURRENT_TIMESTAMP WHERE store_id = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class Store extends BaseEntity {

    @Id
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Builder
    private Store(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Store store)) {
            return false;
        }
        return id != null && Objects.equals(id, store.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
