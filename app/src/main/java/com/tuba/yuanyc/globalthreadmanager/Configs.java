package com.tuba.yuanyc.globalthreadmanager;

/**
 * Author ：yuanyc
 * Time ：2017/3/4
 * Description ：线程池的静态常量类
 */

public class Configs {
    /**
     * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程，线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程
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
     * 使用场景：多个线程读写同一个文件，可考虑使用此种线程池保证安全
     */
    public static final int SINGLE_THREAD_POOL = 20013;
    /**
     * 创建的默认线程个数
     */
    public static final int THREAD_NUMBER = 6;

    /**
     * 默认的核心线程数量
     */
    public static final int THREAD_CORE_NUMBER = 5;
    /**
     * 默认的最大线程数量
     */
    public static final int THREAD_MAX_NUMBER = 10;
    /**
     * 默认的阻塞队列大小
     */
    public static final int THREAD_BLOCK_SIZE = 5;
    /**
     * 默认的空闲线程超时时间(默认是1000毫秒)
     */
    public static final int THREAD_IDLE_TIME_OUT = 1000;



}
