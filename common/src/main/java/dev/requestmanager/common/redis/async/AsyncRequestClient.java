package dev.requestmanager.common.redis.async;

import com.google.gson.Gson;
import dev.requestmanager.common.redis.RedisRequest;
import dev.requestmanager.common.redis.interfaces.RedisResponse;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class AsyncRequestClient {

    private final String channelId;
    private ConcurrentMap<String, Consumer<RedisResponse>> awaitingRequests = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Class<? extends RedisResponse>> awaitingRequestsClasses = new ConcurrentHashMap<>();

    public AsyncRequestClient(String channelId) {
        this.channelId=channelId;
    }

    public void init() {
        System.out.println("Initializing Async Client...");
        connectToChannel();
        try {
            Thread.sleep(1000);
            System.out.println("Async Client initialized.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleIncomingResponse(JSONObject message) {
        String requestId = message.getString("requestId");
        Gson gson = new Gson();
        if (!awaitingRequests.containsKey(requestId)) return;
        awaitingRequests.get(requestId).accept(gson.fromJson(message.getJSONObject("body").toString(), awaitingRequestsClasses.get(requestId)));
        awaitingRequestsClasses.remove(requestId);
        awaitingRequests.remove(requestId);
    }

    public void connectToChannel() {
        Thread thread = new Thread(() -> {
            Jedis jedis = new Jedis();
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    JSONObject messageObject = new JSONObject(message);
                    if (!messageObject.has("response")) return;

                    handleIncomingResponse(messageObject);
                }
            }, channelId);
        });

        thread.start();
    }

    public <T extends RedisRequest, K extends RedisResponse> CompletableFuture<K> send(T request, Class<K> responseCLass) {
        CompletableFuture<K> completableFuture = new CompletableFuture<>();

        awaitingRequestsClasses.put(request.getRequestId().toString(), responseCLass);
        awaitingRequests.put(request.getRequestId().toString(), (data) -> {
            completableFuture.complete((K) data);
        });

        JSONObject requestObject = request.writeRequest();
        Jedis jedis = new Jedis();
        jedis.publish(channelId, requestObject.toString());

        return completableFuture;
    }

}
