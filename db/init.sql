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
    school_email_verified BOOLEAN DEFAULT FALSE,
    postal_code VARCHAR(10),
    road_address VARCHAR(255),
    land_lot_address VARCHAR(255),
    detail_address VARCHAR(255),
    profile_image VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO users (email, nickname, password, role, phone_number)
VALUES ('test@example.com', 'test_user', 'password123', 'USER', '010-1234-5678');

INSERT INTO users (email, nickname, password, role, phone_number)
VALUES ('test2@example.com', 'test_user2', 'password123', 'USER', '010-1111-2222');

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

INSERT INTO funding (current_amount, target_amount, current, category, sub_category, organizer_name, organizer_email, tax_email, organizer_id_card, user_id)
VALUES (75000, 100000, 'DRAFT', 'A0040', 'B0180', '홍길동', 'honggildong@example.com', 'tax@example.com', '/path/to/id_card.jpg', 1);

INSERT INTO funding (current_amount, target_amount, current, category, sub_category, organizer_name, organizer_email, tax_email, organizer_id_card, user_id)
VALUES (25000, 100000, 'REVIEW', 'A0010', 'B0170', 'testuser', 'test@example.com', 'tax@example.com', '/path/to/id_card.jpg', 1);

INSERT INTO funding (current_amount, target_amount, current, category, sub_category, organizer_name, organizer_email, tax_email, organizer_id_card, user_id)
VALUES (80000, 100000, 'REVIEW_COMPLETED', 'A0020', 'B0160', 'testuser2', 'test2@example.com', 'tax@example.com', '/path/to/id_card.jpg', 2);

INSERT INTO funding (current_amount, target_amount, current, category, sub_category, organizer_name, organizer_email, tax_email, organizer_id_card, user_id)
VALUES (30000, 100000, 'ONGOING', 'A0050', 'B0150', 'testuser3', 'test3@example.com', 'tax@example.com', '/path/to/id_card.jpg', 2);

INSERT INTO funding (current_amount, target_amount, current, category, sub_category, organizer_name, organizer_email, tax_email, organizer_id_card, user_id)
VALUES (50000, 100000, 'CLOSED', 'A0060', 'B0140', 'testuser4', 'test4@example.com', 'tax@example.com', '/path/to/id_card.jpg', 2);

CREATE TABLE IF NOT EXISTS notification (
    notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    message VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

INSERT INTO notification (message, user_id) VALUES ('새로운 프로젝트를 시작해보세요.', 1);

CREATE TABLE IF NOT EXISTS tag (
    tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tag_name VARCHAR(255) NOT NULL,
    category ENUM('A0010', 'A0020', 'A0030', 'A0040', 'A0050', 'A0060', 'A0070', 'A0080',
                  'A0090', 'A0100', 'A0110', 'A0120', 'A0130', 'A0140', 'A0150', 'A0160', 'A0170'),
    sub_category VARCHAR(255)
);
INSERT INTO tag (tag_name)
VALUES ('태그1');

INSERT INTO tag (tag_name)
VALUES ('태그2');

CREATE TABLE IF NOT EXISTS funding_tag (
    funding_tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funding_id BIGINT,
    tag_id BIGINT,
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id),
    FOREIGN KEY (tag_id) REFERENCES tag(tag_id)
);
INSERT INTO funding_tag (tag_id, funding_id)
VALUES (1, 1);

INSERT INTO funding_tag (tag_id, funding_id)
VALUES (2, 1);

CREATE TABLE IF NOT EXISTS funding_like (
    funding_like_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    funding_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (funding_id) REFERENCES funding(funding_id)
);

INSERT INTO funding_like (user_id, funding_id)
VALUES (1, 3);

INSERT INTO funding_like (user_id, funding_id)
VALUES (1, 4);

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

INSERT INTO funder (funding_id, user_id)
VALUES (5, 1);

INSERT INTO funder (funding_id, user_id)
VALUES (4, 1);

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