package cn.xiuxius.snake.logic;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xiuxius.snake.packet.ServerBoundType;
import cn.xiuxius.snake.packet.client.ClientBoundPlayerJoinPacket;
import cn.xiuxius.snake.packet.server.ServerBoundPlayerExitPacket;
import cn.xiuxius.snake.packet.server.ServerBoundPlayerInitPacket;
import cn.xiuxius.snake.packet.server.ServerBoundPlayerMovePacket;
import cn.xiuxius.snake.packet.server.ServerBoundPlayerSyncPacket;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class GameHandler extends SimpleChannelInboundHandler<String> {

    private static final GameRoom gameRoom = new GameRoom();

    private String playerId;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        playerId = UUID.randomUUID().toString();
        Player player = gameRoom.addPlayer(playerId, ctx.channel());
        ClientBoundPlayerJoinPacket joinPacket = new ClientBoundPlayerJoinPacket(player, gameRoom.getPlayers().size());
        getGameRoom().getPackets().offer(joinPacket);
        log.info("Client connected: {} ", ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof java.net.SocketException && cause.getMessage().equals("Connection reset")) {
            log.info("Client reset: {} ", ctx.channel().remoteAddress());
        } else {
            log.error("Exception caught: {}", cause.getMessage());
        }
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        handleMsg(ctx, msg);
    }

    private void handleMsg(ChannelHandlerContext ctx, String msg) {
        //去掉\n
        msg = msg.replace("\n", "");

        JSONObject jsonPacket = JSONUtil.parseObj(msg);

        int index = jsonPacket.getInt("index", -1);

        long timestamp = jsonPacket.getLong("timestamp", -1L);

        ServerBoundType type = ServerBoundType.valueOf((String) jsonPacket.getOrDefault("type", ServerBoundType.NONE));

        if (index == -1 || timestamp == -1) {
            return;
        }

        if (type == ServerBoundType.INIT) {
            ServerBoundPlayerInitPacket packet = new ServerBoundPlayerInitPacket(playerId);
            packet.setIndex(index);
            packet.setTimestamp(timestamp);
            gameRoom.handlePlayerInitPacket(ctx, packet);
            return;
        }

        Player player = gameRoom.getChannels().get(ctx.channel().id());

        if (player == null) {
            return;
        }

        playerId = player.getId();

        if (type == ServerBoundType.SYNC) {
            ServerBoundPlayerSyncPacket packet = new ServerBoundPlayerSyncPacket(playerId);
            packet.setIndex(index);
            packet.setTimestamp(timestamp);
            gameRoom.handlePlayerPacket(playerId, packet);
            return;
        }

        if (type == ServerBoundType.ESC) {
            ServerBoundPlayerExitPacket packet = new ServerBoundPlayerExitPacket(playerId);
            packet.setIndex(index);
            packet.setTimestamp(timestamp);
            gameRoom.handlePlayerPacket(playerId, packet);
            return;
        }

        if (type == ServerBoundType.DIR) {
            Direction direction = Direction.valueOf((String) jsonPacket.getOrDefault("direction", Direction.NONE));
            ServerBoundPlayerMovePacket packet = new ServerBoundPlayerMovePacket(playerId, direction);
            packet.setIndex(index);
            packet.setTimestamp(timestamp);
            gameRoom.handleInput(packet);
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        gameRoom.removePlayer(playerId);
        log.info("Client disconnected: {} ", ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    public static GameRoom getGameRoom() {
        return GameHandler.gameRoom;
    }
}
