package dev.requestmanager.client;

import dev.requestmanager.common.redis.async.AsyncRequestClient;
import dev.requestmanager.common.redis.clientToServer.handshake.HandshakeRequest;
import dev.requestmanager.common.redis.clientToServer.handshake.HandshakeResponse;
import dev.requestmanager.common.redis.sync.SyncRequestClient;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // sync
        SyncRequestClient syncRequestClient = new SyncRequestClient("CLIENT->SERVER");
        syncRequestClient.init();

        HandshakeRequest handshakeRequestSync = new HandshakeRequest(
                Map.of("header name", "header value"),
                new HandshakeRequest.RequestBody("Quynh")
        );
        HandshakeResponse response = syncRequestClient.send(handshakeRequestSync, HandshakeResponse.class);
    }

}
