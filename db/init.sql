CREATE DATABASE IF NOT EXISTS testdb;

USE testdb;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    nickname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER', 'SUSPENDED') NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    school_email VARCHAR(255),
    school_email_verified BOOLEAN,
    postal_code VARCHAR(10),
    road_address VARCHAR(255),
    land_lot_address VARCHAR(255),
    detail_address VARCHAR(255),
    profile_image VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO users (email, nickname, password, role, phone_number, created_at)
VALUES ('test@example.com', 'test_user', 'password123', 'USER', '010-1234-5678', '2024-09-10 00:00:00');

CREATE TABLE IF NOT EXISTS funding (
    funding_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    current ENUM('작성중', '심사중', '심사완료', '펀딩진행중', '펀딩종료') NOT NULL,
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
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

INSERT INTO funding (current, category, organizer_name, organizer_email, tax_email, organizer_id_card, user_id)
VALUES ('작성중', '게임', '홍길동', 'honggildong@example.com', 'tax@example.com', '/path/to/id_card.jpg', 1);