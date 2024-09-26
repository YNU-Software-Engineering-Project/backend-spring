package sg.backend.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.backend.entity.*;
import sg.backend.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

import static sg.backend.service.UserService.formatter;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final NotificationRepository notificationRepository;
    private final JPAQueryFactory queryFactory;

    // 펀딩 시작 날짜 -> 프로젝트 시작 알림 & 진행 중으로 상태 변경
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void checkStartedFunding() {
        QFunding funding = QFunding.funding;

        LocalDateTime now = LocalDateTime.now();

        List<Funding> fundingList = queryFactory.selectFrom(funding)
                .where(funding.startDate.after(now)
                        .and(funding.current.eq(State.REVIEW_COMPLETED)))
                .fetch();

        for(Funding f : fundingList) {
            f.setCurrent(State.ONGOING);
            Notification notification = new Notification(f.getUser(), LocalDateTime.now().format(formatter));
            notification.setFundingStartMessage(f.getTitle());
            notificationRepository.save(notification);
        }
    }

    // 펀딩 마감 날짜 -> 프로젝트 종료 알림 & 후원자에게 리워드 배송 or 환불 알림 메시지
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void checkClosedFunding() {
        QFunding funding = QFunding.funding;
        QFunder funder = QFunder.funder;

        LocalDateTime now = LocalDateTime.now();

        List<Funding> fundingList = queryFactory.selectFrom(funding)
                .where(funding.endDate.before(now)
                        .and(funding.current.eq(State.ONGOING)))
                .fetch();

        for(Funding f : fundingList) {
            Notification notification = new Notification(f.getUser(), LocalDateTime.now().format(formatter));
            notification.setFundingCloseMessage(f.getTitle());
            notificationRepository.save(notification);

            List<Funder> funders = queryFactory.selectFrom(funder)
                    .where(funder.funding.eq(f))
                    .fetch();

            for (Funder fd : funders) {
                User user = fd.getUser();

                if (f.getCurrentAmount() >= f.getTargetAmount()) {
                    notification = new Notification(user, LocalDateTime.now().format(formatter));
                    notification.setRewardShippedMessage(f.getTitle());
                } else {
                    notification = new Notification(user, LocalDateTime.now().format(formatter));
                    notification.setFundingFailureMessage(f.getTitle());
                }
                notificationRepository.save(notification);
            }

            f.setCurrent(State.CLOSED);
        }
    }

}
