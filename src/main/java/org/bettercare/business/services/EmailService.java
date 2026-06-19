package org.bettercare.business.services;

import jakarta.mail.internet.MimeMessage;
import org.bettercare.business.entities.enums.NOTIFICATION_LEVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${bettercare.from}")
    private String from;

    @Value("${bettercare.base-url}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender,
                        SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // Simple alert email
    public void sendAlertEmail(String to,
                               String message,
                               NOTIFICATION_LEVEL level) {

        String template = switch (level) {
            case INFO -> "email/alert-info.html";
            case WARNING -> "email/alert-warning.html";
            case DANGER -> "email/alert-danger.html";
        };

        String now = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));

        try {
            Context ctx = new Context();
            ctx.setVariable("message", message);
            ctx.setVariable("date", now);

            String htmlContent = templateEngine.process(template, ctx);

            sendEmail(to, "BetterCare Alert", htmlContent);

        } catch (Exception e) {
            log.warn("Template rendering failed, using fallback email", e);
            sendEmail(to, "BetterCare Alert", buildFallbackHtml(message, now));
        }
    }

    // Detailed alert email
    public void sendDetailedAlertEmail(String to,
                                       String userName,
                                       String message,
                                       int healthScore,
                                       int uvIndex,
                                       int airQuality,
                                       NOTIFICATION_LEVEL level) {

        String template = switch (level) {
            case INFO -> "email/alert-info.html";
            case WARNING -> "email/alert-warning.html";
            case DANGER -> "email/alert-danger.html";
        };

        String now = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));

        Context ctx = new Context();
        ctx.setVariable("userName", userName);
        ctx.setVariable("message", message);
        ctx.setVariable("healthScore", healthScore);
        ctx.setVariable("uvIndex", uvIndex);
        ctx.setVariable("airQuality", airQuality);
        ctx.setVariable("date", now);
        ctx.setVariable("detailsUrl", baseUrl + "/notifications");

        String html = templateEngine.process(template, ctx);

        sendEmail(to, "BetterCare - Health Alert", html);
    }

    // Low-level sender
    private void sendEmail(String to,
                           String subject,
                           String html) {

        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(mail);
            log.info("Email sent to {}", to);

        } catch (Exception e) {
            log.warn("Email sending failed", e);
        }
    }

    // Fallback HTML
    private String buildFallbackHtml(String message, String date) {
        return """
                <div style="font-family:Arial;padding:20px;background:#f4f4f4;
                            border-radius:8px;border:1px solid #ccc">
                    <h2 style="color:#0d6efd;">BetterCare Alert</h2>
                    <p>%s</p>
                    <small>Date: %s</small>
                </div>
                """.formatted(message, date);
    }
}
