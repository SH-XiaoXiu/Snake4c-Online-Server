package cn.xiuxius.snake.packet.client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.packet.ClientBoundType;
import cn.xiuxius.snake.logic.Point;
import cn.xiuxius.snake.packet.ClientBoundPacket;

public class ClientBoundFoodCreatePacket extends ClientBoundPacket {

    private final Point point;

    public ClientBoundFoodCreatePacket(Point point) {
        setType(ClientBoundType.FOOD_CREATE);
        this.point = point;
    }

    @Override
    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("type", getType().name());
        obj.set("point", point);
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
