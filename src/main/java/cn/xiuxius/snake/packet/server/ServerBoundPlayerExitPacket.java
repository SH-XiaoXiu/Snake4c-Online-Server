package cn.xiuxius.snake.packet.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.logic.Direction;
import cn.xiuxius.snake.packet.ServerBoundType;
import cn.xiuxius.snake.packet.ServerBoundPacket;

public class ServerBoundPlayerExitPacket extends ServerBoundPacket {

    private final String playerId;

    public ServerBoundPlayerExitPacket(String playerId) {
        this.playerId = playerId;
        setType(ServerBoundType.ESC);
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

    public String getPlayerId() {
        return playerId;
    }

}
