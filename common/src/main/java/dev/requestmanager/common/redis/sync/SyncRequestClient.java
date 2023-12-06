package dev.requestmanager.common.redis.sync;

import com.google.gson.Gson;
import dev.requestmanager.common.redis.RedisRequest;
import dev.requestmanager.common.redis.interfaces.RedisResponse;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.*;


public class SyncRequestClient {

    private String channelId;
    private ConcurrentMap<String, JSONObject> awaitingRequests = new ConcurrentHashMap<>();

    public SyncRequestClient(String channelId) {
        this.channelId=channelId;
    }

    public void init() {
        connectToChannel();
        try {
            System.out.println("Sleeping 1000ms");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleIncomingResponse(JSONObject message) {
        String requestId = message.getString("requestId");
        awaitingRequests.replace(requestId, message.getJSONObject("body"));
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

    public <T extends RedisRequest, K extends RedisResponse> K send(T request, Class<K> responseCLass) {
        JSONObject requestObject = request.writeRequest();
        Jedis jedis = new Jedis();
        jedis.publish(channelId, requestObject.toString());
        awaitingRequests.put(request.getRequestId().toString(), new JSONObject());

        CompletableFuture<K> completableFuture = new CompletableFuture<>();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (!awaitingRequests.get(request.getRequestId().toString()).isEmpty()) {
                Gson gson = new Gson();
                JSONObject responseObject = awaitingRequests.get(request.getRequestId().toString());
                K response = gson.fromJson(responseObject.toString(), responseCLass);
                completableFuture.complete(response);
                awaitingRequests.remove(request.getRequestId().toString());
                scheduledExecutorService.shutdown();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

        try {
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
