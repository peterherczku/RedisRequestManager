package dev.requestmanager.common.redis.interfaces.listener;

import dev.requestmanager.common.redis.clientToServer.handshake.HandshakeRequest;
import dev.requestmanager.common.redis.interfaces.RedisRequestListener;

public interface ClientToServerListener extends RedisRequestListener {

    void processHandshake(HandshakeRequest request);

}
