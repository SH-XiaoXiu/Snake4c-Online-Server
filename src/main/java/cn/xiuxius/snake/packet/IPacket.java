package cn.xiuxius.snake.packet;

import cn.hutool.json.JSONObject;

public interface IPacket {
    String json();
    JSONObject jsonObj();
    int getIndex();
    long getTimestamp();
}
