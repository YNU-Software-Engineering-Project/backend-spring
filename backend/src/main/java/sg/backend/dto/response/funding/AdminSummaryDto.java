package sg.backend.dto.response.funding;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminSummaryDto {

    private long totalUsers;  //총회원수
    private long todayUsers;  //오늘가입자수
    private double averageFundingLikes; //일인당 평균찜개수
    private double averageFundingAmount;  //일인당 평균펀딩금액
    private long totalFundings;  //전체펀딩수
    private long closedFundings;  //종료된펀딩수
    private double successRate;   //펀딩성공률(성공/종료)
    private long ongoingFundings;  //진행중펀딩수
    private long successfulFundings;  //성공한펀딩수
    private long failedFundings;  //미달펀딩수
}
