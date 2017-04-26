package com.tuba.yuanyc.globalthreadmanager;

import android.text.TextUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author ：yuanyc
 * Time ：2017/3/4
 * Description ：线程统一管理类
 * 使用步骤：
 * 第一步：通过GlobalThreadManager.getInstance获取通过GlobalThreadManager实例
 * 第二步：通过获取的实例调用setType设置要使用的线程池类型，不设置默认使用缓存线程池
 * 第三步：调用execute方法，需传入当前线程的名字（你自定义的名字），要执行的runnable
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
     * 设置线程池类型
     * @param type 类型
     * @see Configs#CACHE_THREAD_POOL
     * @see Configs#FIXED_THREAD_POOL
     * @see Configs#SCHEDULED_THREAD_POOL
     * @see Configs#SINGLE_THREAD_POOL
     */
    public void setType(int type) {
        this.type = type;
    }

    private GlobalThreadManager() {

    }

    /**
     * 获取GlobalThreadManager实例
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
     * @param threadName 自定义的线程名字
     * @param runnable 要执行的runnable
     */
    public void execute(String threadName, Runnable runnable) {
        RunnableWrapper runnableWrapper = new RunnableWrapper(threadName, runnable);
        switch (type){
            case Configs.CACHE_THREAD_POOL:
                executorService = Executors.newCachedThreadPool();
                break;
            case Configs.FIXED_THREAD_POOL:
                executorService = Executors.newFixedThreadPool(10);
                break;
            case Configs.SCHEDULED_THREAD_POOL:
                executorService = Executors.newScheduledThreadPool(10);
                break;
            case Configs.SINGLE_THREAD_POOL:
                executorService = Executors.newSingleThreadExecutor();
                break;
            default:
                executorService = Executors.newCachedThreadPool();
        }
        executorService.execute(runnableWrapper);
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
            setThreadName(name);
        }

        private void setThreadName(String threadName) {
            if (TextUtils.isEmpty(threadName)) {
                throw new RuntimeException("未给Thread线程设置name");
            }
            System.out.println("未设置自定义线程名称之前当前线程的名称： "+Thread.currentThread().getName());
            Thread.currentThread().setName(threadName);
            System.out.println("设置自定义线程名称之后当前线程的名称： "+Thread.currentThread().getName());
        }
    }
}
