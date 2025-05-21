package cn.xiuxius.snake.packet.client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.logic.Player;
import cn.xiuxius.snake.logic.Point;
import cn.xiuxius.snake.packet.ClientBoundPacket;
import cn.xiuxius.snake.packet.ClientBoundType;

import java.util.ArrayList;
import java.util.List;

public class ClientBoundPlayerJoinPacket extends ClientBoundPacket {



    private final Player player;

    private final int nowPlayerCount;

    public ClientBoundPlayerJoinPacket(Player player, int nowPlayerCount) {
        setType(ClientBoundType.JOIN);
        this.player = player;
        this.nowPlayerCount = nowPlayerCount;
    }

    @Override
    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("index", getIndex());
        obj.set("timestamp", getTimestamp());
        obj.set("type", getType().name());
        obj.set("player", player.toSyncPlayer());
        obj.set("playersCount", nowPlayerCount);
        return obj.toJSONString(0);
    }

    @Override
    public JSONObject jsonObj() {
        return JSONUtil.parseObj(this);
    }

    public Player getPlayer() {
        return player.toSyncPlayer();
    }
    public int getPlayersCount() {
        return nowPlayerCount;
    }

}
