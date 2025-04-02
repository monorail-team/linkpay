package monorail.linkpay.settlement.settlement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.settlement.message.settlement.CreateMessage;
import monorail.linkpay.settlement.settlement.service.SettlementService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementListener {

    private final ObjectMapper objectMapper;
    private final SettlementService settlementService;

    @KafkaListener(topics = "${topics.settlement.create}", groupId = "settlement")
    public void createSettlement(@Payload final String messageJson, final Acknowledgment ack)
            throws JsonProcessingException {
        CreateMessage message = objectMapper.readValue(messageJson, CreateMessage.class);
        settlementService.create(message.walletId(), message.storeId(), message.amount());
        ack.acknowledge();
    }
}
