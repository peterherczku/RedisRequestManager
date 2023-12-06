package dev.requestmanager.common.redis.clientToServer.handshake;

import dev.requestmanager.common.redis.interfaces.listener.ClientToServerListener;
import dev.requestmanager.common.redis.interfaces.RedisRequestBody;
import dev.requestmanager.common.redis.RedisRequest;
import dev.requestmanager.common.redis.interfaces.RedisRequestListener;

import java.util.Map;

public class HandshakeRequest extends RedisRequest<HandshakeRequest.RequestBody, HandshakeResponse> {

    public HandshakeRequest() {
        super("HANDSHAKE");
    }

    public HandshakeRequest(Map<String, String> headers, RequestBody body) {
        super("HANDSHAKE", headers, body);
    }

    @Override
    public void processRequest(RedisRequestListener listener) {
        ((ClientToServerListener) listener).processHandshake(this);
    }

    public static class RequestBody implements RedisRequestBody {

        private String name;

        public RequestBody(String name) {
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
