package springboot.services.interfaces;

import java.util.List;

import springboot.dto.request.CreateNotification;
import springboot.entities.NotificationEntity;

public interface Notification {
	
	public NotificationEntity findById(Long id);
	public List<NotificationEntity> findAll();
	
	public String generatePersonalization(NotificationEntity notificationEntity);
	
	public boolean validateTemplateFields(CreateNotification createNotificationRequest);
	public NotificationEntity buildNotificationEntity(CreateNotification createNotificationRequest);
	public NotificationEntity persistData(NotificationEntity notificationEntity);
}
