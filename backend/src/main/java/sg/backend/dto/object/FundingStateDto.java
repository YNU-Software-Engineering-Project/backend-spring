package sg.backend.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingStateDto {

    @Schema(description = "심사 대기 게시물 수")
    long review;

    @Schema(description = "심사 완료 게시물 수")
    long reviewCompleted;

    @Schema(description = "진행 중인 게시물 수")
    long onGoing;

    public static FundingStateDto of(long review, long reviewCompleted, long onGoing) {
        return FundingStateDto.builder()
                .review(review)
                .reviewCompleted(reviewCompleted)
                .onGoing(onGoing)
                .build();
    }
}
