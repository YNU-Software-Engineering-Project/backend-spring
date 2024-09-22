package sg.backend.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
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
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.entity.*;
import sg.backend.repository.FundingLikeRepository;
import sg.backend.repository.FundingTagRepository;
import sg.backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FundingService {

    private final UserRepository userRepository;
    private final FundingTagRepository fundingTagRepository;
    private final FundingLikeRepository fundingLikeRepository;
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public ResponseEntity<? super GetFundingListResponseDto> searchFunding(String email, String keyword, String sort, String category, List<String> tags, Long minAmount, Boolean isClosed, Boolean isLiked, int page, int size) {

        User user = null;
        QFunding funding = QFunding.funding;
        QReward reward = QReward.reward;
        QFundingTag fundingTag = QFundingTag.fundingTag;
        QFundingLike fundingLike = QFundingLike.fundingLike;
        BooleanBuilder builder = new BooleanBuilder();
        Page<Funding> fundingList;
        List<FundingDataDto> data = new ArrayList<>();
        boolean isAuthenticated = false;

        try {
            if(email != null) isAuthenticated = true;
            if(isAuthenticated) {
                Optional<User> optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isEmpty()) return GetFundingListResponseDto.noExistUser();
                user = optionalUser.get();
            }

            if (keyword != null && !keyword.isEmpty()) {
                builder.and(
                        funding.title.containsIgnoreCase(keyword)
                                .or(funding.projectSummary.containsIgnoreCase(keyword))
                );
            }

            if (category != null && !category.isEmpty()) {
                List<String> categories = new ArrayList<>();
                if(category.contains("A")) {
                    addCategory(categories, category);
                }
                categories.add(category);
                for(String c : categories)
                    builder.and(funding.category.eq(Category.valueOf(c)));
            }

            if (minAmount != null) {
                builder.and(funding.currentAmount.goe(minAmount));
            }

            if (tags != null && !tags.isEmpty()) {
                builder.and(fundingTag.tag.tagName.in(tags));
            }

            if (isLiked) {
                if(isAuthenticated)
                    builder.and(fundingLike.user.eq(user));
            } else {
                if(isAuthenticated)
                    builder.and(fundingLike.user.ne(user));
            }

            if (isClosed) {
                builder.and(funding.current.eq(State.CLOSED));
            } else {
                builder.and(funding.current.ne(State.CLOSED));
            }

            OrderSpecifier<?> orderSpecifier = funding.createdAt.desc();
            switch (sort) {
                case "oldest":
                    orderSpecifier = funding.createdAt.asc();
                    break;
                case "priceAsc":
                    orderSpecifier = reward.amount.asc();
                    break;
                case "priceDesc":
                    orderSpecifier = reward.amount.desc();
                    break;
                case "achievementRate":
                    orderSpecifier = Expressions.numberTemplate(Integer.class,
                            "CAST(({0} * 100) / {1} AS INTEGER)", funding.currentAmount, funding.targetAmount).desc();
                    break;
                case "achievementRateAsc":
                    orderSpecifier = Expressions.numberTemplate(Integer.class,
                            "CAST(({0} * 100) / {1} AS INTEGER)", funding.currentAmount, funding.targetAmount).asc();
                    break;
                case "likes":
                    orderSpecifier = funding.totalLikes.desc();
                    break;
                default:
                    break;
            }

            Pageable pageable = PageRequest.of(page, size);

            List<Funding> results = queryFactory.selectFrom(funding)
                    .leftJoin(fundingTag).on(fundingTag.funding.eq(funding))
                    .leftJoin(fundingLike).on(fundingLike.funding.eq(funding))
                    .leftJoin(funding.rewardList, reward)
                    .where(builder)
                    .orderBy(orderSpecifier)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            long total = queryFactory.selectFrom(funding)
                    .leftJoin(fundingTag).on(fundingTag.funding.eq(funding))
                    .leftJoin(fundingLike).on(fundingLike.funding.eq(funding))
                    .where(builder)
                    .fetchCount();

            fundingList = new PageImpl<>(results, pageable, total);

            for (Funding f : fundingList) {
                FundingDataDto dto = new FundingDataDto();
                dto.setTitle(f.getTitle());
                dto.setMainImage(f.getMainImage());
                dto.setProjectSummary(f.getProjectSummary());
                dto.setCategory(String.valueOf(f.getCategory()));

                List<Tag> tagList = fundingTagRepository.findTagByFundingId(f.getFundingId());
                List<String> tag = new ArrayList<>();
                for (Tag t : tagList) {
                    tag.add(t.getTagName());
                }
                dto.setTag(tag);

                int targetAmount = f.getTargetAmount();
                int currentAmount = f.getCurrentAmount();
                int achievementRate;
                if (currentAmount == 0) achievementRate = 0;
                else achievementRate = (int) (((double) currentAmount / targetAmount) * 100);
                dto.setAchievementRate(achievementRate);

                if(isAuthenticated) {
                    boolean isLike = fundingLikeRepository.existsByUserAndFunding(user, f);
                    dto.setLike(isLike);
                } else {
                    dto.setLike(false);
                }

                dto.setState(String.valueOf(f.getCurrent()));
                data.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetFundingListResponseDto.success(fundingList, data);
    }

    public void addCategory(List<String> c, String category) {
        switch (category) {
            case "A0010" :
                for(int i=10; i<=60; i+=10) {
                    c.add("B00" + i);
                }
                break;
            case "A0020":
                for(int i=70; i<=90; i+=10) {
                    c.add("B00" + i);
                }
                for(int i=100; i<=140; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0030":
                for(int i=150; i<=170; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0040":
                for(int i=180; i<=190; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0050":
                for(int i=200; i<=250; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0060":
                for(int i=260; i<=340; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0070":
                for(int i=350; i<=390; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0080":
                for(int i=400; i<=420; i+=10) {
                    c.add("B0" + i);
                }
                c.add("B0400"); c.add("B0410"); c.add("B0420");
                break;
            case "A0090":
                for(int i=430; i<=520; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0100":
                for(int i=530; i<=610; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0110":
                for(int i=620; i<=670; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0120":
                for(int i=680; i<=760; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0130":
                for(int i=770; i<=810; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0140":
                for(int i=820; i<=840; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0150":
                for(int i=850; i<=900; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0160":
                for(int i=910; i<=940; i+=10) {
                    c.add("B0" + i);
                }
                break;
            case "A0170":
                for(int i=950; i<=990; i+=10) {
                    c.add("B0" + i);
                }
                for(int i=1000; i<=1040; i+=10) {
                    c.add("B" + i);
                }
                break;
            default:
                break;
        }
    }
}
