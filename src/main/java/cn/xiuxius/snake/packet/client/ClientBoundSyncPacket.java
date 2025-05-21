package cn.xiuxius.snake.packet.client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.Application;
import cn.xiuxius.snake.logic.Player;
import cn.xiuxius.snake.logic.Point;
import cn.xiuxius.snake.logic.Snake;
import cn.xiuxius.snake.packet.ClientBoundPacket;
import cn.xiuxius.snake.packet.ClientBoundType;

import java.util.ArrayList;
import java.util.List;

public class ClientBoundSyncPacket extends ClientBoundPacket {


    private final int playersCount;

    private final List<Player> players = new ArrayList<>();

    private final Player me;

    private final Point nowFood;

    private final int index;

    private final int tickCounter;

    public ClientBoundSyncPacket(int playersCount, Point nowFood, Player me, List<Player> players, int index, int tickCounter) {
        setType(ClientBoundType.SYNC);
        this.playersCount = playersCount;
        this.nowFood = nowFood;
        this.index = index;
        this.me = me;
        this.players.addAll(players);
        this.tickCounter = tickCounter;
    }


    @Override
    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("index", getIndex());
        obj.set("timestamp", getTimestamp());
        obj.set("type", getType().name());
        obj.set("playersCount", playersCount);
        obj.set("players", players.stream().map(Player::toSyncPlayer).toArray());
        obj.set("me", me.toSyncPlayer());
        obj.set("nowFood", nowFood);
        obj.set("moveInterval", tickCounter);
        return obj.toJSONString(0);
    }

    @Override
    public JSONObject jsonObj() {
        return JSONUtil.parseObj(this);
    }

    @Override
    public int getIndex() {
        return index;
    }
}
