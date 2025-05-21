package cn.xiuxius.snake.packet;

public abstract class AbstractPacket implements IPacket {

    private int index;
    private long timestamp;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
