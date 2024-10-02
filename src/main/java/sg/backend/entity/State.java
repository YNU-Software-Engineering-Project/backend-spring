package sg.backend.entity;

import lombok.Getter;

@Getter
public enum State {
    DRAFT("작성중"),
    REVIEW("심사중"),
    REVIEW_COMPLETED("심사완료"),
    ONGOING("펀딩진행중"),
    CLOSED("펀딩종료");

    private final String message;

    State(String message) {
        this.message = message;
    }
}
