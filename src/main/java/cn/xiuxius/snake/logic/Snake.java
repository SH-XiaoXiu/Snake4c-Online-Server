package cn.xiuxius.snake.logic;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Snake {
    private final Deque<Point> body = new LinkedList<>();
    private Direction direction;

    public Snake(Point start) {
        body.addFirst(start);
        direction = Direction.RIGHT;
    }

    public void setDirection(Direction dir) {
        // 防止180度反向转弯
        if ((direction == Direction.UP && dir == Direction.DOWN) ||
                (direction == Direction.DOWN && dir == Direction.UP) ||
                (direction == Direction.LEFT && dir == Direction.RIGHT) ||
                (direction == Direction.RIGHT && dir == Direction.LEFT)) {
            return;
        }
        direction = dir;
    }

    public Direction getDirection() {
        return direction;
    }

    public Point getHead() {
        return body.peekFirst();
    }

    public List<Point> getBody() {
        return List.copyOf(body);
    }

    public Point move() {
        Point move = getHead().move(direction);
        body.addFirst(move);
        body.removeLast();
        return move;
    }

    public void grow() {
        Point tail = body.getLast();
        body.addLast(new Point(tail));
    }

}
