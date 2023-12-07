package dev.requestmanager.server;

import dev.requestmanager.common.redis.interfaces.listener.ClientToServerListener;
import dev.requestmanager.common.redis.RequestServer;
import dev.requestmanager.common.redis.clientToServer.handshake.HandshakeRequest;
import dev.requestmanager.common.redis.clientToServer.handshake.HandshakeResponse;

public class Main {

    public static void main(String[] args) {
        RequestServer syncRequestServer = new RequestServer("CLIENT->SERVER", new RequestHandler());
        syncRequestServer.registerRequest(new HandshakeRequest());
        syncRequestServer.init();
    }

    public static class RequestHandler implements ClientToServerListener {
        @Override
        public void processHandshake(HandshakeRequest request) {
            HandshakeResponse response =new HandshakeResponse();
            String name = request.getBody().getName();
            response.setMessage("Hello "+name+", it's lovely to meet you!");
            request.setResponse(response);
        }
    }

}
