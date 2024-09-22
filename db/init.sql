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
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS funding (
    funding_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    current ENUM('DRAFT', 'REVIEW', 'REVIEW_COMPLETED', 'ONGOING', 'CLOSED') NOT NULL,
    category ENUM('A0010', 'A0020', 'A0030', 'A0040', 'A0050', 'A0060', 'A0070', 'A0080',
                  'A0090', 'A0100', 'A0110', 'A0120', 'A0130', 'A0140', 'A0150', 'A0160', 'A0170') NOT NULL,
    sub_category VARCHAR(255) NOT NULL,
    organizer_name VARCHAR(255) NOT NULL,
    organizer_email VARCHAR(255) NOT NULL,
    tax_email VARCHAR(255) NOT NULL,
    organizer_id_card VARCHAR(255) NOT NULL,
    start_date DATETIME DEFAULT NULL,
    end_date DATETIME DEFAULT NULL,
    target_amount INT DEFAULT NULL,
    title VARCHAR(255),
    main_image VARCHAR(255),
    project_summary TEXT,
    product_info TEXT,
    refund_policy TEXT,
    total_likes INT DEFAULT 0,
    today_likes INT DEFAULT 0,
    total_visitors INT DEFAULT 0,
    today_visitors INT DEFAULT 0,
    today_amount INT DEFAULT 0,
    current_amount INT DEFAULT 0,
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
    category ENUM('A0010', 'A0020', 'A0030', 'A0040', 'A0050', 'A0060', 'A0070', 'A0080',
                  'A0090', 'A0100', 'A0110', 'A0120', 'A0130', 'A0140', 'A0150', 'A0160', 'A0170'),
    sub_category VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS funding_tag (
    funding_tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funding_id BIGINT,
    tag_id BIGINT,
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id),
    FOREIGN KEY (tag_id) REFERENCES tag(tag_id)
);

CREATE TABLE IF NOT EXISTS funding_like (
    funding_like_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    funding_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id)
);

CREATE TABLE IF NOT EXISTS approval_document (
    document_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funding_id BIGINT,
    document_path VARCHAR(255) NOT NULL,
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id)
);

CREATE TABLE IF NOT EXISTS intro_image (
    introimg_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funding_id BIGINT,
    introimg_path VARCHAR(255) NOT NULL,
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
    selreward_id BIGINT AUTO_INCREMENT PRIMARY KEY,
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

CREATE TABLE IF NOT EXISTS comment (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funding_id BIGINT,
    user_id BIGINT,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id),
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

INSERT INTO funding (title, project_summary, current_amount, target_amount, current, category, sub_category, organizer_name, organizer_email, tax_email, organizer_id_card, user_id)
VALUES
('10초 완판 | 차원이 다른 스마트 가습기', '강력한 미세 분사와 자동 습도 조절 기능 탑재', 1500000, 5000000, 'DRAFT', 'A0100', 'B0530', '더워터', 'organizer1@example.com', 'tax1@example.com', '/path/to/id_card1.jpg', 1),
('여행자를 위한 올인원 멀티툴', '캠핑부터 도심 여행까지, 하나로 충분한 멀티툴', 700000, 1000000, 'DRAFT', 'A0070', 'B0380', '트래블팩', 'organizer2@example.com', 'tax2@example.com', '/path/to/id_card2.jpg', 1),
('한정판 | 차세대 친환경 에코백', '100% 재활용 소재로 만들어진 에코백', 5000000, 8000000, 'DRAFT', 'A0170', 'B0950', '에코리브', 'organizer3@example.com', 'tax3@example.com', '/path/to/id_card3.jpg', 1),
('반려동물을 위한 스마트 피딩기', '자동 급식 및 원격 제어 기능으로 편리한 반려동물 관리', 3000000, 6000000, 'REVIEW', 'A0160', 'B0910', '펫케어', 'organizer4@example.com', 'tax4@example.com', '/path/to/id_card4.jpg', 1),
('세상을 바꾸는 휴대용 태양광 충전기', '휴대가 간편한 태양광 충전기, 어디서나 충전 가능', 2000000, 3000000, 'REVIEW_COMPLETED', 'A0100', 'B0100', '솔라팩', 'organizer5@example.com', 'tax5@example.com', '/path/to/id_card5.jpg', 1),
('스마트홈을 위한 차세대 AI 스피커', 'AI 기술로 더욱 똑똑해진 스마트 스피커', 10000000, 15000000, 'DRAFT', 'A0100', 'B0550', '홈스마트', 'organizer6@example.com', 'tax6@example.com', '/path/to/id_card6.jpg', 1),
('학생들을 위한 다기능 노트북 스탠드', '높이 조절이 가능한 인체공학적 디자인의 노트북 스탠드', 1000000, 2000000, 'REVIEW', 'A0020', 'B0540', '스탠드컴퍼니', 'organizer7@example.com', 'tax7@example.com', '/path/to/id_card7.jpg', 1),
('신개념 휴대용 전동칫솔', '초고속 전동 칫솔로 어디서나 간편한 구강 관리', 1500000, 2500000, 'ONGOING', 'A0020', 'B0540', '클린덴트', 'organizer8@example.com', 'tax8@example.com', '/path/to/id_card8.jpg', 1),
('100% 천연 성분의 스킨케어 제품', '피부에 자극 없는 순한 천연 스킨케어', 800000, 2000000, 'ONGOING', 'A0020', 'B0050', '네이처케어', 'organizer9@example.com', 'tax9@example.com', '/path/to/id_card9.jpg', 1),
('한 번 충전으로 한 달 사용 가능한 무선 청소기', '강력한 흡입력과 긴 사용시간을 자랑하는 무선 청소기', 3000000, 5000000, 'ONGOING', 'A0100', 'B0540', '클린홈', 'organizer10@example.com', 'tax10@example.com', '/path/to/id_card10.jpg', 1),
('프리미엄 블루투스 이어폰', '고음질 무선 블루투스 이어폰으로 새로운 음악 경험', 7000000, 10000000, 'REVIEW_COMPLETED', 'A0100', 'B0590', '뮤직소울', 'organizer11@example.com', 'tax11@example.com', '/path/to/id_card11.jpg', 1),
('실내외 모두 사용 가능한 스마트 LED 조명', '자동 밝기 조절 및 다양한 색상 변경이 가능한 LED 조명', 1200000, 2500000, 'REVIEW', 'A0100', 'B0530', '라이트업', 'organizer12@example.com', 'tax12@example.com', '/path/to/id_card12.jpg', 1),
('친환경 소재로 만든 프리미엄 침구 세트', '편안한 수면을 위한 천연 소재 침구 세트', 5000000, 9000000, 'CLOSED', 'A0020', 'B0950', '슬리프리', 'organizer13@example.com', 'tax13@example.com', '/path/to/id_card13.jpg', 1),
('한정판 스마트 커피머신', '원터치로 즐기는 고품질 커피, 스마트 기능 탑재', 8000000, 12000000, 'DRAFT', 'A0100', 'B0530', '커피클럽', 'organizer14@example.com', 'tax14@example.com', '/path/to/id_card14.jpg', 1),
('차세대 무선 충전 패드', '모든 기기와 호환 가능한 초고속 무선 충전 패드', 3000000, 4000000, 'CLOSED', 'A0100', 'B0590', '테크센스', 'organizer15@example.com', 'tax15@example.com', '/path/to/id_card15.jpg', 1);

INSERT INTO tag (tag_name) VALUES ('프리미엄');
INSERT INTO tag (tag_name) VALUES ('한정판');
INSERT INTO tag (tag_name) VALUES ('스마트');
INSERT INTO tag (tag_name) VALUES ('일상');
INSERT INTO tag (tag_name) VALUES ('디자인');
INSERT INTO tag (tag_name) VALUES ('기술');
INSERT INTO tag (tag_name) VALUES ('선물');
INSERT INTO tag (tag_name) VALUES ('가방');
INSERT INTO tag (tag_name) VALUES ('플래너');

INSERT INTO funding_tag (tag_id, funding_id) VALUES (1, 1);
INSERT INTO funding_tag (tag_id, funding_id) VALUES (2, 1);
INSERT INTO funding_tag (tag_id, funding_id) VALUES (3, 2);
INSERT INTO funding_tag (tag_id, funding_id) VALUES (4, 2);
INSERT INTO funding_tag (tag_id, funding_id) VALUES (5, 3);
INSERT INTO funding_tag (tag_id, funding_id) VALUES (6, 4);
INSERT INTO funding_tag (tag_id, funding_id) VALUES (7, 5);
INSERT INTO funding_tag (tag_id, funding_id) VALUES (8, 6);
INSERT INTO funding_tag (tag_id, funding_id) VALUES (9, 7);

INSERT INTO notification (message, user_id) VALUES ('새로운 프로젝트를 시작해보세요.', 1);

INSERT INTO funding_like (user_id, funding_id, created_at)
VALUES
(1, 1, '2024-09-22 11:01:00'),
(1, 2, '2024-09-22 11:02:00'),
(1, 3, '2024-09-22 11:03:00'),
(2, 6, '2024-09-22 11:06:00'),
(1, 7, '2024-09-22 11:07:00'),
(1, 8, '2024-09-22 11:08:00'),
(1, 11, '2024-09-22 11:11:00'),
(1, 12, '2024-09-22 11:12:00'),
(1, 13, '2024-09-22 11:13:00'),
(1, 15, '2024-09-22 11:15:00');

INSERT INTO funder (user_id, funding_id, created_at)
VALUES
(1, 1, '2024-09-22 11:01:00'),
(1, 2, '2024-09-22 11:02:00'),
(1, 3, '2024-09-22 11:03:00'),
(1, 4, '2024-09-22 11:04:00'),
(1, 5, '2024-09-22 11:05:00'),
(1, 9, '2024-09-22 11:09:00'),
(1, 10, '2024-09-22 11:10:00'),
(1, 11, '2024-09-22 11:11:00'),
(1, 12, '2024-09-22 11:12:00'),
(1, 14, '2024-09-22 11:14:00'),
(1, 15, '2024-09-22 11:15:00');
