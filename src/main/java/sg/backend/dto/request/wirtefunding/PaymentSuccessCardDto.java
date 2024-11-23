package sg.backend.dto.request.wirtefunding;

import lombok.Data;

@Data
public class PaymentSuccessCardDto {
    String company;
    String number;
    String installmentPlanMonths;
    String isInterestFree;
    String approveNo;
    String useCardPoint;
    String cardType;
    String ownerType;
    String acquireStatus;
    String receiptUrl;
}
