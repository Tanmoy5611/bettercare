package org.bettercare.data.repository;

import org.bettercare.business.entities.Notification;
import org.bettercare.business.entities.UserAccount;
import org.bettercare.business.entities.enums.NOTIFICATION_LEVEL;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcNotificationRepository implements NotificationRepository {

    private final JdbcClient jdbcClient;

    public JdbcNotificationRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    // Save a notification (user specific or global if user is null)
    @Override
    public void save(Notification n) {
        String sql = """
            INSERT INTO notifications (user_id, message, level, seen, created_at)
            VALUES (:userId, :message, :level, :seen, :createdAt)
        """;

        jdbcClient.sql(sql)
                .params(Map.of(
                        "userId", n.getUser() != null ? n.getUser().getUserId() : null,
                        "message", n.getMessage(),
                        "level", n.getLevel().name(),
                        "seen", n.isSeen(),
                        "createdAt", n.getCreatedAt()
                ))
                .update();
    }

    // Get all notifications for a specific user
    @Override
    public List<Notification> findByUser(UserAccount user) {
        String sql = """
            SELECT notification_id, message, level, seen, created_at
            FROM notifications
            WHERE user_id = :userId
            ORDER BY created_at DESC
        """;

        return jdbcClient.sql(sql)
                .param("userId", user.getUserId())
                .query((rs, rowNum) -> {
                    Notification n = new Notification();
                    n.setNotificationId(rs.getInt("notification_id"));
                    n.setMessage(rs.getString("message"));
                    n.setLevel(NOTIFICATION_LEVEL.valueOf(rs.getString("level")));
                    n.setSeen(rs.getBoolean("seen"));
                    n.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    n.setUser(user);
                    return n;
                })
                .list();
    }

    // Get all notifications
    @Override
    public List<Notification> findAll() {
        return jdbcClient.sql("SELECT * FROM notifications")
                .query((rs, rowNum) -> {
                    Notification n = new Notification();
                    n.setNotificationId(rs.getInt("notification_id"));
                    n.setMessage(rs.getString("message"));
                    n.setLevel(NOTIFICATION_LEVEL.valueOf(rs.getString("level")));
                    n.setSeen(rs.getBoolean("seen"));
                    n.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return n;
                })
                .list();
    }

    // Mark a single notification as seen
    @Override
    public void markSeen(int id) {
        jdbcClient.sql("""
            UPDATE notifications SET seen = TRUE
            WHERE notification_id = :id
        """)
                .param("id", id)
                .update();
    }

    // Mark all notifications as seen for a specific user
    @Override
    public void markAllSeenForUser(int userId) {
        jdbcClient.sql("""
        UPDATE notifications
        SET seen = TRUE
        WHERE user_id = :userId
    """)
                .param("userId", userId)
                .update();
    }

    // Get the latest N notifications for one user
    @Override
    public List<Notification> findLatestByUser(int userId, int limit) {
        String sql = """
        SELECT notification_id, message, level, seen, created_at
        FROM notifications
        WHERE user_id = :userId
        ORDER BY created_at DESC
        LIMIT :limit
    """;

        return jdbcClient.sql(sql)
                .param("userId", userId)
                .param("limit", limit)
                .query((rs, rowNum) -> {
                    Notification n = new Notification();
                    n.setNotificationId(rs.getInt("notification_id"));
                    n.setMessage(rs.getString("message"));
                    n.setLevel(NOTIFICATION_LEVEL.valueOf(rs.getString("level")));
                    n.setSeen(rs.getBoolean("seen"));
                    n.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return n;
                })
                .list();
    }
}