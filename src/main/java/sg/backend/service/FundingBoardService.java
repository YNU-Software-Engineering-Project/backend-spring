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
import sg.backend.dto.object.RewardDataDto;
import sg.backend.dto.response.funding.FundingDashboardResponseDto;
import sg.backend.dto.response.funding.GetFunderListResponseDto;
import sg.backend.dto.response.funding.GetRewardListResponseDto;
import sg.backend.entity.*;
import sg.backend.exception.UnauthorizedAccessException;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static sg.backend.entity.QReward.reward;
import static sg.backend.entity.QSelectedReward.selectedReward;
import static sg.backend.service.FundingService.findFundingById;
import static sg.backend.service.UserService.findUserByEmail;
import static sg.backend.service.UserService.formatter;

@Service
@RequiredArgsConstructor
public class FundingBoardService {

    private final FundingRepository fundingRepository;
    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public boolean checkPermission(String email, Long fundingId) {
        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 펀딩이 존재하지 않습니다."));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        if (funding.getUser().getUserId().equals(user.getUserId())) {
            return true;
        }

        return user.isAdmin();
    }

    @Transactional
    public FundingDashboardResponseDto getFundingDashboard(String email, Long fundingId){
        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(()-> new IllegalArgumentException("해당 펀딩이 존재하지 않습니다."));
        User user = findUserByEmail(email, userRepository);
        checkFundingOwner(funding, user);

        return new FundingDashboardResponseDto(funding);
    }

    @Transactional
    public ResponseEntity<GetFunderListResponseDto> getFunderList(String email, Long fundingId, String sort, String id, Integer rewardNo, int page, int size) {

        User user = findUserByEmail(email, userRepository);
        Funding funding = findFundingById(fundingId, fundingRepository);
        checkFundingOwner(funding, user);

        QFunder funder = QFunder.funder;
        QSelectedReward selectedReward = QSelectedReward.selectedReward;
        QReward reward = QReward.reward;
        BooleanBuilder filterBuilder = createFilterBuilder(funding, id, rewardNo, funder);
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sort, funder);
        Pageable pageable = PageRequest.of(page, size);

        List<Funder> results = queryFactory.selectFrom(funder)
                .join(funder.user, QUser.user).fetchJoin()
                .join(funder.selrewardList, selectedReward).fetchJoin()
                .join(selectedReward.reward, reward).fetchJoin()
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

        Page<Funder> funderList = new PageImpl<>(results, pageable, total);
        List<String> rewards = fetchRewards(selectedReward, funder, fundingId);

        List<FunderDataDto> data = funderList.stream()
                .map(f -> FunderDataDto.of(f, rewards))
                .collect(Collectors.toList());

        return GetFunderListResponseDto.success(funderList, data);
    }

    @Transactional
    public ResponseEntity<GetRewardListResponseDto> getRewardList(Long fundingId) {

        Funding funding = findFundingById(fundingId, fundingRepository);

        List<String> rewards = funding.getRewardList().stream()
                .map(Reward::getRewardName)
                .toList();

        List<RewardDataDto> data = IntStream.range(0, rewards.size())
                .mapToObj(index -> {
                    String reward = rewards.get(index);
                    return RewardDataDto.of(index, reward);
                })
                .collect(Collectors.toList());

        return GetRewardListResponseDto.success(data);
    }

    private void checkFundingOwner(Funding funding, User user) {
        if(!funding.getUser().getUserId().equals(user.getUserId()))
            throw new UnauthorizedAccessException();
    }

    private BooleanBuilder createFilterBuilder(Funding funding, String id, Integer rewardNo, QFunder funder) {
        BooleanBuilder filterBuilder = new BooleanBuilder();
        filterBuilder.and(funder.funding.eq(funding));

        if (id != null) {
            StringTemplate funderId = Expressions.stringTemplate("SUBSTRING({0}, 1, LOCATE('@', {0}) - 1)", funder.user.email);
            filterBuilder.and(funderId.contains(id));
        }

        if (rewardNo != null) {
            String rewardOption = funding.getRewardList().get(rewardNo).getRewardName();
            filterBuilder.and(selectedReward.reward.rewardName.eq(rewardOption));
        }

        return filterBuilder;
    }

    private OrderSpecifier<?> getOrderSpecifier(String sort, QFunder funder) {
        return switch (sort) {
            case "latest" -> funder.createdAt.desc();
            case "nicknameAsc" -> funder.user.nickname.asc();
            case "nicknameDesc" -> funder.user.nickname.desc();
            case "idAsc", "emailAsc" -> funder.user.email.asc();
            case "idDesc", "emailDesc" -> funder.user.email.desc();
            case "phoneNumAsc" -> funder.user.phoneNumber.asc();
            case "phoneNumDesc" -> funder.user.phoneNumber.desc();
            case "adAsc" ->
                    Expressions.stringTemplate("CASE WHEN {0} IS NOT NULL AND {0} != '' THEN CONCAT({0}, ' ', {1}) ELSE CONCAT({2}, ' ', {1}) END",
                            funder.user.roadAddress, funder.user.detailAddress, funder.user.landLotAddress).asc();
            case "adDesc" ->
                    Expressions.stringTemplate("CASE WHEN {0} IS NOT NULL AND {0} != '' THEN CONCAT({0}, ' ', {1}) ELSE CONCAT({2}, ' ', {1}) END",
                            funder.user.roadAddress, funder.user.detailAddress, funder.user.landLotAddress).desc();
            default -> funder.createdAt.asc();
        };
    }

    @Transactional
    public List<String> fetchRewards(QSelectedReward selectedReward, QFunder funder, Long fundingId) {
        return queryFactory.select(reward.rewardName)
                .from(selectedReward)
                .join(selectedReward.reward, reward)
                .where(selectedReward.funder.eq(funder)
                        .and(reward.funding.funding_id.eq(fundingId)))
                .fetch();
    }

}
