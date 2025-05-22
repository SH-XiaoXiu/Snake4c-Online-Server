package cn.xiuxius.snake.packet.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.packet.ServerBoundPacket;
import cn.xiuxius.snake.packet.ServerBoundType;

public class ServerBoundPlayerInitPacket extends ServerBoundPacket {

    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public ServerBoundPlayerInitPacket(String playerName) {
        this.playerName = playerName;
        setType(ServerBoundType.INIT);
    }

    @Override
    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("type", getType().name());
        obj.set("playerName", playerName);
        return obj.toJSONString(0);
    }

    @Override
    public JSONObject jsonObj() {
        return JSONUtil.parseObj(this);
    }

}
