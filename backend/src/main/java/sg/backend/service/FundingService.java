package sg.backend.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.FundingDataDto;
import sg.backend.dto.object.FundingStateDto;
import sg.backend.dto.object.ShortFundingDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.GetFundingByStateResponseDto;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetFundingStateCountResponseDto;
import sg.backend.entity.*;
import sg.backend.exception.CustomException;
import sg.backend.repository.FundingLikeRepository;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.NotificationRepository;
import sg.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static sg.backend.service.UserService.*;

@Service
@RequiredArgsConstructor
public class FundingService {

    private final UserRepository userRepository;
    private final FundingRepository fundingRepository;
    private final FundingLikeRepository fundingLikeRepository;
    private final NotificationRepository notificationRepository;
    private final JPAQueryFactory queryFactory;

    public static Funding findFundingById(Long fundingId, FundingRepository fundingRepository) {
        return fundingRepository.findById(fundingId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_EXISTED_FUNDING, ResponseMessage.NOT_EXISTED_FUNDING));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<GetFundingListResponseDto> searchFunding(
            String email, String keyword, String sort, List<String> categories, List<String> tags, int minRate, int maxRate, Boolean isClosed,
            Boolean isLiked, int page, int size
    ) {

        User user;
        boolean isAuthenticated = !email.equals("anonymousUser");

        if(isAuthenticated) {
            user = findUserByEmail(email, userRepository);
        } else {
            user = null;
        }

        QFunding funding = QFunding.funding;
        BooleanBuilder filterBuilder = createFundingFilter(isClosed, categories, keyword, tags, minRate, maxRate, isAuthenticated, isLiked, user, funding);
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sort, funding);
        Pageable pageable = PageRequest.of(page, size);

        List<Funding> results = queryFactory.selectFrom(funding)
                .leftJoin(funding.tagList).fetchJoin()
                .where(filterBuilder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(funding)
                .where(filterBuilder)
                .fetch()
                .size();

        Page<Funding> fundingList = new PageImpl<>(results, pageable, total);

        List<FundingDataDto> data = fundingList.stream()
                .map(f -> FundingDataDto.of(f, fundingLikeRepository, true, user))
                .collect(Collectors.toList());

        return GetFundingListResponseDto.success(fundingList, data);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<GetFundingStateCountResponseDto> getFundingStateCount(String email) {

        User user = findUserByEmail(email, userRepository);
        checkAdminAccess(user);

        QFunding funding = QFunding.funding;

        long review = getFundingCount(State.REVIEW, funding);
        long reviewCompleted = getFundingCount(State.REVIEW_COMPLETED, funding);
        long onGoing = getFundingCount(State.ONGOING, funding);
        FundingStateDto data = FundingStateDto.of(review, reviewCompleted, onGoing);

        return GetFundingStateCountResponseDto.success(data);
    }

    private long getFundingCount(State state, QFunding funding) {
        Long count = queryFactory
                .select(funding.count())
                .from(funding)
                .where(funding.current.eq(state))
                .fetchOne();

        return (count != null) ? count : 0L;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<GetFundingByStateResponseDto> getFundingByState(String email, String state, String keyword, int page, int size) {

        User user = findUserByEmail(email, userRepository);
        checkAdminAccess(user);

        QFunding funding = QFunding.funding;
        BooleanBuilder filterBuilder = new BooleanBuilder();
        Pageable pageable = PageRequest.of(page, size);

        if(keyword != null) {
            filterBuilder.and(funding.title.containsIgnoreCase(keyword));
        }

        filterBuilder.and(funding.current.eq(State.valueOf(state)));

        List<Funding> results = queryFactory.selectFrom(funding)
                .where(filterBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(funding)
                .where(filterBuilder)
                .fetch()
                .size();

        Page<Funding> fundingList = new PageImpl<>(results, pageable, total);
        List<ShortFundingDataDto> data = fundingList.stream()
                .map(ShortFundingDataDto::of)
                .collect(Collectors.toList());

        return GetFundingByStateResponseDto.success(fundingList, data);
    }

    @Transactional
    public ResponseEntity<ResponseDto> changeFundingState(String email, Long fundingId, String state) {

        User user = findUserByEmail(email, userRepository);
        checkAdminAccess(user);

        Funding funding = findFundingById(fundingId, fundingRepository);

        State newState = State.valueOf(state);
        funding.setCurrent(newState);
        fundingRepository.save(funding);

        if(newState == State.REVIEW_COMPLETED) {
            sendFundingReviewCompletedNotification(funding);
        }

        return ResponseDto.success();
    }

    private BooleanBuilder createFundingFilter(
            Boolean isClosed, List<String> categories, String keyword, List<String> tags, int minRate, int maxRate,
            Boolean isAuthenticated, Boolean isLiked, User user, QFunding funding
    ) {
        BooleanBuilder filterBuilder = new BooleanBuilder();

        if (isClosed) {
            filterBuilder.and(funding.current.eq(State.ONGOING).or(funding.current.eq(State.CLOSED)));
        } else {
            filterBuilder.and(funding.current.eq(State.ONGOING));
        }

        addCategoryFilter(categories, funding, filterBuilder);
        addKeywordFilter(keyword, funding, filterBuilder);
        addTagFilter(tags, funding, filterBuilder);
        addRateFilter(minRate, maxRate, funding, filterBuilder);

        if (isAuthenticated && !isLiked) {
            filterBuilder.and(funding.funding_id.notIn(
                    JPAExpressions.select(QFundingLike.fundingLike.funding.funding_id)
                            .from(QFundingLike.fundingLike)
                            .where(QFundingLike.fundingLike.user.eq(user))
            ));
        }

        return filterBuilder;
    }

    private void addCategoryFilter(List<String> categories, QFunding funding, BooleanBuilder filterBuilder) {
        if(categories != null) {
            BooleanBuilder categoryFilter = new BooleanBuilder();
            for(String category : categories) {
                categoryFilter.or(funding.category.eq(Category.valueOf(category)));
            }
            filterBuilder.and(categoryFilter);
        }
    }

    private void addTagFilter(List<String> tags, QFunding funding, BooleanBuilder filterBuilder) {
        if (tags != null && !tags.isEmpty()) {
            BooleanBuilder tagFilter = new BooleanBuilder();
            for (String tag : tags) {
                tagFilter.or(funding.tagList.any().tag_name.eq(tag));
            }
            filterBuilder.and(tagFilter);
        }
    }

    private void addKeywordFilter(String keyword, QFunding funding, BooleanBuilder filterBuilder) {
        if (keyword != null) {
            filterBuilder.and(
                    funding.title.containsIgnoreCase(keyword)
                            .or(funding.projectSummary.containsIgnoreCase(keyword))
            );
        }
    }

    private void addRateFilter(int minRate, int maxRate, QFunding funding, BooleanBuilder filterBuilder) {
        if(minRate > 0) {
            filterBuilder.and(
                    Expressions.numberTemplate(Integer.class, "CASE WHEN {1} > 0 THEN (CAST({0} AS double) * 1.0 / {1}) * 100 ELSE 0 END",
                                    funding.currentAmount, funding.targetAmount)
                            .goe(minRate)
            );
        }

        if(maxRate < 100) {
            filterBuilder.and(
                    Expressions.numberTemplate(Integer.class, "CASE WHEN {1} > 0 THEN (CAST({0} AS double) * 1.0 / {1}) * 100 ELSE 0 END",
                                    funding.currentAmount, funding.targetAmount)
                            .loe(maxRate)
            );
        }
    }

    private OrderSpecifier<?> getOrderSpecifier(String sort, QFunding funding) {
        return switch (sort) {
            case "oldest" -> funding.createdAt.asc();
            case "priceAsc" -> funding.rewardAmount.asc();
            case "priceDesc" -> funding.rewardAmount.desc();
            case "likes" -> funding.totalLikes.desc();
            case "achievementRateAsc" ->
                    Expressions.numberTemplate(Double.class, "CASE WHEN {0} > 0 THEN {0} / {1} ELSE 0 END",
                            funding.currentAmount, funding.targetAmount).asc();
            case "achievementRateDesc" ->
                    Expressions.numberTemplate(Double.class, "CASE WHEN {0} > 0 THEN {0} / {1} ELSE 0 END",
                            funding.currentAmount, funding.targetAmount).desc();
            case "deadlineDesc" -> funding.endDate.desc();
            default -> funding.createdAt.desc();
        };
    }

    private void sendFundingReviewCompletedNotification(Funding funding) {
        Notification notification = new Notification(funding.getUser(), LocalDateTime.now().format(formatter));
        notification.setFundingReviewCompletedMessage(funding.getTitle());
        notificationRepository.save(notification);
    }
}
