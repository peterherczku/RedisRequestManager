package dev.requestmanager.common.redis.clientToServer.handshake;

import dev.requestmanager.common.redis.interfaces.RedisResponse;

public class HandshakeResponse implements RedisResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
