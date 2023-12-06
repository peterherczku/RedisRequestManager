package dev.requestmanager.common.redis.handshake;

import dev.requestmanager.common.redis.RedisResponse;

public class HandshakeResponse implements RedisResponse {

    private boolean success;

    public void setResponse(boolean success) {
        this.success=success;
    }

    public boolean isSuccess() {
        return success;
    }

}
