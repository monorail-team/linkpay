package monorail.linkpay.linkcard.repository;

import static java.time.LocalDateTime.now;

import java.util.List;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.linkcard.domain.LinkCard;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomLinkCardRepositoryImpl implements CustomLinkCardRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(final List<LinkCard> linkCards) {
        final String sql = """
                INSERT INTO link_card 
                    (link_card_id, limit_price, card_type, card_color, card_name, expired_at, state, 
                    used_point, member_id, wallet_id, created_at, modified_at) 
                VALUES 
                    (:linkCardId, :limitPrice, :cardType, :cardColor, :cardName, :expiredAt, :state,
                    :usedPoint, :memberId, :walletId, :createdAt, :modifiedAt)
                """;
        namedParameterJdbcTemplate.batchUpdate(sql, createLinkCardsToSqlParameterSources(linkCards));
    }

    private MapSqlParameterSource[] createLinkCardsToSqlParameterSources(final List<LinkCard> linkCards) {
        return linkCards.stream()
                .map(this::createLinkCardToSqlParameterSource)
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource createLinkCardToSqlParameterSource(final LinkCard linkCard) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("linkCardId", linkCard.getId());
        params.addValue("limitPrice", linkCard.getLimitPrice().getAmount());
        params.addValue("cardType", linkCard.getCardType().name());
        params.addValue("cardColor", linkCard.getCardColor().name());
        params.addValue("cardName", linkCard.getCardName());
        params.addValue("expiredAt", linkCard.getExpiredAt());
        params.addValue("state", linkCard.getState().name());
        params.addValue("usedPoint", linkCard.getUsedPoint().getAmount());
        params.addValue("memberId", linkCard.getMember().getId());
        params.addValue("walletId", linkCard.getWalletId());
        params.addValue("createdAt", now());
        params.addValue("modifiedAt", now());
        return params;
    }
}
