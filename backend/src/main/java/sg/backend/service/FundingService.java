package sg.backend.service;

import org.springframework.http.ResponseEntity;
import sg.backend.dto.response.funding.GetUserWishListResponseDto;

public interface FundingService {
    ResponseEntity<? super GetUserWishListResponseDto> getWishList(Long userId, int page, int size);
}
