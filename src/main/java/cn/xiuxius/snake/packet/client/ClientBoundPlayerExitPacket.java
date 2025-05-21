package cn.xiuxius.snake.packet.client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.packet.ClientBoundType;
import cn.xiuxius.snake.packet.ClientBoundPacket;

public class ClientBoundPlayerExitPacket extends ClientBoundPacket {

    private String playerId;

    private final int nowPlayerCount;

    public ClientBoundPlayerExitPacket(String playerId, int nowPlayerCount) {
        this.nowPlayerCount = nowPlayerCount;
        setType(ClientBoundType.EXIT);
        this.playerId = playerId;
    }

    @Override
    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("type", getType().name());
        obj.set("playerId", playerId);
        obj.set("nowPlayerCount", nowPlayerCount);
        return obj.toJSONString(0);
    }

    @Override
    public JSONObject jsonObj() {
        return JSONUtil.parseObj(this);
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }



}
