package dev.requestmanager.common.redis.clientToServer.handshake;

import dev.requestmanager.common.redis.interfaces.listener.ClientToServerListener;
import dev.requestmanager.common.redis.interfaces.RedisBody;
import dev.requestmanager.common.redis.RedisRequest;
import dev.requestmanager.common.redis.interfaces.RedisRequestListener;

import java.util.Map;

public class HandshakeRequest extends RedisRequest<HandshakeRequest.Body, HandshakeResponse> {

    public HandshakeRequest() {
        super("HANDSHAKE");
    }

    public HandshakeRequest(Map<String, String> headers, Body body) {
        super("HANDSHAKE", headers, body);
    }

    @Override
    public void processRequest(RedisRequestListener listener) {
        ((ClientToServerListener) listener).processHandshake(this);
    }

    public static class Body implements RedisBody {

        private String name;

        public Body(String name) {
            this.name=name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
