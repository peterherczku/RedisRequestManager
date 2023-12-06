package dev.requestmanager.common.redis;

import dev.requestmanager.common.redis.clientToServer.handshake.HandshakeRequest;
import dev.requestmanager.common.redis.serverToClient.game.GameStartRequest;

public interface ServerToClientListener extends RedisRequestListener {

    void processGameStart(GameStartRequest request);

}
