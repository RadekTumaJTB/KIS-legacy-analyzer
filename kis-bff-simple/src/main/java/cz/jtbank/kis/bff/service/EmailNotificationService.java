package cz.jtbank.kis.bff.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Email notification service
 * Based on original: Mail.sendBudgetZmenaCastky()
 */
@Service
public class EmailNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    /**
     * Send email notification about budget amount change
     *
     * @param budgetCode Budget code (e.g., "BUD-2025-001")
     * @param budgetName Budget name (e.g., "IT Oddělení 2025")
     * @param month Month number (1-12)
     * @param monthName Month name (e.g., "Únor")
     * @param origAmount Original amount
     * @param newAmount New amount
     * @param currency Currency code (e.g., "CZK")
     * @param changedBy Username who made the change
     */
    public void sendBudgetAmountChangeNotification(
            String budgetCode,
            String budgetName,
            Integer month,
            String monthName,
            BigDecimal origAmount,
            BigDecimal newAmount,
            String currency,
            String changedBy) {

        // Build email message (original format)
        StringBuilder message = new StringBuilder();
        message.append("V rozpočtu '").append(budgetName).append("' (").append(budgetCode).append(")");
        message.append(" byla u měsíce '").append(monthName).append("'");
        message.append(" změněna částka z ").append(origAmount);
        message.append(" na ").append(newAmount);
        message.append(" (v ").append(currency).append(").");
        message.append(" Změnu částky provedl(a) ").append(changedBy).append(".");

        // Log the notification (in production, would send actual email)
        logger.info("=== EMAIL NOTIFICATION ===");
        logger.info("Subject: Budgeting - změna částky");
        logger.info("Message: {}", message);
        logger.info("Recipients: kontroling@jtbank.cz (+ budget stakeholders)");
        logger.info("==========================");

        // TODO: In production, integrate with actual email service
        // For example: JavaMailSender, SendGrid, AWS SES, etc.
        /*
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("kis@jtbank.cz");
            helper.setTo(getRecipients());
            helper.setSubject("Budgeting - změna částky");
            helper.setText(message.toString(), false);

            mailSender.send(mimeMessage);
            logger.info("Email notification sent successfully");
        } catch (Exception e) {
            logger.error("Failed to send email notification", e);
        }
        */
    }

    /**
     * Send email notification about document amount change
     *
     * @param documentNumber Document number
     * @param documentType Document type
     * @param origAmount Original amount
     * @param newAmount New amount
     * @param currency Currency code
     * @param changedBy Username who made the change
     */
    public void sendDocumentAmountChangeNotification(
            String documentNumber,
            String documentType,
            BigDecimal origAmount,
            BigDecimal newAmount,
            String currency,
            String changedBy) {

        StringBuilder message = new StringBuilder();
        message.append("U dokumentu '").append(documentNumber).append("' (").append(documentType).append(")");
        message.append(" byla změněna částka z ").append(origAmount);
        message.append(" na ").append(newAmount);
        message.append(" (v ").append(currency).append(").");
        message.append(" Změnu provedl(a) ").append(changedBy).append(".");

        logger.info("=== EMAIL NOTIFICATION ===");
        logger.info("Subject: Dokument - změna částky");
        logger.info("Message: {}", message);
        logger.info("Recipients: finance@jtbank.cz (+ document stakeholders)");
        logger.info("==========================");
    }
}
