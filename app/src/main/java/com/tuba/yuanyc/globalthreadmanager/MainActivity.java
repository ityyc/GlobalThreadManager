package com.tuba.yuanyc.globalthreadmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tuba.yuanyc.globalthreadmanager.GlobalThreadManager.BlockingQueueEnum;
import com.tuba.yuanyc.globalthreadmanager.GlobalThreadManager.TimeUnitEnum;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    GlobalSimpleThreadManager globalSimpleThreadManager = null;
    GlobalThreadManager globalThreadManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        globalSimpleThreadManager = GlobalSimpleThreadManager.getInstance();
        globalThreadManager = GlobalThreadManager.getInstace();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                globalSimpleThreadManager.setType(Configs.CACHE_THREAD_POOL);
                for (int i = 0; i < 4; i++) {
                    try {
                        Thread.sleep(i * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    globalSimpleThreadManager.execute("线程一", new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(finalI);
                        }
                    });
                }
                break;
            case R.id.button2:
                globalSimpleThreadManager.setType(Configs.FIXED_THREAD_POOL);
                globalSimpleThreadManager.setThreadNumber(3);
                for (int i = 0; i < 5; i++) {
                    final int finalI = i;
                    globalSimpleThreadManager.execute("线程二", new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println(finalI);
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                break;
            case R.id.button3:
                globalSimpleThreadManager.setType(Configs.SCHEDULED_THREAD_POOL);
                globalSimpleThreadManager.setThreadNumber(5);
                globalSimpleThreadManager.schedule("线程三", new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("延迟3秒");
                    }
                },3, TimeUnit.SECONDS);

                break;
            case R.id.button4:
                globalSimpleThreadManager.setType(Configs.SINGLE_THREAD_POOL);
                for (int i = 0; i < 5; i++) {
                    final int finalI = i;
                    globalSimpleThreadManager.execute("线程四", new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println(finalI);
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
            case R.id.button5:
                //构建自定义的线程池
                GlobalThreadManager.ThreadPoolSetting threadPoolSetting = new GlobalThreadManager.ThreadPoolSetting();
                threadPoolSetting.setCorePoolSize(3);
                threadPoolSetting.setMaximumPoolSize(8);
                threadPoolSetting.setTimeUnitEnum(TimeUnitEnum.MILLISECONDS);
                threadPoolSetting.setKeepAliveTime(1000);
                threadPoolSetting.setBlockSize(5);
                threadPoolSetting.setBlockingQueueEnum(BlockingQueueEnum.ARRAY_BLOCKING_QUEUE);
                threadPoolSetting.build();
                globalThreadManager.execute("自定义线程池", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }
}
