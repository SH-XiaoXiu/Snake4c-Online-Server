package cn.xiuxius.snake.packet.client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.Application;
import cn.xiuxius.snake.logic.Player;
import cn.xiuxius.snake.logic.Snake;
import cn.xiuxius.snake.packet.ClientBoundPacket;
import cn.xiuxius.snake.packet.ClientBoundType;

public class ClientBoundInitPacket extends ClientBoundPacket {

    private final Player me;

    private final Snake snake;

    public ClientBoundInitPacket(Player player, Snake snake) {
        setType(ClientBoundType.INIT);
        this.me = player;
        this.snake = snake;
    }

    @Override
    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("type", getType().name());
        int gridSize = Application.GRID_SIZE;
        obj.set("gridSize", gridSize);
        int cellSize = Application.CELL_SIZE;
        obj.set("cellSize", cellSize);
        int tick = Application.TICK;
        obj.set("tick", tick);
        int delay = Application.DELTA_TIME;
        obj.set("delay", delay);
        int moveInterval = Application.MOVE_INTERVAL;
        obj.set("moveInterval", moveInterval);
        int maxPlayers = Application.MAX_PLAYERS;
        obj.set("maxPlayers", maxPlayers);
        obj.set("me", me.toSyncPlayer());
        obj.set("index", getIndex());
        obj.set("snake", snake);
        return obj.toJSONString(0);
    }

    @Override
    public JSONObject jsonObj() {
        return JSONUtil.parseObj(this);
    }

}
