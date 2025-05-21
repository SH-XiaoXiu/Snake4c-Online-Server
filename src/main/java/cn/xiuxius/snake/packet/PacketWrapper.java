package cn.xiuxius.snake.packet;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.List;
import java.util.Queue;

public class PacketWrapper {

    private final ClientBoundType type = ClientBoundType.FRAME;
    private int index;
    private long timestamp;
    private List<IPacket> packets;

    public String json() {
        JSONObject obj = JSONUtil.createObj();
        obj.set("type", type.name());
        obj.set("index", index);
        obj.set("timestamp", timestamp);
        obj.set("frames", packets);
        return obj.toJSONString(0);
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public PacketWrapper() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ClientBoundType getType() {
        return type;
    }

    public List<IPacket> getPackets() {
        return packets;
    }

    public void setPackets(List<IPacket> packets) {
        this.packets = packets;
    }

    public PacketWrapper(int index, long timestamp, List<IPacket> packets) {
        this.index = index;
        this.timestamp = timestamp;
        this.packets = packets;
    }
}
