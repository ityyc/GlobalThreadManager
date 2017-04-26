package com.tuba.yuanyc.globalthreadmanager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalThreadManager globalThreadManager = GlobalThreadManager.getInstance();
                globalThreadManager.setType(Configs.CACHE_THREAD_POOL);
                globalThreadManager.execute("current thread is main", new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("开启了子线程");
                    }
                });
            }
        });

    }
}
