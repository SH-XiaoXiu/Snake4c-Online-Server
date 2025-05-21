package cn.xiuxius.snake.logic;

import cn.xiuxius.snake.Application;
import cn.xiuxius.snake.packet.IPacket;
import cn.xiuxius.snake.packet.PacketWrapper;
import cn.xiuxius.snake.packet.ServerBoundPacket;
import cn.xiuxius.snake.packet.ServerBoundType;
import cn.xiuxius.snake.packet.client.*;
import cn.xiuxius.snake.packet.server.ServerBoundPlayerInitPacket;
import cn.xiuxius.snake.packet.server.ServerBoundPlayerMovePacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class GameRoom {

    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private final Map<ChannelId, Player> channels = new ConcurrentHashMap<>();
    private static final ClientBoundNonePacket NONE_PACKET = new ClientBoundNonePacket();
    private Point food = new Point(10, 10);
    private int currentFrame = 0;
    private final Queue<IPacket> packets = new ConcurrentLinkedQueue<>();
    private int tickCounter = 0;
    private final Random rand = new Random();

    public Map<ChannelId, Player> getChannels() {
        return channels;
    }

    public Queue<IPacket> getPackets() {
        return packets;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public Player addPlayer(String playerId, Channel ch) {
        log.info("Player joined: ip:{} id:{}", ch.remoteAddress(), playerId);
        // 初始位置简单放中心附近随机点
        Point start = new Point(5 + players.size() * 3, 5);
        log.info("Start Point: x:{} y:{}", start.getX(), start.getY());
        Player player = new Player(playerId, ch, start);
        players.put(playerId, player);
        channels.put(ch.id(), player);
        return player;
    }

    public void removePlayer(String playerId) {
        players.remove(playerId);
    }


    public void handlePlayerInitPacket(ChannelHandlerContext ctx, ServerBoundPlayerInitPacket packet) {
        if (packet.getType() == ServerBoundType.INIT) {
            Player player = channels.get(ctx.channel().id());
            if (player == null) return;
            log.info("Player {} send INIT packet", player.getId());
            ClientBoundInitPacket p = new ClientBoundInitPacket(player, player.getSnake());
            sendToPlayer(player, p);
        }
    }

    public void handlePlayerPacket(String playerId, ServerBoundPacket packet) {
        Player player = players.get(playerId);
        if (player == null) return;
        if (packet.getType() == ServerBoundType.SYNC) {
            log.info("Player {} send SYNC packet", player.getId());
            List<Player> syncPlayers = players.values().stream().map(Player::toSyncPlayer).toList();
            ClientBoundSyncPacket p = new ClientBoundSyncPacket(players.size(), food, player, syncPlayers, currentFrame, tickCounter);
            p.setTimestamp(System.currentTimeMillis());
            sendToPlayer(player, p);
        }
        if (packet.getType() == ServerBoundType.ESC) {
            log.info("Player {} send ESC packet", player.getId());
            ClientBoundPlayerExitPacket p = new ClientBoundPlayerExitPacket(player.getId(), players.size() - 1);
            packets.offer(p);
            player.getChannel().close();
        }
    }

    public void handleInput(ServerBoundPlayerMovePacket packet) {
        Player player = players.get(packet.getPlayerId());
        if (player == null) return;
        try {
            if (packet.getType() == ServerBoundType.DIR) {
                if (player.getLatestDirection() == packet.getDirection()) {
                    return;
                }
                log.info("Player {} send DIR:{} packet", player.getId(), packet.getDirection());
                player.setLatestDirection(packet.getDirection());
                packets.offer(packet.toClientBoundPlayerInputPacket(currentFrame));
            }

        } catch (Exception ignored) {
        }
    }

    public void gameTick() {
        currentFrame++;
        tickCounter++;
        log.debug("Tick: {}", currentFrame);
        if (tickCounter >= Application.MOVE_INTERVAL) {
            //先移动所有蛇，判断是否吃到食物 //撞墙就死
            for (Player player : players.values()) {
                Snake snake = player.getSnake();
                snake.setDirection(player.getLatestDirection());
                //正方形地图
                int mapSize = Application.GRID_SIZE;
                Point nextHead = snake.getHead().move(snake.getDirection());
                if (nextHead.getX() <= 0 || nextHead.getX() >= mapSize || nextHead.getY() <= 0 || nextHead.getY() >= mapSize) {
                    ClientBoundPlayerDirPacket p = new ClientBoundPlayerDirPacket(player.getId(), Direction.NONE);
                    Direction nextDirection = Direction.NONE;
                    switch (snake.getDirection()) {
                        case UP -> {
                            nextDirection = Direction.RIGHT;
                        }
                        case DOWN -> {
                            nextDirection = Direction.LEFT;
                        }
                        case LEFT -> {
                            nextDirection = Direction.UP;
                        }
                        case RIGHT -> {
                            nextDirection = Direction.DOWN;
                        }
                    }
                    player.setLatestDirection(nextDirection);
                    snake.setDirection(nextDirection);
                    p.setDirection(nextDirection);
                    p.setIndex(currentFrame);
                    p.setTimestamp(System.currentTimeMillis());
                    packets.offer(p);
                }
                Point move = snake.move();
                if (move.equals(food)) {
                    log.info("Player {} eat food", player.getId());
                    snake.grow();
                    ClientBoundFoodEatenPacket foodEatenPacket = new ClientBoundFoodEatenPacket(food, player.getId());
                    foodEatenPacket.setIndex(currentFrame);
                    foodEatenPacket.setTimestamp(System.currentTimeMillis());
                    packets.offer(foodEatenPacket);
                    food = generateFood();
                    ClientBoundFoodCreatePacket foodCreatePacket = new ClientBoundFoodCreatePacket(food);
                    log.info("Food created: x:{} y:{}", food.getX(), food.getY());
                    foodCreatePacket.setIndex(currentFrame);
                    foodCreatePacket.setTimestamp(System.currentTimeMillis());
                    packets.offer(foodCreatePacket);
                }
            }
            tickCounter = 0;
        }
        netTick();
    }

    private Point generateFood() {
        int x = rand.nextInt(Application.GRID_SIZE - 2) + 1;
        int y = rand.nextInt(Application.GRID_SIZE - 2) + 1;
        return new Point(x, y);
    }

    public void netTick() {
        List<IPacket> framePackets = new ArrayList<>();
        IPacket pkt;
        while ((pkt = packets.poll()) != null) {
            framePackets.add(pkt);
        }
        if (framePackets.isEmpty()) {
            framePackets.add(NONE_PACKET);
        }
        PacketWrapper wrapper = new PacketWrapper();
        wrapper.setIndex(currentFrame);
        wrapper.setTimestamp(System.currentTimeMillis());
        wrapper.setPackets(framePackets);

        String data = wrapper.json();

        for (Player player : players.values()) {
            Channel ch = player.getChannel();
            if (ch != null && ch.isActive()) {
                ch.eventLoop().execute(() -> {
                    ch.writeAndFlush(data).addListener(future -> {
                        if (!future.isSuccess()) {
                            log.error("send fail - {}", player.getId(), future.cause());
                        }
                    });
                });
            }
        }
    }

    public static void sendToPlayer(Player player, IPacket packet) {
        Channel ch = player.getChannel();
        if (ch == null || !ch.isActive()) return;

        String data = packet.json();

        if (ch.eventLoop().inEventLoop()) {
            ch.writeAndFlush(data).addListener(future -> {
                if (!future.isSuccess()) {
                    log.error("send fail - {}", player.getId(), future.cause());
                }
            });
        } else {
            ch.eventLoop().execute(() -> {
                ch.writeAndFlush(data).addListener(future -> {
                    if (!future.isSuccess()) {
                        log.error("send fail - {}", player.getId(), future.cause());
                    }
                });
            });
        }
    }


    public Map<String, Player> getPlayers() {
        return players;
    }
}
