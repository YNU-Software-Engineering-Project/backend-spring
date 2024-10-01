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

    private final List<UserDataDto> data;
    private final int page;
    private final int size;
    private final int totalPages;
    private final long totalElements;

    private GetUserListResponseDto(Page<User> userList, List<UserDataDto> data, String sort) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        int page = userList.getNumber();
        int size = userList.getSize();

        for(int i=1; i<=data.size(); i++) {
            UserDataDto dto = data.get(i-1);
            int no = sort.equals("noDesc") ? (page * size) + (data.size() - i + 1) : (page * size) + i;
            dto.setNo(no);
        }

        this.data = data;
        this.page = page;
        this.size = size;
        this.totalPages = userList.getTotalPages();
        this.totalElements = userList.getTotalElements();
    }

    public static ResponseEntity<GetUserListResponseDto> success(Page<User> userList, List<UserDataDto> data, String sort) {
        GetUserListResponseDto result = new GetUserListResponseDto(userList, data, sort);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
