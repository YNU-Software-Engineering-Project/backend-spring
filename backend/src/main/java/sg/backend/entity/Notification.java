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

    // 회원가입 시 알림 메시지
    public void setStartMessage() {
        this.message = "새로운 프로젝트를 시작해보세요.";
    }

    // 후원한 펀딩의 목표 금액 달성 알림 메시지
    public void setFundingSuccessMessage(String fundingName) {
        this.message = String.format("축하합니다! 후원하신 [%s]가 목표 금액을 달성했습니다.", fundingName);
    }

    // 리워드 발송 알림 메시지
    public void setRewardShippedMessage(String fundingName) {
        this.message = String.format("후원하신 [%s]가 종료되었으며, 리워드가 곧 발송될 예정입니다.", fundingName);
    }

    // 후원금 환불 알림 메시지
    public void setFundingFailureMessage(String fundingName) {
        this.message = String.format("후원하신 [%s]가 목표 금액을 달성하지 못하여 후원 금액이 환불되었습니다.", fundingName);
    }

    // 댓글 알림 메시지
    public void setCommentMessage(String fundingName) {
        this.message = String.format("[%s]에 댓글이 달렸습니다.", fundingName);
    }

    // 댓글 답글 알림 메시지
    public void setCommentReplyMessage(String fundingName) {
        this.message = String.format("[%s]에 남긴 댓글에 답글이 달렸습니다.", fundingName);
    }

    // 후원한 펀딩 게시물 업데이트 알림 메시지
    public void setFundingUpdateMessage(String fundingName) {
        this.message = String.format("[%s]에 새로운 업데이트가 있습니다.", fundingName);
    }

    // 펀딩 게시물 상태 변경 알림 메시지
    public void setFundingStateUpdateMessage(String fundingName, State state) {
        switch (state) {
            case DRAFT:
                this.message = String.format("[%s] 게시물이 '작성 중' 상태로 변경되었습니다.", fundingName);
                break;
            case REVIEW:
                this.message = String.format("[%s] 게시물이 '심사 대기' 상태로 변경되었습니다.", fundingName);
                break;
            case REVIEW_COMPLETED:
                this.message =String.format("[%s] 프로젝트가 승인되었습니다.", fundingName);
                break;
        }

    }

    // 새로운 채팅 도착 알림 메시지
    public String getNewMessageFromUserNotification(String userName) {
        return String.format("%s 님으로부터 새로운 채팅이 도착했습니다.", userName);
    }

    // 계정 정지 알림 메시지
    public void getAccountSuspensionMessage() {
        this.message = String.format("회원님의 계정이 정지되었습니다. 자세한 내용은 관리자에게 문의해 주세요.");
    }

    // 계정 정지 해제 알림 메시지
    public String getAccountRestorationMessage() {
        return "회원님의 계정이 정상적으로 복구되었습니다. 다시 서비스를 이용하실 수 있습니다.";
    }
}
