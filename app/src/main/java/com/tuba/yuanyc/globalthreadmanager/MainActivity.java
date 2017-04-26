package com.tuba.yuanyc.globalthreadmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    GlobalThreadManager globalThreadManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        globalThreadManager = GlobalThreadManager.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                globalThreadManager.setType(Configs.CACHE_THREAD_POOL);
                for (int i = 0; i < 4; i++) {
                    try {
                        Thread.sleep(i * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    globalThreadManager.execute("线程一", new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(finalI);
                        }
                    });
                }
                break;
            case R.id.button2:
                globalThreadManager.setType(Configs.FIXED_THREAD_POOL);
                globalThreadManager.setThreadNumber(3);
                for (int i = 0; i < 5; i++) {
                    final int finalI = i;
                    globalThreadManager.execute("线程二", new Runnable() {
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
                globalThreadManager.setType(Configs.SCHEDULED_THREAD_POOL);
                globalThreadManager.setThreadNumber(5);
                globalThreadManager.schedule("线程三", new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("延迟3秒");
                    }
                },3, TimeUnit.SECONDS);

                break;
            case R.id.button4:
                globalThreadManager.setType(Configs.SINGLE_THREAD_POOL);
                for (int i = 0; i < 5; i++) {
                    final int finalI = i;
                    globalThreadManager.execute("线程四", new Runnable() {
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
        }
    }
}
