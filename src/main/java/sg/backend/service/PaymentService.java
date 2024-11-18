package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import sg.backend.dto.request.wirtefunding.PaySuccessRequestDto;
import sg.backend.dto.request.wirtefunding.PaymentSuccessDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.writefunding.PaySuccessResponseDto;
import sg.backend.entity.*;
import sg.backend.repository.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final String secretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
    private final RewardRepository rewardRepository;
    private final FundingRepository fundingRepository;
    private final UserRepository userRepository;
    private final FunderRepository funderRepository;
    private final SelectedRewardRepository selectedRewardReository;

    // Toss Payments 인증 헤더 생성
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = secretKey + ":"; // Secret Key + Colon
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // 결제 상태 확인
    public PaymentSuccessDto checkPaymentStatus(String paymentKey) {
        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();

        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<PaymentSuccessDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    PaymentSuccessDto.class
            );
            System.out.println("결제 상태 확인 성공: " + response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            System.out.println("결제 상태 확인 실패: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // 결제 승인 요청
    public ResponseEntity<? super PaySuccessResponseDto> paySuccess(String email, Long funding_id, PaySuccessRequestDto paySuccessRequestDto) {
        System.out.println("Received paymentKey: " + paySuccessRequestDto.getPaymentKey());
        System.out.println("Received orderId: " + paySuccessRequestDto.getOrderId());

        // 1. 결제 상태 확인
        PaymentSuccessDto existingPayment = checkPaymentStatus(paySuccessRequestDto.getPaymentKey());
        if (existingPayment != null && "APPROVED".equals(existingPayment.getStatus())) {
            System.out.println("이미 승인된 결제입니다.");
            return PaySuccessResponseDto.alreadyProcessed();
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();

        // JSON 객체를 구성
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", paySuccessRequestDto.getOrderId());
        params.put("amount", paySuccessRequestDto.getAmount());
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);

        try {
            PaymentSuccessDto result = restTemplate.postForObject(
                    "https://api.tosspayments.com/v1/payments/" + paySuccessRequestDto.getPaymentKey(),
                    request,
                    PaymentSuccessDto.class
            );

            if (result == null) {
                System.out.println("Toss Payments 응답이 비어 있습니다.");
                return PaySuccessResponseDto.not_existed_data();
            }


            //1. getOrderName으로 받은 reward_id와 수량 정보로 reward_id 객체 만듬
            //2. reward객체의 금액 다 더해서 totalAmount와 같은지 확인 -> 금액이 다르면 오류
            //3. funding_id, email 에 대한 funding, user 객체 만듬
            //4. funding, user 객체로 funder 객체 생성
            //5. funder객체, reward객체, 수량으로 selectedReward 객체 생성
            int amount = Integer.parseInt(result.getTotalAmount());
            String order = result.getOrderId();
            Map<Long, Integer> rewards = new HashMap<>();
            //데이터 받아오기
            String[] items = order.replace(" ", "").split(",");
            for (String item : items) {
                if (item.contains(":")) {
                    String[] parts = item.split(":");
                    Long rewardID = Long.parseLong(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);
                    rewards.put(rewardID, quantity);
                }
            }
            int totalAmount = 0;
            //1. getOrderName으로 받은 reward_id와 수량 정보로 reward_id 객체 만듬
            Map<Reward, Integer> rewardMap = new HashMap<>();
            for (Map.Entry<Long, Integer> entry : rewards.entrySet()) {
                Long rewardID = entry.getKey();
                int quantity = entry.getValue();

                Optional<Reward> option = rewardRepository.findById(rewardID);
                if (option.isEmpty()) {
                    return ResponseDto.databaseError();
                }
                Reward reward = option.get();
                rewardMap.put(reward, quantity);
                totalAmount += quantity * reward.getAmount();
            }
            //2. reward객체의 금액 다 더해서 totalAmount와 같은지 확인 -> 금액이 다르면 오류
            if(totalAmount != amount){
                return PaySuccessResponseDto.validation_error();
            }

            //3. funding_id, email 에 대한 funding, user 객체 만듬
            Optional<Funding> option = fundingRepository.findById(funding_id);
            if (option.isEmpty()) {
                return ResponseDto.noExistFunding();
            }
            Funding funding = option.get();
            Optional<User> option1 = userRepository.findByEmail(email);
            if (option1.isEmpty()) {
                return ResponseDto.noExistFunding();
            }
            User user = option1.get();
            //4. funding, user 객체로 funder 객체 생성
            Funder funder = new Funder(user, funding);
            funderRepository.save(funder);

            //5. funder객체, reward객체, 수량으로 selectedReward 객체 생성
            Reward rewardItem;
            int selQuantity;
            for (Map.Entry<Reward, Integer> entry : rewardMap.entrySet()) {
                rewardItem = entry.getKey();
                selQuantity = entry.getValue();
                SelectedReward selReward = new SelectedReward(funder, rewardItem, selQuantity);
                selectedRewardReository.save(selReward);
            }

        } catch (HttpStatusCodeException e) {
            System.out.println("Toss Payments Error: " + e.getResponseBodyAsString());
            return PaySuccessResponseDto.validation_error();
        } catch (Exception e) {
            e.printStackTrace();
            return PaySuccessResponseDto.validation_error();
        }

        return ResponseDto.success();
    }

}
