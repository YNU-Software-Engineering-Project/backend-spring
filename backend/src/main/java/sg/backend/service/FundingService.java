package sg.backend.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
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
import sg.backend.dto.object.FundingDataDto;
import sg.backend.dto.object.ShortFundingDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.GetFundingByStateResponseDto;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetFundingStateCountResponseDto;
import sg.backend.dto.response.funding.GetMyFundingListResponseDto;
import sg.backend.entity.*;
import sg.backend.repository.FundingLikeRepository;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.NotificationRepository;
import sg.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static sg.backend.service.UserService.formatter;

@Service
@RequiredArgsConstructor
public class FundingService {

    private final UserRepository userRepository;
    private final FundingRepository fundingRepository;
    private final FundingLikeRepository fundingLikeRepository;
    private final NotificationRepository notificationRepository;
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public ResponseEntity<? super GetFundingListResponseDto> searchFunding(String email, String keyword, String sort, String category, List<String> tags, Long minAmount, Boolean isClosed, Boolean isLiked, int page, int size) {

        User user = null;
        boolean isAuthenticated = false;
        QFunding funding = QFunding.funding;
        QFundingLike fundingLike = QFundingLike.fundingLike;
        BooleanBuilder filterBuilder = new BooleanBuilder();
        Page<Funding> fundingList;
        List<FundingDataDto> data = new ArrayList<>();

        try {
            if(!email.equals("anonymousUser")) isAuthenticated = true;
            if(isAuthenticated) {
                Optional<User> optionalUser = userRepository.findByEmail(email);
                if(optionalUser.isEmpty()) return GetMyFundingListResponseDto.noExistUser();
                user = optionalUser.get();
            }

            if (isClosed) {
                filterBuilder.and(funding.current.eq(State.ONGOING).or(funding.current.eq(State.CLOSED)));
            } else {
                filterBuilder.and(funding.current.eq(State.ONGOING));
            }

            if(category != null) {
                addCategoryFilter(category, funding, filterBuilder);
            }

            if(keyword != null) {
                addKeywordFilter(keyword, funding, filterBuilder);
            }

            if(tags != null && !tags.isEmpty()) {
                addTagFilter(tags, funding, filterBuilder);
            }

            if (minAmount != null) {
                filterBuilder.and(funding.currentAmount.goe(minAmount));
            }

            if (isAuthenticated) {
                if (!isLiked) {
                    filterBuilder.and(funding.fundingId.notIn(
                            JPAExpressions.select(fundingLike.funding.fundingId)
                                    .from(fundingLike)
                                    .where(fundingLike.user.eq(user))
                    ));
                }
            }

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

            fundingList = new PageImpl<>(results, pageable, total);
            for(Funding f : fundingList) {
                data.add(convertToDto(f, fundingLikeRepository, isAuthenticated, user));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetFundingListResponseDto.success(fundingList, data);
    }

    private void addCategoryFilter(String category, QFunding funding, BooleanBuilder filterBuilder) {
        filterBuilder.and(funding.category.eq(Category.valueOf(category)));
    }

    private void addTagFilter(List<String> tags, QFunding funding, BooleanBuilder filterBuilder) {
        if (tags != null && !tags.isEmpty()) {
            BooleanBuilder tagFilter = new BooleanBuilder();
            for (String tag : tags) {
                tagFilter.or(funding.tagList.any().tagName.eq(tag));
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

    private OrderSpecifier<?> getOrderSpecifier(String sort, QFunding funding) {
        switch (sort) {
            case "latest":
                return funding.createdAt.desc();
            case "oldest":
                return funding.createdAt.asc();
            case "priceAsc":
                return funding.rewardAmount.asc();
            case "priceDesc":
                return funding.rewardAmount.desc();
            case "likes":
                return funding.totalLikes.desc();
            default:
                return funding.createdAt.desc();
        }
    }

     public static FundingDataDto convertToDto(Funding funding, FundingLikeRepository fundingLikeRepository, boolean isAuthenticated, User user) {
        FundingDataDto dto = new FundingDataDto();
        dto.setTitle(funding.getTitle());
        dto.setMainImage(funding.getMainImage());
        dto.setProjectSummary(funding.getProjectSummary());
        dto.setPrice(funding.getRewardAmount());
        dto.setCategory(String.valueOf(funding.getCategory()));

        List<String> tags = funding.getTagList().stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());
        dto.setTag(tags);

        int achievementRate = funding.getCurrentAmount() == 0 ? 0 :
                (int) (((double) funding.getCurrentAmount() / funding.getTargetAmount()) * 100);
        dto.setAchievementRate(achievementRate);

        dto.setLiked(isAuthenticated && fundingLikeRepository.existsByUserAndFunding(user, funding));
        dto.setState(String.valueOf(funding.getCurrent()));

        return dto;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<? super GetFundingStateCountResponseDto> getFundingStateCount(String email) {

        User user;
        QFunding funding = QFunding.funding;
        long review;
        long reviewCompleted;
        long onGoing;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) return ResponseDto.noExistUser();
            user = optionalUser.get();

            if(!user.getRole().equals(Role.ADMIN))
                return ResponseDto.noPermission();

            review = getFundingCount(State.REVIEW, funding);
            reviewCompleted = getFundingCount(State.REVIEW_COMPLETED, funding);
            onGoing = getFundingCount(State.ONGOING, funding);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetFundingStateCountResponseDto.success(review, reviewCompleted, onGoing);
    }

    private long getFundingCount(State state, QFunding funding) {
        return queryFactory
                .select(funding.count())
                .from(funding)
                .where(funding.current.eq(state))
                .fetchOne();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<? super GetFundingByStateResponseDto> getFundingByState(String email, String state, String keyword, int page, int size) {

        User user;
        QFunding funding = QFunding.funding;
        BooleanBuilder filterBuilder = new BooleanBuilder();
        Page<Funding> fundingList;
        List<ShortFundingDataDto> data = new ArrayList<>();

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) return ResponseDto.noExistUser();
            user = optionalUser.get();

            if(!user.getRole().equals(Role.ADMIN))
                return ResponseDto.noPermission();

            if(keyword != null) {
                filterBuilder.and(funding.title.containsIgnoreCase(keyword));
            }

            filterBuilder.and(funding.current.eq(State.valueOf(state)));

            Pageable pageable = PageRequest.of(page, size);

            List<Funding> results = queryFactory.selectFrom(funding)
                    .where(filterBuilder)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            long total = queryFactory.selectFrom(funding)
                    .where(filterBuilder)
                    .fetch()
                    .size();

            fundingList = new PageImpl<>(results, pageable, total);
            for(Funding f : fundingList) {
                ShortFundingDataDto dto = new ShortFundingDataDto();
                dto.setTitle(f.getTitle());
                dto.setMainImage(f.getMainImage());
                dto.setState(String.valueOf(f.getCurrent()));
                data.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetFundingByStateResponseDto.success(fundingList, data);
    }

    @Transactional
    public ResponseEntity<? super ResponseDto> changeFundingState(String email, Long fundingId, String state) {

        User user;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) return ResponseDto.noExistUser();
            user = optionalUser.get();

            if(!user.getRole().equals(Role.ADMIN))
                return ResponseDto.noPermission();

            Optional<Funding> optionalFunding = fundingRepository.findByFundingId(fundingId);
            Funding funding;
            if(optionalFunding.isPresent())
                funding = optionalFunding.get();
            else return ResponseDto.noExistFunding();

            funding.setCurrent(State.valueOf(state));
            fundingRepository.save(funding);

            if(state.equals("REVIEW_COMPLETED")) {
                Notification notification = new Notification(funding.getUser(), LocalDateTime.now().format(formatter));
                notification.setFundingReviewCompletedMessage(funding.getTitle());
                notificationRepository.save(notification);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }
}
