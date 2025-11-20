package com.popcorn.jrp.messaging.producer;

import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.entity.JobEntity;

public interface NotificationProducer {
    public void sendJobCreatedCreatedNotification(JobEntity entity);

    public void sendApplicationStatusNotification(ApplicationEntity entity);

    public void sendApplicantAppliesNotification(ApplicationEntity entity);
}
