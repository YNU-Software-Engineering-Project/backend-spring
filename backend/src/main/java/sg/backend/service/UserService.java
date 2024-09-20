package sg.backend.service;

import org.springframework.http.ResponseEntity;
import sg.backend.dto.response.funding.GetFundingListResponseDto;

public interface UserService {
    ResponseEntity<? super GetFundingListResponseDto> getWishList(Long userId, int page, int size);
    ResponseEntity<? super GetFundingListResponseDto> getPledgeList(Long userId, int page, int size);
}
