package com.tuba.yuanyc.globalthreadmanager;

/**
 * Author ：yuanyc
 * Time ：2017/4/26
 * Description ：
 */

public class Configs {
    /**
     * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
     */
    public static final int CACHE_THREAD_POOL = 20010;
    /**
     * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
     */
    public static final int FIXED_THREAD_POOL = 20011;
    /**
     * 创建一个定长线程池，支持定时及周期性任务执行
     */
    public static final int SCHEDULED_THREAD_POOL = 20012;
    /**
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
     */
    public static final int SINGLE_THREAD_POOL = 20013;
}
