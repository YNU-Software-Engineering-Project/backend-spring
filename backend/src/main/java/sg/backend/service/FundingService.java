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
import sg.backend.common.CategoryUtil;
import sg.backend.dto.object.FundingDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetMyFundingListResponseDto;
import sg.backend.entity.*;
import sg.backend.repository.FundingLikeRepository;
import sg.backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FundingService {

    private final UserRepository userRepository;
    private final FundingLikeRepository fundingLikeRepository;
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
                List<String> categories = new ArrayList<>();
                if(Category.valueOf(category).toString().contains("A"))
                    categories = CategoryUtil.valueOf(category).getCategories();
                else
                    categories.add(category);
                addCategoryFilter(categories, funding, filterBuilder);
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

    private void addCategoryFilter(List<String> categories, QFunding funding, BooleanBuilder filterBuilder) {
        if (!categories.isEmpty()) {
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

}
