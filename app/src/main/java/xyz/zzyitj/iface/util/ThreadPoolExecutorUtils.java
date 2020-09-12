package xyz.zzyitj.iface.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * xyz.intent.pt.magician.util
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/8/2 18:14
 * @since 1.0
 */
public class ThreadPoolExecutorUtils {
    private static volatile ThreadPoolExecutor threadPoolExecutor;
    private static final float LOAD_FACTOR = 0.8F;
    private static final int N_CPU = Runtime.getRuntime().availableProcessors();
    /**
     * 因为是IO密集型，根据规则设置为
     * 线程数 = CPU核心数/(1-阻塞系数) 这个阻塞系数一般为0.8~0.9之间，也可以取0.8或者0.9。
     */
    public static final int CORE_POOL_SIZE = (int) (N_CPU / (1 - LOAD_FACTOR));
    public static final int MAX_POOL_SIZE = (int) (N_CPU / (LOAD_FACTOR / 2));
    public static final long KEEP_ALIVE_TIME = 0;

    private ThreadPoolExecutorUtils() {
    }

    public static ThreadPoolExecutor getInstance() {
        if (threadPoolExecutor == null) {
            synchronized (ThreadPoolExecutorUtils.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                            MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                            new ArrayBlockingQueue<>(CORE_POOL_SIZE),
                            new CustomThreadFactory(),
                            new ThreadPoolExecutor.CallerRunsPolicy());
                }
            }
        }
        return threadPoolExecutor;
    }

    /**
     * 线程工厂
     */
    public static class CustomThreadFactory implements ThreadFactory {

        public CustomThreadFactory() {
        }

        private final AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            // 设置线程名称
            t.setName("Pool" + count.addAndGet(1));
            // 设置线程为守护进程
            t.setDaemon(true);
            return t;
        }
    }
}
