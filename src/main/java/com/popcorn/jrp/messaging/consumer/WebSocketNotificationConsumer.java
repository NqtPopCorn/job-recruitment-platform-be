package com.popcorn.jrp.messaging.consumer;

public interface WebSocketNotificationConsumer {
    public void consumeWebSocketJobNotification(String message);
}
