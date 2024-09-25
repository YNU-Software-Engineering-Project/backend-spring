package sg.backend.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.backend.entity.Funding;
import sg.backend.entity.Notification;
import sg.backend.entity.QFunding;
import sg.backend.entity.State;
import sg.backend.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

import static sg.backend.service.UserService.formatter;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final NotificationRepository notificationRepository;
    private final JPAQueryFactory queryFactory;

    // 펀딩 시작 날짜 -> 진행 중으로 상태 변경
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
        }
    }

    // 펀딩 마감 날짜 -> 펀딩 성공(리워드 배송 안내) or 실패(환불) 알림 메시지
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void checkClosedFunding() {
        QFunding funding = QFunding.funding;

        LocalDateTime now = LocalDateTime.now();

        List<Funding> fundingList = queryFactory.selectFrom(funding)
                .where(funding.endDate.before(now)
                        .and(funding.current.eq(State.ONGOING)))
                .fetch();

        for(Funding f : fundingList) {
            if(f.getCurrentAmount() >= f.getTargetAmount()) {
                Notification notification = new Notification(f.getUser(), LocalDateTime.now().format(formatter));
                notification.setRewardShippedMessage(f.getTitle());
                notificationRepository.save(notification);
            } else {
                Notification notification = new Notification(f.getUser(), LocalDateTime.now().format(formatter));
                notification.setFundingFailureMessage(f.getTitle());
                notificationRepository.save(notification);
            }
            f.setCurrent(State.CLOSED);
        }
    }
}
