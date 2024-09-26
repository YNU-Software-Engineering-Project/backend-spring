package sg.backend.dto.response.user;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.UserDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.User;

import java.util.List;

@Getter
public class GetUserListResponseDto extends ResponseDto {

    private List<UserDataDto> data;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    private GetUserListResponseDto(Page<User> userList, List<UserDataDto> data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
        this.page = userList.getNumber();
        this.size = userList.getSize();
        this.totalPages = userList.getTotalPages();
        this.totalElements = userList.getTotalElements();
    }

    public static ResponseEntity<GetUserListResponseDto> success(Page<User> userList, List<UserDataDto> data) {
        GetUserListResponseDto result = new GetUserListResponseDto(userList, data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
