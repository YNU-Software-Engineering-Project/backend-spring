package sg.backend.dto.response.funding;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.ShortFundingDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.Funding;

import java.util.List;

@Getter
public class GetMyFundingListResponseDto extends ResponseDto {

    private List<ShortFundingDataDto> data;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private int todayAmount;
    private int todayLikes;

    private GetMyFundingListResponseDto(Page<Funding> fundingList, List<ShortFundingDataDto> data, int todayAmount, int todayLikes) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
        this.page = fundingList.getNumber();
        this.size = fundingList.getSize();
        this.totalPages = fundingList.getTotalPages();
        this.totalElements = fundingList.getTotalElements();
        this.todayAmount = todayAmount;
        this.todayLikes = todayLikes;
    }

    public static ResponseEntity<GetMyFundingListResponseDto> success(Page<Funding> fundingList, List<ShortFundingDataDto> data, int todayAmount, int todayLikes) {
        GetMyFundingListResponseDto result = new GetMyFundingListResponseDto(fundingList, data, todayAmount, todayLikes);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
