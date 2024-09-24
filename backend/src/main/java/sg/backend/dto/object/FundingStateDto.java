package sg.backend.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundingStateDto {

    @Schema(description = "심사 대기 게시물 수")
    int review;

    @Schema(description = "심사 완료 게시물 수")
    int reviewCompleted;

    @Schema(description = "진행 중인 게시물 수")
    int onGoing;
}
