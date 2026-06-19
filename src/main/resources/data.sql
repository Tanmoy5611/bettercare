INSERT INTO user_accounts (name, email, password, receive_email_alerts)
VALUES
    ('demo', 'demo@bettercare.local', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', TRUE);

INSERT INTO user_profile (name, skin_color, age, profile_key)
VALUES
    ('Demo profile', 'OLIVE', 22, 1);

INSERT INTO sensor_data (uv_index, pollution_level, observed_at)
VALUES
    (2, 45, DATEADD('HOUR', -4, CURRENT_TIMESTAMP)),
    (4, 72, DATEADD('HOUR', -3, CURRENT_TIMESTAMP)),
    (6, 118, DATEADD('HOUR', -2, CURRENT_TIMESTAMP)),
    (5, 95, DATEADD('HOUR', -1, CURRENT_TIMESTAMP));

INSERT INTO traffic_data (congestion_level, traffic_jams, observed_at)
VALUES
    (20, 19, DATEADD('HOUR', -4, CURRENT_TIMESTAMP)),
    (35, 24, DATEADD('HOUR', -3, CURRENT_TIMESTAMP)),
    (52, 31, DATEADD('HOUR', -2, CURRENT_TIMESTAMP)),
    (40, 27, DATEADD('HOUR', -1, CURRENT_TIMESTAMP));

INSERT INTO notifications (user_id, message, level, seen, created_at)
VALUES
    (1, 'Welcome to BetterCare demo mode.', 'INFO', FALSE, CURRENT_TIMESTAMP);
