package cn.xiuxius.snake.logic;

import io.netty.channel.Channel;

public class Player {
    private final String id;
    private String playerName;
    private final Channel channel;
    private Snake snake;

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    private Direction latestDirection;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Player(String id, Channel channel, Point startPos) {
        this.id = id;
        this.channel = channel;
        this.snake = new Snake(startPos);
        this.latestDirection = Direction.RIGHT;
    }

    public String getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }

    public Snake getSnake() {
        return snake;
    }

    public Direction getLatestDirection() {
        return latestDirection;
    }

    public void setLatestDirection(Direction latestDirection) {
        if (latestDirection == Direction.NONE) {
            return;
        }
        //不能掉头
        if ((latestDirection == Direction.UP && this.latestDirection == Direction.DOWN) ||
                (latestDirection == Direction.DOWN && this.latestDirection == Direction.UP) ||
                (latestDirection == Direction.LEFT && this.latestDirection == Direction.RIGHT) ||
                (latestDirection == Direction.RIGHT && this.latestDirection == Direction.LEFT)) {
            return;
        }
        this.latestDirection = latestDirection;
    }

    public Player toSyncPlayer() {
        Player player = new Player(this.id, null, this.snake.getHead());
        player.setSnake(this.snake);
        player.setPlayerName(this.playerName);
        return player;
    }

}
