package sg.backend.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.backend.dto.object.FunderDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.GetFunderListResponseDto;
import sg.backend.entity.*;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sg.backend.service.UserService.formatter;

@Service
@RequiredArgsConstructor
public class FunderService {

    private final FundingRepository fundingRepository;
    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public ResponseEntity<? super GetFunderListResponseDto> getFunderList(String email, Long fundingId, String sort, String id, String rewardOption, int page, int size) {

        User loginUser;
        QFunder funder = QFunder.funder;
        BooleanBuilder filterBuilder = new BooleanBuilder();
        Page<Funder> funderList;
        List<FunderDataDto> data = new ArrayList<>();

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return ResponseDto.noExistUser();
            loginUser = optionalUser.get();

            Optional<Funding> optionalFunding = fundingRepository.findById(fundingId);
            if(optionalFunding.isEmpty()) return ResponseDto.noExistFunding();
            Funding funding = optionalFunding.get();

            if(!funding.getUser().getUserId().equals(loginUser.getUserId()))
                return ResponseDto.noPermission();

            filterBuilder.and(funder.funding.eq(funding));

            if(id != null) {
                StringTemplate funderId = Expressions.stringTemplate("SUBSTRING({0}, 1, LOCATE('@', {0}) - 1)", funder.user.email);
                filterBuilder.and(funderId.contains(id));
            }

            QSelectedReward selectedReward = QSelectedReward.selectedReward;
            QReward reward = QReward.reward;

            if (rewardOption != null) {
                filterBuilder.and(selectedReward.reward.rewardName.eq(rewardOption));
            }

            if(sort.equals("idAsc")) sort = "emailAsc";
            else if(sort.equals("idDesc")) sort = "emailDesc";

            OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sort, funder);

            Pageable pageable = PageRequest.of(page, size);

            List<Funder> results = queryFactory.selectFrom(funder)
                    .join(funder.selrewardList, selectedReward)
                    .join(selectedReward.reward, reward)
                    .where(filterBuilder)
                    .orderBy(orderSpecifier)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            long total = queryFactory.selectFrom(funder)
                    .leftJoin(funder.selrewardList, selectedReward)
                    .leftJoin(selectedReward.reward, reward)
                    .where(filterBuilder)
                    .fetch()
                    .size();

            funderList = new PageImpl<>(results, pageable, total);

            for(Funder f : funderList) {
                data.add(convertToFunderDataDto(f, fundingId));
            }


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetFunderListResponseDto.success(funderList, data);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sort, QFunder funder) {
        switch (sort) {
            case "latest":
                return funder.createdAt.desc();
            case "oldest":
                return funder.createdAt.asc();
            case "nicknameAsc":
                return funder.user.nickname.asc();
            case "nicknameDesc":
                return funder.user.nickname.desc();
            case "emailAsc":
                return funder.user.email.asc();
            case "emailDesc":
                return funder.user.email.desc();
            case "phoneNumAsc":
                return funder.user.phoneNumber.asc();
            case "phoneNumDesc":
                return funder.user.phoneNumber.desc();
            case "adAsc":
                return Expressions.stringTemplate("CASE WHEN {0} IS NOT NULL AND {0} != '' THEN CONCAT({0}, ' ', {1}) ELSE CONCAT({2}, ' ', {1}) END",
                        funder.user.roadAddress, funder.user.detailAddress, funder.user.landLotAddress).asc();
            case "adDesc":
                return Expressions.stringTemplate("CASE WHEN {0} IS NOT NULL AND {0} != '' THEN CONCAT({0}, ' ', {1}) ELSE CONCAT({2}, ' ', {1}) END",
                        funder.user.roadAddress, funder.user.detailAddress, funder.user.landLotAddress).desc();
            default:
                return funder.createdAt.asc();
        }
    }

    @Transactional
    public FunderDataDto convertToFunderDataDto(Funder funder, Long fundingId) {
        FunderDataDto dto = new FunderDataDto();

        dto.setCreatedAt(funder.getCreatedAt().format(formatter));

        String email = funder.getUser().getEmail();
        int emailIndex = email.indexOf("@");
        dto.setId(email.substring(0, emailIndex));

        if(funder.getUser().getNickname() == null)
            dto.setNickname("");
        else
            dto.setNickname(funder.getUser().getNickname());

        dto.setEmail(funder.getUser().getEmail());

        String roadAddress = funder.getUser().getRoadAddress();
        String landLotAddress = funder.getUser().getLandLotAddress();
        String detailAddress = funder.getUser().getDetailAddress();
        StringBuilder address = new StringBuilder();
        if(roadAddress != null && detailAddress != null) {
            address.append(roadAddress + " ").append(detailAddress);
        } else if(landLotAddress != null && detailAddress != null) {
            address.append(landLotAddress + " ").append(detailAddress);

        }
        dto.setAddress(address.toString());

        dto.setPhoneNumber(funder.getUser().getPhoneNumber());

        QSelectedReward selectedReward = QSelectedReward.selectedReward;
        QReward reward = QReward.reward;

        List<String> rewards = queryFactory.select(reward.rewardName)
                .from(selectedReward)
                .join(selectedReward.reward, reward)
                .where(selectedReward.funder.eq(funder)
                        .and(reward.funding.funding_id.eq(fundingId)))
                .fetch();

        dto.setRewards(rewards);

        return dto;
    }
}
