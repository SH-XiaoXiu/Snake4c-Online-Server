package cn.xiuxius.snake.packet.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.logic.Direction;
import cn.xiuxius.snake.packet.ServerBoundType;
import cn.xiuxius.snake.packet.ServerBoundPacket;
import cn.xiuxius.snake.packet.client.ClientBoundPlayerDirPacket;

public class ServerBoundPlayerMovePacket extends ServerBoundPacket {

    private final String playerId;

    private final Direction direction;

    public ServerBoundPlayerMovePacket(String playerId, Direction direction) {
        this.playerId = playerId;
        setType(ServerBoundType.DIR);
        this.direction = direction;
    }

    @Override
    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("type", getType().name());
        obj.set("direction", direction.name());
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



    public Direction getDirection() {
        return direction;
    }


    public ClientBoundPlayerDirPacket toClientBoundPlayerInputPacket(int index) {
        ClientBoundPlayerDirPacket packet = new ClientBoundPlayerDirPacket(playerId, direction);
        packet.setIndex(index);
        packet.setIndex(index);
        return packet;
    }

}
