package monorail.linkpay.linkedwallet.repository.dto;

public interface LinkedWalletDto {

    Long getLinkedWalletId();

    String getLinkedWalletName();

    Long getAmount();

    int getParticipantCount();
}
