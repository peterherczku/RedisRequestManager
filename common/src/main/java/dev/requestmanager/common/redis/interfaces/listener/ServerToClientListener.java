package dev.requestmanager.common.redis.interfaces.listener;

import dev.requestmanager.common.redis.interfaces.RedisRequestListener;
import dev.requestmanager.common.redis.serverToClient.game.GameStartRequest;

public interface ServerToClientListener extends RedisRequestListener {

    void processGameStart(GameStartRequest request);

}
