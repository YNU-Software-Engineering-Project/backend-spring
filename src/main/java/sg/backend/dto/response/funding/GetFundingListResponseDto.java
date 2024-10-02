package sg.backend.dto.response.funding;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.FundingDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.Funding;

import java.util.List;

@Getter
public class GetFundingListResponseDto extends ResponseDto {

    private final List<FundingDataDto> data;
    private final int page;
    private final int size;
    private final int totalPages;
    private final long totalElements;

    private GetFundingListResponseDto(Page<Funding> fundingList, List<FundingDataDto> data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
        this.page = fundingList.getNumber();
        this.size = fundingList.getSize();
        this.totalPages = fundingList.getTotalPages();
        this.totalElements = fundingList.getTotalElements();
    }

    public static ResponseEntity<GetFundingListResponseDto> success(Page<Funding> fundingList, List<FundingDataDto> data) {
        GetFundingListResponseDto result = new GetFundingListResponseDto(fundingList, data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
