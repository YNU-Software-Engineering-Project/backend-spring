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
public class GetFundingByStateResponseDto extends ResponseDto {
    private final List<ShortFundingDataDto> data;
    private final int page;
    private final int size;
    private final int totalPages;
    private final long totalElements;

    private GetFundingByStateResponseDto(Page<Funding> fundingList, List<ShortFundingDataDto> data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
        this.page = fundingList.getNumber();
        this.size = fundingList.getSize();
        this.totalPages = fundingList.getTotalPages();
        this.totalElements = fundingList.getTotalElements();
    }

    public static ResponseEntity<GetFundingByStateResponseDto> success(Page<Funding> fundingList, List<ShortFundingDataDto> data) {
        GetFundingByStateResponseDto result = new GetFundingByStateResponseDto(fundingList, data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
