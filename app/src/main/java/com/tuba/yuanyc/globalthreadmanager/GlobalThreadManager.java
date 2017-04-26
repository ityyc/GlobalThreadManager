package com.tuba.yuanyc.globalthreadmanager;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>Author ：yuanyc </p>
 * <p>Time ：2017/3/4</p>
 * <p>Description ：线程统一管理类</p>
 * <p>使用步骤：</p>
 * <p>第一步：通过GlobalThreadManager.getInstance获取通过GlobalThreadManager实例</p>
 * <p>第二步：通过获取的实例调用setType设置要使用的线程池类型，不设置默认使用缓存线程池</p>
 * <p>第三步：调用execute方法，需传入当前线程的名字（你自定义的名字），要执行的runnable</p>
 * <p>特殊说明：如果创建的是一个守护线程，比如说使用的是缓存线程池，如果在池中已经创建了一个守护线程，当线程复用的时候，其他线程复用这个守护线程会导致本来是一个用户线程，因为复用变成了一个守护线程，所以在创建使用守护线程的时候，请不要使用此类(守护线程在android中使用没有多大的意义，建议不要将一个线程设置为守护线程)</p>
 */

public class GlobalThreadManager {
    /**
     * 线程池
     */
    private ExecutorService executorService;
    /**
     * 选择的线程池类型
     */
    private int type;
    /**
     * 线程的个数
     */
    private int threadNumber;

    /**
     * 设置线程的个数
     * <p>适用于</p>
     * <p>{@link Configs#FIXED_THREAD_POOL}</p>
     * <p>{@link Configs#SCHEDULED_THREAD_POOL}</p>
     *
     * @param threadNumber 线程的个数
     */
    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    /**
     * 设置线程池类型
     *
     * @param type 类型
     *             <p>{@link Configs#CACHE_THREAD_POOL}</p>
     *             <p>{@link Configs#FIXED_THREAD_POOL}</p>
     *             <p>{@link Configs#SCHEDULED_THREAD_POOL}</p>
     *             <p>{@link Configs#SINGLE_THREAD_POOL}</p>
     */
    public void setType(int type) {
        this.type = type;
    }

    private GlobalThreadManager() {

    }

    /**
     * 获取GlobalThreadManager实例
     *
     * @return 实例
     */
    public static GlobalThreadManager getInstance() {
        return InstanceHolder.instance;
    }

    private static final class InstanceHolder {
        public static final GlobalThreadManager instance = new GlobalThreadManager();
    }

    /**
     * 外部调用执行
     *
     * @param threadName 自定义的线程名字
     * @param runnable   要执行的runnable
     */
    public void execute(@NonNull String threadName, @NonNull Runnable runnable) {
        if (type == Configs.SCHEDULED_THREAD_POOL) {
            throw new RuntimeException("不适用于执行定时周期任务的线程池");
        }
        //包装runnable
        RunnableWrapper runnableWrapper = new RunnableWrapper(threadName, runnable);
        switch (type) {
            case Configs.CACHE_THREAD_POOL:
                executorService = Executors.newCachedThreadPool();
                executorService.execute(runnableWrapper);
                break;
            case Configs.FIXED_THREAD_POOL:
                executorService = Executors.newFixedThreadPool(checkThreadNumber(threadNumber) ? Configs.THREAD_NUMBER : threadNumber);
                executorService.execute(runnableWrapper);
                break;
            case Configs.SINGLE_THREAD_POOL:
                executorService = Executors.newSingleThreadExecutor();
                executorService.execute(runnableWrapper);
                break;
            default:
                executorService = Executors.newCachedThreadPool();
                executorService.execute(runnableWrapper);
        }
    }

    /**
     * 外部调用接口，适用于
     * <p>{@link Configs#SCHEDULED_THREAD_POOL}</p>
     * @param threadName 自定义的线程名字
     * @param runnable 要执行的runnable
     * @param delay 延迟的时间
     * @param unit 时间单位
     */
    public void schedule(@NonNull String threadName, @NonNull Runnable runnable, long delay, TimeUnit unit) {
        if (type != Configs.SCHEDULED_THREAD_POOL) {
            throw new RuntimeException("只适用于执行定时周期任务的线程池");
        }
        //包装runnable
        RunnableWrapper runnableWrapper = new RunnableWrapper(threadName, runnable);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(checkThreadNumber(threadNumber) ? Configs.THREAD_NUMBER : threadNumber);
        scheduledExecutorService.schedule(runnableWrapper, delay, unit);
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
            System.out.println("未设置自定义线程名称之前当前线程的名称： " + Thread.currentThread().getName());
            Thread.currentThread().setName(threadName);
            System.out.println("设置自定义线程名称之后当前线程的名称： " + Thread.currentThread().getName());
        }
    }

    private boolean checkThreadNumber(int number) {
        return number == 0;
    }
}
