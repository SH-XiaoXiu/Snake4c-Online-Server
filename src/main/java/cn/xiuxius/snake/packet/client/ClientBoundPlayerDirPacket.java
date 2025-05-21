package cn.xiuxius.snake.packet.client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.packet.ClientBoundType;
import cn.xiuxius.snake.logic.Direction;
import cn.xiuxius.snake.packet.ClientBoundPacket;

public class ClientBoundPlayerDirPacket extends ClientBoundPacket {

    private String playerId;

    private Direction direction;

    public ClientBoundPlayerDirPacket(String playerId, Direction direction) {
        setType(ClientBoundType.DIR);
        this.playerId = playerId;
        this.direction = direction;
    }

    @Override
    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("type", getType().name());
        obj.set("playerId", playerId);
        obj.set("direction", direction.name());
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


    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }


}
