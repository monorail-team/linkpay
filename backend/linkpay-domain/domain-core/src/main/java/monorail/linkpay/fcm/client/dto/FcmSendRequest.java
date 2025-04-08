package monorail.linkpay.fcm.client.dto;

import lombok.Builder;

@Builder
public record FcmSendRequest(String token, String title, String body) {
}
