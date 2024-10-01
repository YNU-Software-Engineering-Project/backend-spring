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

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final NotificationRepository notificationRepository;
    private final JPAQueryFactory queryFactory;

    // 펀딩 시작 날짜 -> 프로젝트 시작 알림 & 진행 중으로 상태 변경
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void checkStartedFunding() {
        LocalDateTime now = LocalDateTime.now();
        QFunding funding = QFunding.funding;

        List<Funding> fundingList = queryFactory.selectFrom(funding)
                .where(funding.startDate.after(now)
                        .and(funding.current.eq(State.REVIEW_COMPLETED)))
                .fetch();

        for(Funding f : fundingList) {
            f.setCurrent(State.ONGOING);
            sendNotification(f.getUser(), f.getTitle(), NotificationType.START);
        }
    }

    // 펀딩 마감 날짜 -> 프로젝트 종료 알림 & 후원자에게 리워드 배송 or 환불 알림 메시지
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void checkClosedFunding() {
        LocalDateTime now = LocalDateTime.now();
        QFunding funding = QFunding.funding;
        QFunder funder = QFunder.funder;

        List<Funding> fundingList = queryFactory.selectFrom(funding)
                .where(funding.endDate.before(now)
                        .and(funding.current.eq(State.ONGOING)))
                .fetch();

        for(Funding f : fundingList) {
            sendNotification(f.getUser(), f.getTitle(), NotificationType.CLOSE);
            f.setCurrent(State.CLOSED);

            List<Funder> funders = queryFactory.selectFrom(funder)
                    .where(funder.funding.eq(f))
                    .fetch();

            for (Funder fd : funders) {
                User user = fd.getUser();
                NotificationType notificationType = (f.getCurrentAmount() >= f.getTargetAmount())
                        ? NotificationType.REWARD_SHIPPED
                        : NotificationType.FUNDING_FAILURE;
                sendNotification(user, f.getTitle(), notificationType);
            }
        }
    }

    private void sendNotification(User user, String fundingTitle, NotificationType type) {
        Notification notification = new Notification(user, LocalDateTime.now().format(UserService.formatter));

        switch (type) {
            case START -> notification.setFundingStartMessage(fundingTitle);
            case CLOSE -> notification.setFundingCloseMessage(fundingTitle);
            case REWARD_SHIPPED -> notification.setRewardShippedMessage(fundingTitle);
            case FUNDING_FAILURE -> notification.setFundingFailureMessage(fundingTitle);
        }

        notificationRepository.save(notification);
    }

    private enum NotificationType {
        START,
        CLOSE,
        REWARD_SHIPPED,
        FUNDING_FAILURE
    }

}
