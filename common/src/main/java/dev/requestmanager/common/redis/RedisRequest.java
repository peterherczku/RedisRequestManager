package dev.requestmanager.common.redis;

import com.google.gson.Gson;
import dev.requestmanager.common.redis.interfaces.RedisBody;
import dev.requestmanager.common.redis.interfaces.RedisRequestListener;
import dev.requestmanager.common.redis.interfaces.RedisResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class RedisRequest<T extends RedisBody, K extends RedisResponse> {

    protected UUID requestId;
    protected String requestName;
    protected Map<String, String> headers;
    protected T body;
    protected K response;

    public RedisRequest(String requestName) {
        this.requestName=requestName;
    }

    public RedisRequest(String requestName, Map<String, String> headers, T body) {
        this.requestId=UUID.randomUUID();
        this.requestName=requestName;
        this.headers=headers;
        this.body=body;
    }

    public void readRequest(JSONObject jsonObject) {
        Gson gson = new Gson();
        this.requestId=UUID.fromString(jsonObject.getString("requestId"));
        this.headers=new HashMap<>();
        JSONArray headerArray = jsonObject.getJSONArray("headers");
        for (Object o : headerArray) {
            JSONObject headerObj = (JSONObject) o;
            headers.put(headerObj.getString("name"), headerObj.getString("value"));
        }
        this.body=gson.fromJson(jsonObject.getJSONObject("body").toString(), (Class<T>)
                ((ParameterizedType)getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0]);
    }

    public JSONObject writeRequest() {
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();
        JSONArray headerArray = new JSONArray();

        jsonObject.put("requestId", requestId.toString());
        jsonObject.put("requestName", requestName);
        this.headers.forEach((name, value) -> {
            JSONObject headerObject = new JSONObject();
            headerObject.put("name", name);
            headerObject.put("value", value);
            headerArray.put(headerObject);
        });

        jsonObject.put("headers", headerArray);
        JSONObject bodyObject = new JSONObject(gson.toJson(this.body));
        jsonObject.put("body", bodyObject);
        return jsonObject;
    }

    public abstract void processRequest(RedisRequestListener listener);

    public UUID getRequestId() {
        return requestId;
    }

    public String getRequestName() {
        return requestName;
    }

    public K getResponse() {
        return response;
    }

    public String getHeader(String name) {
        return this.headers.get(name);
    }

    public T getBody() {
        return body;
    }

    public void setResponse(K response) {
        this.response = response;
    }
}
