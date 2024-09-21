package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.object.FundingDataDto;
import sg.backend.dto.object.MyFundingDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetMyFundingListResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.Tag;
import sg.backend.entity.User;
import sg.backend.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FundingLikeRepository fundingLikeRepository;
    private final FundingTagRepository fundingTagRepository;
    private final FunderRepository funderRepository;
    private final FundingRepository fundingRepository;

    public ResponseEntity<? super GetFundingListResponseDto> getWishList(Long userId, int page, int size) {

        User user = null;
        List<FundingDataDto> data = new ArrayList<>();

        try {
            user = userRepository.findByUserId(userId);
            if(user == null) return GetFundingListResponseDto.noExistUser();

            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Funding> fundingList = fundingLikeRepository.findFundingLikedByUserIdOrderByLikeCreatedAt(userId, pageRequest);

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

        return GetFundingListResponseDto.success(data);
    }

    public ResponseEntity<? super GetFundingListResponseDto> getPledgeList(Long userId, int page, int size) {

        User user = null;
        List<FundingDataDto> data = new ArrayList<>();

        try {
            user = userRepository.findByUserId(userId);
            if(user == null) return GetFundingListResponseDto.noExistUser();

            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Funding> fundingList = funderRepository.findFundingByUserIdOrderByFunderCreatedAt(userId, pageRequest);

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

                boolean isLike = fundingLikeRepository.existsByUserAndFunding(user, f);
                dto.setLike(isLike);
                dto.setState(String.valueOf(f.getCurrent()));
                data.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetFundingListResponseDto.success(data);
    }

    public ResponseEntity<? super GetMyFundingListResponseDto> getMyFundingList(Long userId, int page, int size) {

        User user = null;
        List<MyFundingDataDto> data = new ArrayList<>();
        int todayAmount = 0;
        int todayLikes = 0;

        try {
            user = userRepository.findByUserId(userId);
            if(user == null) return GetMyFundingListResponseDto.noExistUser();

            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Funding> fundingList = fundingRepository.findByUserUserId(userId, pageRequest);

            for(Funding f : fundingList) {
                MyFundingDataDto dto = new MyFundingDataDto();
                dto.setTitle(f.getTitle());
                dto.setMainImage(f.getMainImage());
                dto.setState(String.valueOf(f.getCurrent()));

                todayAmount += f.getTodayAmount();
                todayLikes += f.getTodayLikes();
                data.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetMyFundingListResponseDto.success(data, todayAmount, todayLikes);
    }
}
