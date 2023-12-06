package dev.requestmanager.common.redis;

import dev.requestmanager.common.redis.clientToServer.handshake.HandshakeRequest;

public interface ClientToServerListener extends RedisRequestListener{

    void processHandshake(HandshakeRequest request);

}
