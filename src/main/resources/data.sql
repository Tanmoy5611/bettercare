-- This demo user makes it easier to test the application after startup
INSERT INTO user_accounts (name, email, password, receive_email_alerts)
VALUES
    ('demo', 'demo@bettercare.local', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', TRUE);

INSERT INTO user_profile (name, skin_color, age, profile_key)
VALUES
    ('Demo profile', 'OLIVE', 22, 1);

-- These readings give the dashboard data to show in the graphs
INSERT INTO sensor_data (uv_index, pollution_level, observed_at)
VALUES
    (2, 64, DATEADD('DAY', -45, CURRENT_TIMESTAMP)),
    (3, 72, DATEADD('DAY', -38, CURRENT_TIMESTAMP)),
    (4, 85, DATEADD('DAY', -31, CURRENT_TIMESTAMP)),
    (5, 105, DATEADD('DAY', -28, CURRENT_TIMESTAMP)),
    (6, 120, DATEADD('DAY', -24, CURRENT_TIMESTAMP)),
    (7, 145, DATEADD('DAY', -20, CURRENT_TIMESTAMP)),
    (5, 130, DATEADD('DAY', -16, CURRENT_TIMESTAMP)),
    (4, 110, DATEADD('DAY', -12, CURRENT_TIMESTAMP)),
    (6, 125, DATEADD('DAY', -9, CURRENT_TIMESTAMP)),
    (7, 150, DATEADD('DAY', -7, CURRENT_TIMESTAMP)),
    (5, 135, DATEADD('DAY', -6, CURRENT_TIMESTAMP)),
    (8, 160, DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
    (7, 150, DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
    (5, 120, DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
    (4, 110, DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
    (6, 125, DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
    (5, 130, DATEADD('HOUR', -21, CURRENT_TIMESTAMP)),
    (7, 140, DATEADD('HOUR', -18, CURRENT_TIMESTAMP)),
    (8, 155, DATEADD('HOUR', -15, CURRENT_TIMESTAMP)),
    (6, 145, DATEADD('HOUR', -12, CURRENT_TIMESTAMP)),
    (4, 130, DATEADD('HOUR', -9, CURRENT_TIMESTAMP)),
    (3, 110, DATEADD('HOUR', -6, CURRENT_TIMESTAMP)),
    (2, 95, DATEADD('HOUR', -3, CURRENT_TIMESTAMP)),
    (3, 105, DATEADD('HOUR', -1, CURRENT_TIMESTAMP));

INSERT INTO traffic_data (congestion_level, traffic_jams, observed_at)
VALUES
    (22, 3, DATEADD('DAY', -45, CURRENT_TIMESTAMP)),
    (30, 5, DATEADD('DAY', -38, CURRENT_TIMESTAMP)),
    (38, 7, DATEADD('DAY', -31, CURRENT_TIMESTAMP)),
    (45, 9, DATEADD('DAY', -28, CURRENT_TIMESTAMP)),
    (52, 12, DATEADD('DAY', -24, CURRENT_TIMESTAMP)),
    (60, 16, DATEADD('DAY', -20, CURRENT_TIMESTAMP)),
    (48, 10, DATEADD('DAY', -16, CURRENT_TIMESTAMP)),
    (42, 8, DATEADD('DAY', -12, CURRENT_TIMESTAMP)),
    (50, 12, DATEADD('DAY', -9, CURRENT_TIMESTAMP)),
    (64, 18, DATEADD('DAY', -7, CURRENT_TIMESTAMP)),
    (58, 15, DATEADD('DAY', -6, CURRENT_TIMESTAMP)),
    (70, 22, DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
    (62, 18, DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
    (45, 10, DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
    (36, 7, DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
    (50, 11, DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
    (34, 6, DATEADD('HOUR', -21, CURRENT_TIMESTAMP)),
    (48, 11, DATEADD('HOUR', -18, CURRENT_TIMESTAMP)),
    (66, 20, DATEADD('HOUR', -15, CURRENT_TIMESTAMP)),
    (72, 24, DATEADD('HOUR', -12, CURRENT_TIMESTAMP)),
    (60, 17, DATEADD('HOUR', -9, CURRENT_TIMESTAMP)),
    (44, 9, DATEADD('HOUR', -6, CURRENT_TIMESTAMP)),
    (32, 5, DATEADD('HOUR', -3, CURRENT_TIMESTAMP)),
    (40, 8, DATEADD('HOUR', -1, CURRENT_TIMESTAMP));

INSERT INTO notifications (user_id, message, level, seen, created_at)
VALUES
    (1, 'Welcome to BetterCare demo mode.', 'INFO', FALSE, CURRENT_TIMESTAMP),
    (1, 'UV levels may rise later today. Consider sunscreen before going outside.', 'WARNING', FALSE, DATEADD('MINUTE', -15, CURRENT_TIMESTAMP)),
    (1, 'Air quality is currently moderate. Sensitive individuals should reduce prolonged outdoor activity.', 'WARNING', TRUE, DATEADD('HOUR', -2, CURRENT_TIMESTAMP));