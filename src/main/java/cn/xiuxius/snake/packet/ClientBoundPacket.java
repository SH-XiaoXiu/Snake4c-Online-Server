package cn.xiuxius.snake.packet;

public abstract class ClientBoundPacket extends AbstractPacket {

    private ClientBoundType type = ClientBoundType.NONE;

    public ClientBoundPacket() {
    }

    @Override
    public abstract String json();


    public ClientBoundType getType() {
        return type;
    }

    public void setType(ClientBoundType type) {
        this.type = type;
    }


}
