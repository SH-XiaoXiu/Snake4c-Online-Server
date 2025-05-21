package cn.xiuxius.snake.packet;

public enum ClientBoundType {
    FRAME,
    NONE,
    INIT,
    DIR,
    MOVE,
    JOIN,
    EXIT,
    SYNC,
    FOOD_CREATE,
    FOOD_EATEN
}
