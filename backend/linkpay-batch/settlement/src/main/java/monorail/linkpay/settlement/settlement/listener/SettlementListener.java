package monorail.linkpay.settlement.settlement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.settlement.message.settlement.CreateMessage;
import monorail.linkpay.settlement.settlement.service.SettlementService;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableKafka
public class SettlementListener {

    private final ObjectMapper objectMapper;
    private final SettlementService settlementService;

    @KafkaListener(topics = "create", groupId = "settlement")
    public void createSettlement(@Payload String messageJson, Acknowledgment ack) throws JsonProcessingException {
        CreateMessage message = objectMapper.readValue(messageJson, CreateMessage.class);
        settlementService.create(message.walletId(), message.storeId(), message.amount());
        ack.acknowledge();
    }
}
