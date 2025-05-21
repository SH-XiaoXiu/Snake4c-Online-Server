package cn.xiuxius.snake.packet.client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.logic.Point;
import cn.xiuxius.snake.packet.ClientBoundPacket;
import cn.xiuxius.snake.packet.ClientBoundType;

public class ClientBoundNonePacket extends ClientBoundPacket {

    public ClientBoundNonePacket() {
        setType(ClientBoundType.NONE);
    }

    @Override
    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("type", getType().name());
        return obj.toJSONString(0);
    }

    @Override
    public JSONObject jsonObj() {
        return JSONUtil.parseObj(this);
    }

}
