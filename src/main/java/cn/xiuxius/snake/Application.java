package cn.xiuxius.snake;

import cn.xiuxius.snake.logic.GameHandler;
import cn.xiuxius.snake.logic.GameRoom;
import cn.xiuxius.snake.nio.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Application {

    public static final int GRID_SIZE = 25;

    public static final int CELL_SIZE = 35;

    public static final int TICK = 25;

    public static final int MOVE_INTERVAL = 10;

    public static final int DELTA_TIME = 16;

    public static final int MAX_PLAYERS = 20;

    public static void main(String[] args) throws Exception {
        log.info("Server started");
        EventLoopGroup bossGroup = new NioEventLoopGroup(4,new NamedThreadFactory("main"));
        EventLoopGroup workerGroup = new NioEventLoopGroup(new NamedThreadFactory("worker"));
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(
                                    // 最大包长 10MB，长度字段偏移0，长度字段4字节，长度调整0，跳过长度字段4字节
                                    new LengthFieldBasedFrameDecoder(10 * 1024 * 1024, 0, 4, 0, 4),
                                    new StringDecoder(StandardCharsets.UTF_8),
                                    new LengthFieldPrepender(4),  // 发送数据时加4字节长度头
                                    new StringEncoder(StandardCharsets.UTF_8),
                                    new GameHandler()
                            );
                        }
                    });
            GameRoom gameRoom = GameHandler.getGameRoom();
            log.info("Game Room Initialized");
            ScheduledExecutorService gameLoopExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("game"));
            gameLoopExecutor.scheduleAtFixedRate(() -> {
                long startTime = System.currentTimeMillis();
                try {
                    gameRoom.gameTick();
                } catch (Exception e) {
                    log.error("Game Loop Error", e);
                }
                long cost = System.currentTimeMillis() - startTime;
                if (cost > Application.DELTA_TIME + 5) {
                    log.warn("Game Loop Cost: {}ms", cost);
                }
            }, 0, Application.DELTA_TIME, TimeUnit.MILLISECONDS);

            ChannelFuture f = bootstrap.bind(8080).sync();
            log.info("Server started at {}", 8080);
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
