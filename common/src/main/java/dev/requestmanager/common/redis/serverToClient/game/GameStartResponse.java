package dev.requestmanager.common.redis.serverToClient.game;

import dev.requestmanager.common.redis.interfaces.RedisResponse;

public class GameStartResponse implements RedisResponse {

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
