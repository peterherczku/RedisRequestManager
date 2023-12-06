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
                new HandshakeRequest.Body("Peter")
        );
        HandshakeResponse response = syncRequestClient.send(handshakeRequestSync, HandshakeResponse.class);
        System.out.println("SYNC: "+response.getMessage());

        // async with completable future

        AsyncRequestClient asyncRequestClient = new AsyncRequestClient("CLIENT->SERVER");
        asyncRequestClient.init();

        HandshakeRequest handshakeRequest = new HandshakeRequest(
                Map.of("header name", "header value"),
                new HandshakeRequest.Body("Peter")
        );
        asyncRequestClient.send(handshakeRequest, HandshakeResponse.class).thenAccept((handshakeResponse -> {
            System.out.println("ASYNC: "+handshakeResponse.getMessage());
        }));
    }

}
