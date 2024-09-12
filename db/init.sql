CREATE DATABASE IF NOT EXISTS testdb;

USE testdb;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    nickname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL,
    phoneNumber VARCHAR(20),
    schoolEmail VARCHAR(255),
    schoolEmailVerified BOOLEAN,
    postalCode VARCHAR(10),
    roadAddress VARCHAR(255),
    landLotAddress VARCHAR(255),
    detailAddress VARCHAR(255),
    profileImage VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO users (email, nickname, password, role, created_at)
VALUES ('test@example.com', 'test_user', 'password123', 'USER', '2024-09-10 00:00:00');

CREATE TABLE IF NOT EXISTS funding (
    funding_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    current ENUM('작성중', '심사중', '심사완료', '진행중') NOT NULL,
    category ENUM('캐릭터_굿즈', '홈_리빙', '사진', '게임', '키즈', '도서_전자책', '여행', '만화_웹툰',
                  '스포츠_아웃도어', '테크_가전', '자동차', '패션', '아트', '소셜', '영화_음악', '반려동물', '디자인') NOT NULL,
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
    total_visitors INT DEFAULT 0,
    today_visitors INT DEFAULT 0,
    today_amount INT DEFAULT 0,
    current_amount INT DEFAULT 0,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

INSERT INTO funding (current, category, organizer_name, organizer_email, tax_email, organizer_id_card, user_id)
VALUES ('작성중', '게임', '홍길동', 'honggildong@example.com', 'tax@example.com', '/path/to/id_card.jpg', 1);