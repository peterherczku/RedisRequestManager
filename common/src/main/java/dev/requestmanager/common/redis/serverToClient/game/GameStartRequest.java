package dev.requestmanager.common.redis.serverToClient.game;

import dev.requestmanager.common.redis.interfaces.RedisRequestBody;
import dev.requestmanager.common.redis.RedisRequest;
import dev.requestmanager.common.redis.interfaces.RedisRequestListener;
import dev.requestmanager.common.redis.interfaces.listener.ServerToClientListener;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameStartRequest extends RedisRequest<GameStartRequest.RequestBody, GameStartResponse> {

    public GameStartRequest() {
        super("GAME_START");
    }

    public GameStartRequest(Map<String, String> headers, RequestBody body) {
        super("GAME_START", headers, body);
    }

    @Override
    public void processRequest(RedisRequestListener listener) {
        ((ServerToClientListener) listener).processGameStart(this);
        /*this.body.uuids.forEach(uuid -> {
            System.out.println(uuid.toString());
        });

        this.response=new GameStartResponse();
        this.response.setSuccess(true);*/
    }

    public static class RequestBody implements RedisRequestBody {

        private List<UUID> uuids;

        public RequestBody(List<UUID> uuids) {
            this.uuids=uuids;
        }

        public List<UUID> getUuids() {
            return uuids;
        }

        public void setUuids(List<UUID> uuids) {
            this.uuids = uuids;
        }
    }

}
