package sg.backend.dto.response.funding;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.FunderDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.Funder;

import java.util.List;

@Getter
public class GetFunderListResponseDto extends ResponseDto {

    private List<FunderDataDto> data;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    private GetFunderListResponseDto(Page<Funder> funderList, List<FunderDataDto> data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
        this.page = funderList.getNumber();
        this.size = funderList.getSize();
        this.totalPages = funderList.getTotalPages();
        this.totalElements = funderList.getTotalElements();
    }

    public static ResponseEntity<GetFunderListResponseDto> success(Page<Funder> funderList, List<FunderDataDto> data) {
        GetFunderListResponseDto result = new GetFunderListResponseDto(funderList, data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
