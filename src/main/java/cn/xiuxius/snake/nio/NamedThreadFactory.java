package cn.xiuxius.snake.nio;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private final String baseName;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final ThreadFactory defaultFactory = java.util.concurrent.Executors.defaultThreadFactory();
    public NamedThreadFactory(String baseName) {
        this.baseName = baseName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = defaultFactory.newThread(r);
        t.setName(baseName + "-" + counter.incrementAndGet());
        return t;
    }
}
