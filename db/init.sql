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
    reward_amount INT DEFAULT NULL,
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


