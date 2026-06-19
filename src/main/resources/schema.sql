-- User account details are stored in this table
CREATE TABLE user_accounts (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL,
    password VARCHAR(100) NOT NULL,
    date_subscription DATE DEFAULT CURRENT_DATE,
    receive_email_alerts BOOLEAN DEFAULT FALSE
);

-- Each profile belongs to one user account
CREATE TABLE user_profile (
    profile_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    skin_color VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    profile_key INT NOT NULL,
    CONSTRAINT fk_user_profile_account
        FOREIGN KEY (profile_key) REFERENCES user_accounts(user_id)
        ON DELETE CASCADE
);

CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    message VARCHAR(1000) NOT NULL,
    level VARCHAR(20) NOT NULL,
    seen BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notifications_account
        FOREIGN KEY (user_id) REFERENCES user_accounts(user_id)
        ON DELETE CASCADE
);

CREATE TABLE sensor_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uv_index INT NOT NULL,
    pollution_level INT NOT NULL,
    observed_at TIMESTAMP NOT NULL
);

CREATE TABLE traffic_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    congestion_level INT NOT NULL,
    traffic_jams INT NOT NULL,
    observed_at TIMESTAMP NOT NULL
);