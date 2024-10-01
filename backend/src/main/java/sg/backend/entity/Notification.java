package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String message;

    private String createdAt;

    public Notification(User user, String createdAt) {
        this.user = user;
        this.createdAt = createdAt;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // 회원가입 시 알림 메시지
    public void setStartMessage() {
        setMessage("새로운 프로젝트를 시작해보세요.");
    }

    // 목표 금액 달성 알림 메시지
    public void setFundingSuccessMessage(String fundingName) {
        setMessage(String.format("축하합니다! [%s] 프로젝트가 목표 금액을 달성했습니다.", fundingName));
    }

    // 리워드 발송 알림 메시지
    public void setRewardShippedMessage(String fundingName) {
        setMessage(String.format("후원하신 [%s] 프로젝트가 종료되었으며, 리워드가 곧 발송될 예정입니다.", fundingName));
    }

    // 후원금 환불 알림 메시지
    public void setFundingFailureMessage(String fundingName) {
        setMessage(String.format("후원하신 [%s] 프로젝트가 목표 금액을 달성하지 못하여 후원 금액이 환불되었습니다.", fundingName));
    }

    // 댓글 알림 메시지
    public void setQuestionMessage(String name, String fundingName) {
        setMessage(String.format("'%s'님이 [%s]에 질문을 남겼습니다.", name, fundingName));
    }

    // 댓글 답글 알림 메시지
    public void setCommentMessage(String fundingName) {
        setMessage(String.format("[%s]에 답글이 달렸습니다.", fundingName));
    }

    // 후원한 펀딩 게시물 업데이트 알림 메시지
    public void setFundingUpdateMessage(String fundingName) {
        setMessage(String.format("[%s]에 새로운 업데이트가 있습니다.", fundingName));
    }

    // 펀딩 게시물 승인 알림 메시지
    public void setFundingReviewCompletedMessage(String fundingName) {
        setMessage(String.format("[%s] 프로젝트가 승인되었습니다.", fundingName));
    }

    // 프로젝트 시작 알림 메시지
    public void setFundingStartMessage(String fundingName) {
        setMessage(String.format("[%s] 프로젝트가 시작됩니다.", fundingName));
    }

    // 프로젝트 종료 알림 메시지
    public void setFundingCloseMessage(String fundingName) {
        setMessage(String.format("[%s] 프로젝트가 종료되었습니다.", fundingName));
    }

    // 새로운 채팅 도착 알림 메시지
    public void setNewMessageFromUserNotification(String userName) {
        setMessage(String.format("%s 님으로부터 새로운 채팅이 도착했습니다.", userName));
    }

    // 계정 관련 알림 메시지
    public void setAccountMessage(Role role) {
        if(role == Role.USER) {
            setMessage("회원님의 계정이 정상적으로 복구되었습니다. 다시 서비스를 이용하실 수 있습니다.");
        } else if(role == Role.SUSPENDED) {
            setMessage("회원님의 계정이 정지되었습니다. 자세한 내용은 관리자에게 문의해 주세요.");
        }
    }
}
