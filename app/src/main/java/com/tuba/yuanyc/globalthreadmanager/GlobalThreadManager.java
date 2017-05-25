package com.tuba.yuanyc.globalthreadmanager;

import android.text.TextUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author ：yuanyc
 * Time ：2017/5/25
 * Description ：线程统一管理类
 * 对GlobalSimpleThreadManager进行的优化，此处使用ThreadPoolExecutor让使用者自己去创建自己想要的线程池
 */

public class GlobalThreadManager {
    /**
     * 自定义的线程池
     */
    private static ThreadPoolExecutor threadPoolExecutor;

    private GlobalThreadManager() {
    }

    private static class InstanceHolder {
        private static final GlobalThreadManager instace = new GlobalThreadManager();
    }

    /**
     * 获取GlobalThreadManager实例（采用静态内部实现延迟加载实例）
     *
     * @return
     */
    public static GlobalThreadManager getInstace() {
        return InstanceHolder.instace;
    }

    /**
     * 外部调用执行
     * @param threadName 自定义的线程的名字
     * @param runnable
     */
    public void execute(String threadName, Runnable runnable) {
        RunnableWrapper runnableWrapper = new RunnableWrapper(threadName, runnable);
        if (null == threadPoolExecutor) {
            ThreadPoolSetting threadPoolSetting = new ThreadPoolSetting();
            threadPoolSetting.build();
        }
        threadPoolExecutor.execute(runnableWrapper);
    }

    /**
     * Runnable包装类（因为只有在线程run起来之后才能设置线程的名称）
     */
    private class RunnableWrapper implements Runnable {
        private String name;
        private Runnable runnable;

        public RunnableWrapper(String name, Runnable runnable) {
            this.name = name;
            this.runnable = runnable;
        }

        @Override
        public void run() {
            runnable.run();
            //在调用run方法之后调用
            setThreadName(name);
        }

        private void setThreadName(String threadName) {
            if (TextUtils.isEmpty(threadName)) {
                throw new RuntimeException("必须给Thread线程设置name");
            }
            Thread.currentThread().setName(threadName);
            System.out.println("前线程的名称： " + Thread.currentThread().getName());
        }
    }


    public static class ThreadPoolSetting {
        /**
         * 核心线程数
         */
        private int corePoolSize = Configs.THREAD_CORE_NUMBER;
        /**
         * 最大线程数
         */
        private int maximumPoolSize = Configs.THREAD_MAX_NUMBER;
        /**
         * 阻塞队列的大小
         */
        private int blockSize = Configs.THREAD_BLOCK_SIZE;
        /**
         * 空闲线程超时时间
         */
        private long keepAliveTime = Configs.THREAD_IDLE_TIME_OUT;
        /**
         * 时间单位
         * 默认的单位：毫秒
         */
        private TimeUnitEnum timeUnitEnum = TimeUnitEnum.MILLISECONDS;
        /**
         * 阻塞队列
         * 默认：ArrayBlockingQueue（有界队列）
         */
        private BlockingQueueEnum blockingQueueEnum = BlockingQueueEnum.ARRAY_BLOCKING_QUEUE;

        /**
         * 设置线程池核心线程数量
         *
         * @param corePoolSize
         */
        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        /**
         * 设置线程池最大线程数量
         *
         * @param maximumPoolSize
         */
        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        /**
         * 设置阻塞队列的大小
         *
         * @param blockSize
         */
        public void setBlockSize(int blockSize) {
            this.blockSize = blockSize;
        }

        /**
         * 设置空闲线程超时时间
         *
         * @param keepAliveTime
         */
        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }


        /**
         * 设置时间单位
         *
         * @param timeUnitEnum 单位：默认的单位是毫秒
         * @see TimeUnitEnum#MILLISECONDS
         * @see TimeUnitEnum#SECONDS
         */
        public void setTimeUnitEnum(TimeUnitEnum timeUnitEnum) {
            this.timeUnitEnum = timeUnitEnum;
        }

        /**
         * 设置阻塞的队列
         *
         * @param blockingQueueEnum
         */
        public void setBlockingQueueEnum(BlockingQueueEnum blockingQueueEnum) {
            this.blockingQueueEnum = blockingQueueEnum;
        }

        public ThreadPoolSetting() {

        }

        public void build() {
            TimeUnit timeUnit = checkTimeUnit(timeUnitEnum);
            BlockingQueue blockingQueue = checkBlockingQueue(blockingQueueEnum);
            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, blockingQueue);
            System.out.println("核心线程数corePoolSize = "+corePoolSize);
            System.out.println("最大线程数maximumPoolSize = "+maximumPoolSize);
            System.out.println("阻塞队列大小blockSize = "+blockSize);
            System.out.println("时间单位timeUnitEnum = "+timeUnitEnum);
            System.out.println("空闲线程超时时间keepAliveTime = "+keepAliveTime);
            System.out.println("当前选中的阻塞队列blockingQueueEnum = "+blockingQueueEnum);
        }

        private TimeUnit checkTimeUnit(TimeUnitEnum timeUnitEnum) {
            TimeUnit timeUnit;
            switch (timeUnitEnum) {
                case MILLISECONDS:
                    timeUnit = TimeUnit.MILLISECONDS;
                    break;
                case SECONDS:
                    timeUnit = TimeUnit.SECONDS;
                    break;
                default:
                    timeUnit = TimeUnit.MILLISECONDS;
            }
            return timeUnit;
        }

        private BlockingQueue checkBlockingQueue(BlockingQueueEnum blockingQueueEnum) {
            BlockingQueue blockingQueue;
            switch (blockingQueueEnum) {
                case ARRAY_BLOCKING_QUEUE:
                    blockingQueue = new ArrayBlockingQueue(blockSize);
                    break;
                case DELAY_QUEUE:
                    blockingQueue = new DelayQueue();
                    break;
                case LINKED_BLOCKING_DEQUE:
                    blockingQueue = new LinkedBlockingDeque();
                    break;
                case LINKED_BLOCKING_QUEUE:
                    blockingQueue = new LinkedBlockingQueue();
                    break;
                case PRIORITY_BLOCKING_QUEUE:
                    blockingQueue = new PriorityBlockingQueue();
                    break;
                case SYNCHRONOUS_QUEUE:
                    blockingQueue = new SynchronousQueue();
                    break;
                default:
                    blockingQueue = new ArrayBlockingQueue(blockSize);
            }
            return blockingQueue;
        }
    }

    public enum TimeUnitEnum {
        /**
         * 毫秒
         */
        MILLISECONDS, /**
         * 秒
         */
        SECONDS;
    }

    public enum BlockingQueueEnum {
        ARRAY_BLOCKING_QUEUE, DELAY_QUEUE, LINKED_BLOCKING_DEQUE, LINKED_BLOCKING_QUEUE, PRIORITY_BLOCKING_QUEUE, SYNCHRONOUS_QUEUE;
    }


}
