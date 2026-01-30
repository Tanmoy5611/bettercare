package org.bettercare.business.services;

import org.bettercare.business.entities.Notification;
import org.bettercare.business.entities.UserAccount;
import org.bettercare.business.entities.enums.NOTIFICATION_LEVEL;
import org.bettercare.data.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // user specific notification (stored only for this user)
    public void createNotification(UserAccount user,
                                   String message,
                                   NOTIFICATION_LEVEL level) {

        Notification notification =
                new Notification(user, message, level, LocalDateTime.now());

        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsFor(UserAccount user) {
        return notificationRepository.findByUser(user);
    }

    public Notification getLatestNotification() {
        List<Notification> all = notificationRepository.findAll();
        return all.isEmpty() ? null : all.get(all.size() - 1);
    }

    // Latest N notifications for one user (home page preview)
    public List<Notification> getLatestForUser(UserAccount user, int limit) {
        return notificationRepository.findLatestByUser(user.getUserId(), limit);
    }

    // mark specific notification as seen
    public void markSeen(int id) {
        notificationRepository.markSeen(id);
    }


    public void markAllAsSeenByUserId(int userId) {
        notificationRepository.markAllSeenForUser(userId);
    }
}