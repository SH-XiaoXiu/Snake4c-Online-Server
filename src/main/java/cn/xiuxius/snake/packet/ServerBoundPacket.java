package cn.xiuxius.snake.packet;

public abstract class ServerBoundPacket extends AbstractPacket {


    private ServerBoundType type = ServerBoundType.NONE;

    public ServerBoundPacket() {
    }


    @Override
    public abstract String json();


    public ServerBoundType getType() {
        return type;
    }

    public void setType(ServerBoundType type) {
        this.type = type;
    }
}
