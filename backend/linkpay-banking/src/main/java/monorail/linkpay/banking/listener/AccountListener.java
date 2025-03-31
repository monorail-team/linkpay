package monorail.linkpay.banking.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.banking.account.domain.Point;
import monorail.linkpay.banking.account.service.AccountService;
import monorail.linkpay.banking.listener.message.CreateMessage;
import monorail.linkpay.banking.listener.message.DeductMessage;
import monorail.linkpay.banking.listener.message.DeleteMessage;
import monorail.linkpay.banking.listener.message.WithdrawalMessage;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableKafka
public class AccountListener {

    private final AccountService accountService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "create", groupId = "account")
    public void create(@Payload String messageJson, Acknowledgment ack) throws JsonProcessingException {
        CreateMessage message = objectMapper.readValue(messageJson, CreateMessage.class);
        accountService.create(message.walletId(), message.memberId());
        ack.acknowledge();
    }

    @KafkaListener(topics = "delete", groupId = "account")
    public void delete(@Payload String messageJson, Acknowledgment ack) throws JsonProcessingException {
        DeleteMessage message = objectMapper.readValue(messageJson, DeleteMessage.class);
        accountService.delete(message.walletId(), message.memberId());
        ack.acknowledge();
    }

    @KafkaListener(topics = "deduct", groupId = "account")
    public void deductPoint(@Payload String messageJson, Acknowledgment ack) throws JsonProcessingException {
        DeductMessage message = objectMapper.readValue(messageJson, DeductMessage.class);
        accountService.deduct(message.walletId(), new Point(message.amount()));
        ack.acknowledge();
    }

    @KafkaListener(topics = "withdrawal", groupId = "account")
    public void withdrawalPoint(@Payload String messageJson, Acknowledgment ack) throws JsonProcessingException {
        WithdrawalMessage message = objectMapper.readValue(messageJson, WithdrawalMessage.class);
        accountService.withdrawal(message.walletId(), new Point(message.amount()));
        ack.acknowledge();
    }
}
