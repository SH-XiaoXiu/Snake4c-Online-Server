package cn.xiuxius.snake.packet.client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.packet.ClientBoundType;
import cn.xiuxius.snake.logic.Point;
import cn.xiuxius.snake.packet.ClientBoundPacket;

public class ClientBoundFoodEatenPacket extends ClientBoundPacket {

    private final Point point;

    private final String playerId;

    public ClientBoundFoodEatenPacket(Point point, String playerId) {
        setType(ClientBoundType.FOOD_EATEN);
        this.point = point;
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("type", getType().name());
        obj.set("point", point);
        obj.set("playerId", playerId);
        obj.set("index", getIndex());
        obj.set("timestamp", getTimestamp());
        return obj.toJSONString(0);
    }

    @Override
    public JSONObject jsonObj() {
        return JSONUtil.parseObj(this);
    }

    public Point getPoint() {
        return point;
    }

}
