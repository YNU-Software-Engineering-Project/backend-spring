package sg.backend.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.object.FundingDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.GetUserWishListResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.Tag;
import sg.backend.entity.User;
import sg.backend.repository.FundingLikeRepository;
import sg.backend.repository.FundingTagRepository;
import sg.backend.repository.UserRepository;
import sg.backend.service.FundingService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundingServiceImpl implements FundingService {

    private final UserRepository userRepository;
    private final FundingLikeRepository fundingLikeRepository;
    private final FundingTagRepository fundingTagRepository;

    @Override
    public ResponseEntity<? super GetUserWishListResponseDto> getWishList(Long userId, int page, int size) {

        User user = null;
        List<FundingDataDto> data = new ArrayList<>();

        try {
            user = userRepository.findByUserId(userId);
            if(user == null) return GetUserWishListResponseDto.noExistUser();

            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Funding> fundingList = fundingLikeRepository.findFundingLikedByUser(user, pageRequest);

            for(Funding f : fundingList) {
                FundingDataDto dto = new FundingDataDto();
                dto.setTitle(f.getTitle());
                dto.setMainImage(f.getMainImage());
                dto.setProjectSummary(f.getProjectSummary());
                dto.setCategory(String.valueOf(f.getCategory()));
                dto.setSubCategory(String.valueOf(f.getSubCategory()));

                List<Tag> tagList = fundingTagRepository.findTagByFundingId(f.getFundingId());
                List<String> tag = new ArrayList<>();
                for(Tag t : tagList) {
                    tag.add(t.getTagName());
                }
                dto.setTag(tag);

                int targetAmount = f.getTargetAmount();
                int currentAmount = f.getCurrentAmount();
                int achievementRate;
                if(currentAmount == 0) achievementRate = 0;
                else achievementRate = (int) (((double) currentAmount / targetAmount) * 100);
                dto.setAchievementRate(achievementRate);

                dto.setLike(true);
                dto.setState(String.valueOf(f.getCurrent()));
                data.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetUserWishListResponseDto.success(data);
    }
}
