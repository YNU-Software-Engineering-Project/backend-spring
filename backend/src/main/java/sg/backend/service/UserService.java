package sg.backend.service;

import org.springframework.http.ResponseEntity;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetMyFundingListResponseDto;

public interface UserService {
    ResponseEntity<? super GetFundingListResponseDto> getWishList(Long userId, int page, int size);
    ResponseEntity<? super GetFundingListResponseDto> getPledgeList(Long userId, int page, int size);
    ResponseEntity<? super GetMyFundingListResponseDto> getMyFundingList(Long userId, int page, int size);

}
