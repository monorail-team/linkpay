package monorail.linkpay.linkcard.repository;

import java.util.List;
import monorail.linkpay.linkcard.domain.LinkCard;

public interface CustomLinkCardRepository {

    void saveAll(List<LinkCard> linkCards);
}
