package cn.xiuxius.snake.packet.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.logic.Direction;
import cn.xiuxius.snake.packet.ServerBoundPacket;
import cn.xiuxius.snake.packet.ServerBoundType;

public class ServerBoundPlayerSyncPacket extends ServerBoundPacket {

    private String playerId;


    public ServerBoundPlayerSyncPacket(String playerId) {
        this.playerId = playerId;
        setType(ServerBoundType.SYNC);
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("type", getType().name());
        obj.set("playerId", playerId);
        return obj.toJSONString(0);
    }

    @Override
    public JSONObject jsonObj() {
        return JSONUtil.parseObj(this);
    }

}
