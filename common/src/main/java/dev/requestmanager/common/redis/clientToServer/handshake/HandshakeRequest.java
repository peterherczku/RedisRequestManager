package dev.requestmanager.common.redis.handshake;

import dev.requestmanager.common.redis.RedisBody;
import dev.requestmanager.common.redis.RedisRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HandshakeRequest extends RedisRequest<HandshakeRequest.Body, HandshakeResponse> {

    public HandshakeRequest() {
        super("HANDSHAKE");
    }

    public HandshakeRequest(Map<String, String> headers, Body body) {
        super("HANDSHAKE", headers, body);
    }

    @Override
    public void processRequest() {
        this.response=new HandshakeResponse();
        this.response.setResponse(body.getName().equalsIgnoreCase("dza"));
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
