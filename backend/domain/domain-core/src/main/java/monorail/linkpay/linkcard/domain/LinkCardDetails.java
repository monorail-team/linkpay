package monorail.linkpay.linkcard.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Table(name = "link_card_details")
@Getter
@EqualsAndHashCode(of = "linkCardDetailsId", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class LinkCardDetails {

    @Id
    private Long linkCardDetailsId;

}
