package dev.requestmanager.common.redis;

import com.google.gson.Gson;
import dev.requestmanager.common.redis.interfaces.RedisRequestListener;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RequestServer {

    private final String channelId;
    private ConcurrentMap<String, RedisRequest> requests = new ConcurrentHashMap<>();
    private RedisRequestListener listener;

    public RequestServer(String channelId, RedisRequestListener listener) {
        this.channelId=channelId;
        this.listener=listener;
    }

    public void init() {
        subscribeToChannel();

        try {
            Thread.sleep(1000);
            System.out.println("Server initialized.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerRequest(RedisRequest request) {
        requests.put(request.getRequestName(), request);
    }

    public void handleIncomingMessage(JSONObject message) {
        String requestName = message.getString("requestName");
        RedisRequest request = requests.get(requestName);
        request.readRequest(message);
        request.processRequest(listener);

        JSONObject response = new JSONObject();
        response.put("requestName", requestName);
        response.put("requestId", request.getRequestId());
        response.put("response", true);

        Gson gson = new Gson();
        JSONObject responseBody = new JSONObject(gson.toJson(request.getResponse()));
        response.put("body", responseBody);

        Jedis jedis = new Jedis();
        jedis.publish(channelId, response.toString());
    }

    public void subscribeToChannel() {
        Thread thread = new Thread(() -> {
            Jedis jedis = new Jedis();
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    JSONObject messageObject = new JSONObject(message);
                    if (messageObject.has("response")) return;
                    handleIncomingMessage(messageObject);
                }
            }, channelId);
            System.out.println("Subscribing");
        });
        thread.start();
    }

}
