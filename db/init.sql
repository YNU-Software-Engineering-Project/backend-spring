CREATE DATABASE IF NOT EXISTS testdb;

USE testdb;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    nickname VARCHAR(255) ,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER', 'SUSPENDED') DEFAULT 'USER',
    phone_number VARCHAR(20) NOT NULL,
    school_email VARCHAR(255),
    school_email_verified BOOLEAN DEFAULT FALSE,
    postal_code VARCHAR(10),
    road_address VARCHAR(255),
    land_lot_address VARCHAR(255),
    detail_address VARCHAR(255),
    profile_image VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS funding (
    funding_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    current ENUM('DRAFT', 'REVIEW', 'REVIEW_COMPLETED', 'ONGOING', 'CLOSED') NOT NULL,
    category VARCHAR(255),
    organizer_name VARCHAR(255),
    organizer_email VARCHAR(255),
    tax_email VARCHAR(255),
    organizer_id_card VARCHAR(255),
    start_date DATETIME DEFAULT NULL,
    end_date DATETIME DEFAULT NULL,
    target_amount INT DEFAULT NULL,
    title VARCHAR(255),
    main_image VARCHAR(255),
    project_summary TEXT,
    reward_info TEXT,
    refund_policy TEXT,
    story Text,
    total_likes INT DEFAULT 0,
    today_likes INT DEFAULT 0,
    total_visitors INT DEFAULT 0,
    today_visitors INT DEFAULT 0,
    today_amount INT DEFAULT 0,
    current_amount INT DEFAULT 0,
    is_target_amount_achieved BOOLEAN DEFAULT FALSE,
    reward_amount INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS notification (
    notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    message VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS tag (
    tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tag_name VARCHAR(255) NOT NULL,
    funding_id BIGINT,
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id)
);

CREATE TABLE IF NOT EXISTS funding_like (
    funding_like_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    funding_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id)
);

CREATE TABLE IF NOT EXISTS approval_document (
    document_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funding_id BIGINT,
    name VARCHAR(255) NOT NULL,
    uuid VARCHAR(255) NOT NULL,
    fpath VARCHAR(255) NOT NULL,
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id)
);

CREATE TABLE IF NOT EXISTS intro_image (
    img_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funding_id BIGINT,
    uuid VARCHAR(255) NOT NULL,
    fpath VARCHAR(255) NOT NULL,
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id)
);

CREATE TABLE IF NOT EXISTS reward (
    reward_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funding_id BIGINT,
    amount INT NOT NULL,
    reward_name VARCHAR(255) NOT NULL,
    reward_description TEXT,
    quantity INT,
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id)
);

CREATE TABLE IF NOT EXISTS funder (
    funder_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funding_id BIGINT,
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS selected_reward (
    sel_reward_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funder_id BIGINT,
    reward_id BIGINT,
    sel_quantity INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (funder_id) REFERENCES funder(funder_id),
    FOREIGN KEY (reward_id) REFERENCES reward(reward_id)
);

CREATE TABLE IF NOT EXISTS community_post (
    community_post_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funding_id BIGINT,
    user_id BIGINT,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS question (
    question_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funding_id BIGINT NOT NULL,
    user_id BIGINT,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS comment (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    user_id BIGINT,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES question(question_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS email_token (
    email_token_id VARCHAR(36) PRIMARY KEY,
    expiration_date TIMESTAMP NOT NULL,
    expired BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    email VARCHAR(255)
);

INSERT INTO users (user_id, email, nickname, password, phone_number)
VALUES
(1, 'test@example.com', 'testuser1', 'Password123!', '010-1234-5678'),
(2, 'test2@example.com', 'testuser2', 'Password123!', '010-1111-2222');

INSERT INTO funding (funding_id, reward_amount, title, project_summary, current_amount, target_amount, current, category, organizer_name, organizer_email, tax_email, organizer_id_card, user_id, created_at, total_likes, reward_info, refund_policy, end_date, story)
VALUES
(1, 100000, '10초 완판 | 차원이 다른 스마트 가습기', '강력한 미세 분사와 자동 습도 조절 기능 탑재', 1500000, 5000000, 'ONGOING', 'A0010', '더워터', 'organizer1@example.com', 'tax1@example.com', '/path/to/id_card1.jpg', 1, '2024-09-21 11:11:11', 25, '가습기 리워드 정보', '환불 정책 안내', '2024-10-31 23:59:59', '스마트 가습기의 우수성에 대한 설명'),
(2, 200000, '여행자를 위한 올인원 멀티툴', '캠핑부터 도심 여행까지, 하나로 충분한 멀티툴', 700000, 1000000, 'ONGOING', 'A0020', '트래블팩', 'organizer2@example.com', 'tax2@example.com', '/path/to/id_card2.jpg', 1, '2024-09-21 11:12:11', 50, '멀티툴 리워드 정보', '환불 정책 안내', '2024-10-31 23:59:59', '멀티툴의 다기능성에 대한 설명'),
(3, 150000, '한정판 | 차세대 친환경 에코백', '100% 재활용 소재로 만들어진 에코백', 5000000, 8000000, 'ONGOING', 'A0030', '에코리브', 'organizer3@example.com', 'tax3@example.com', '/path/to/id_card3.jpg', 1, '2024-09-21 11:13:11', 30, '에코백 리워드 정보', '환불 정책 안내', '2024-10-31 23:59:59', '친환경 소재 에코백의 중요성에 대한 설명'),
(4, 230000, '반려동물을 위한 스마트 피딩기', '자동 급식 및 원격 제어 기능으로 편리한 반려동물 관리', 6000000, 6000000, 'CLOSED', 'A0040', '펫케어', 'organizer4@example.com', 'tax4@example.com', '/path/to/id_card4.jpg', 1, '2024-09-21 11:14:11', 75, '피딩기 리워드 정보', '환불 정책 안내', '2024-10-31 23:59:59', '반려동물 관리의 편리성에 대한 설명'),
(5, 187000, '세상을 바꾸는 휴대용 태양광 충전기', '휴대가 간편한 태양광 충전기, 어디서나 충전 가능', 2000000, 3000000, 'CLOSED', 'A0050', '솔라팩', 'organizer5@example.com', 'tax5@example.com', '/path/to/id_card5.jpg', 1, '2024-09-21 11:15:11', 90, '태양광 충전기 리워드 정보', '환불 정책 안내', '2024-10-31 23:59:59', '태양광 기술의 미래에 대한 설명'),
(6, 268000, '스마트홈을 위한 차세대 AI 스피커', 'AI 기술로 더욱 똑똑해진 스마트 스피커', 10000000, 15000000, 'ONGOING', 'A0060', '홈스마트', 'organizer6@example.com', 'tax6@example.com', '/path/to/id_card6.jpg', 1, '2024-09-21 11:16:11', 10, 'AI 스피커 리워드 정보', '환불 정책 안내', '2024-10-31 23:59:59', 'AI 기술의 스마트홈 적용에 대한 설명'),
(7, 328300, '학생들을 위한 다기능 노트북 스탠드', '높이 조절이 가능한 인체공학적 디자인의 노트북 스탠드', 1000000, 2000000, 'REVIEW', 'A0010', '스탠드컴퍼니', 'organizer7@example.com', 'tax7@example.com', '/path/to/id_card7.jpg', 1, '2024-09-21 11:17:11', 20, '노트북 스탠드 리워드 정보', '환불 정책 안내', '2024-10-31 23:59:59', '노트북 사용의 편리성에 대한 설명'),
(8, 981240, '신개념 휴대용 전동칫솔', '초고속 전동 칫솔로 어디서나 간편한 구강 관리', 1500000, 2500000, 'ONGOING', 'A0020', '클린덴트', 'organizer8@example.com', 'tax8@example.com', '/path/to/id_card8.jpg', 1, '2024-09-21 11:18:11', 15, '전동칫솔 리워드 정보', '환불 정책 안내', '2024-10-31 23:59:59', '전동칫솔의 장점에 대한 설명'),
(9, 124150, '100% 천연 성분의 스킨케어 제품', '피부에 자극 없는 순한 천연 스킨케어', 800000, 2000000, 'ONGOING', 'A0030', '네이처케어', 'organizer9@example.com', 'tax9@example.com', '/path/to/id_card9.jpg', 1, '2024-09-21 11:19:11', 12, '스킨케어 리워드 정보', '환불 정책 안내', '2024-10-31 23:59:59', '천연 성분 스킨케어 제품의 중요성에 대한 설명'),
(10, 359000, '한 번 충전으로 한 달 사용 가능한 무선 청소기', '강력한 흡입력과 긴 사용시간을 자랑하는 무선 청소기', 3000000, 5000000, 'CLOSED', 'A0040', '클린홈', 'organizer10@example.com', 'tax10@example.com', '/path/to/id_card10.jpg', 1, '2024-09-21 11:20:11', 100, '무선 청소기 리워드 정보', '환불 정책 안내', '2024-10-31 23:59:59', '무선 청소기의 편리성에 대한 설명');


INSERT INTO tag (tag_name, funding_id) VALUES ('프리미엄', 1);
INSERT INTO tag (tag_name, funding_id) VALUES ('한정판', 1);
INSERT INTO tag (tag_name, funding_id) VALUES ('스마트', 2);
INSERT INTO tag (tag_name, funding_id) VALUES ('일상', 2);
INSERT INTO tag (tag_name, funding_id) VALUES ('가방', 3);
INSERT INTO tag (tag_name, funding_id) VALUES ('반려동물', 4);
INSERT INTO tag (tag_name, funding_id) VALUES ('충전기', 5);
INSERT INTO tag (tag_name, funding_id) VALUES ('스피커', 6);
INSERT INTO tag (tag_name, funding_id) VALUES ('스탠드', 7);

INSERT INTO notification (message, user_id) VALUES ('새로운 프로젝트를 시작해보세요.', 1);

INSERT INTO funding_like (user_id, funding_id, created_at)
VALUES
(1, 1,'2024-09-22 11:11:12'),
(1, 2, '2024-09-22 11:12:13'),
(1, 3, '2024-09-22 11:13:13'),
(2, 4, '2024-09-22 11:14:13'),
(1, 5, '2024-09-22 11:15:13'),
(1, 6, '2024-09-22 11:16:13');

INSERT INTO funder (user_id, funding_id, created_at)
VALUES
(1, 1, '2024-09-22 11:01:00'),
(1, 2, '2024-09-22 11:02:00'),
(1, 3, '2024-09-22 11:03:00'),
(1, 4, '2024-09-22 11:04:00'),
(1, 5, '2024-09-22 11:05:00'),
(1, 6, '2024-09-22 11:09:00'),
(1, 7, '2024-09-22 11:10:00'),
(1, 8, '2024-09-22 11:11:00'),
(1, 9, '2024-09-22 11:12:00'),
(1, 10, '2024-09-22 11:14:00');


INSERT INTO question (funding_id, user_id, content, created_at)
VALUES
(1, 1, '이 제품의 예상 배송일은 언제인가요?', '2024-09-22 11:01:00'),
(1, 1, '펀딩 금액은 어떤 방식으로 사용되나요?', '2024-09-22 11:03:00'),
(1, 1, '제품의 A/S는 어떻게 받을 수 있나요?', '2024-09-22 11:05:00'),
(1, 1, '배송비는 별도로 부과되나요?', '2024-09-22 11:09:00'),
(1, 1, '제품 색상은 선택할 수 있나요?', '2024-09-22 11:02:00'),
(1, 1, '첫번째 리워드의 특징이 무엇인가요?', '2024-09-22 11:06:00');

INSERT INTO comment (question_id, user_id, content, created_at)
VALUES
(1, 1, '제품은 2024년 10월 중순에 발송될 예정입니다.', '2024-09-22 12:01:00'),
(1, 1, '배송은 예상보다 빠를 수 있지만, 지연될 가능성도 있습니다.', '2024-09-22 12:03:00'),
(1, 1, '펀딩 금액은 주로 생산비와 품질 관리를 위해 사용됩니다.', '2024-09-22 12:05:00'),
(1, 1, '일부 금액은 마케팅과 배송비로도 쓰입니다.', '2024-09-22 12:06:00'),
(1, 1, 'A/S는 제품 구매 후 1년간 무상으로 제공됩니다.', '2024-09-22 12:08:00'),
(1, 1, 'A/S 신청은 고객센터로 연락 주시면 됩니다.', '2024-09-22 12:10:00'),
(1, 1, '네, 배송비는 결제 시 별도로 추가됩니다.', '2024-09-22 12:12:00'),
(1, 1, '지역에 따라 배송비가 상이할 수 있습니다.', '2024-09-22 12:15:00'),
(1, 1, '네, 결제 시 색상 옵션을 선택할 수 있습니다.', '2024-09-22 12:17:00'),
(1, 1, '선택 가능한 색상은 총 3가지입니다: 블랙, 화이트, 블루.', '2024-09-22 12:20:00');

INSERT INTO reward (funding_id, amount, reward_name)
VALUES
(1, 10000, '미니 가습기'),
(1, 30000, '스마트 가습기'),
(2, 15000, '멀티툴 기본형'),
(2, 35000, '멀티툴 프리미엄'),
(3, 20000, '친환경 에코백 소형'),
(3, 40000, '친환경 에코백 대형'),
(4, 25000, '스마트 피딩기 기본형'),
(4, 50000, '스마트 피딩기 프리미엄'),
(5, 18000, '휴대용 태양광 충전기 소형'),
(5, 36000, '휴대용 태양광 충전기 대형'),
(6, 40000, '차세대 AI 스피커 기본형'),
(6, 60000, '차세대 AI 스피커 프리미엄'),
(7, 12000, '노트북 스탠드 기본형'),
(7, 24000, '노트북 스탠드 프리미엄'),
(8, 15000, '휴대용 전동칫솔');
