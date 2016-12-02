package com.yamankod.thread_1_progress;

import java.util.Random;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    private ProgressBar bar1;
    private ProgressBar bar2;
    private TextView msgWorking;
    private TextView msgReturned;
    boolean isRunning = false;
    final int MAX_SEC = 60;
    String strTest = "global value seen by all threads ";
    int intTest = 0;
    // -----------------------------------------------------------------------
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String returnedValue = (String) msg.obj;
            msgReturned.setText("returned by background thread: \n\n"
                    + returnedValue);
            bar1.incrementProgressBy(2);
            if (bar1.getProgress() == MAX_SEC) {
                msgReturned.setText("Done \n back thread has been stopped");
                isRunning = false;
            }
            if (bar1.getProgress() == bar1.getMax()) {
                msgWorking.setText("Done");
                bar1.setVisibility(View.INVISIBLE);
                bar2.setVisibility(View.INVISIBLE);
            } else {
                msgWorking.setText("Working..." + bar1.getProgress());
            }
        }
    }; // handler
    // -----------------------------------------------------------------------
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        bar1 = (ProgressBar) findViewById(R.id.progress);
        bar2 = (ProgressBar) findViewById(R.id.progress2);
        bar1.setMax(MAX_SEC);
        bar1.setProgress(0);
        msgWorking = (TextView) findViewById(R.id.TextView01);
        msgReturned = (TextView) findViewById(R.id.TextView02);
        strTest += "-01";
        intTest = 1;
    }// onCreate
    // -----------------------------------------------------------------------
    public void onStart() {
        super.onStart();
        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < MAX_SEC && isRunning; i++) {
                        Thread.sleep(1000);
                        Random rnd = new Random();
                        String data = "Thread Value: " + (int) rnd.nextInt(101);
                        data += "\n" + strTest + " " + intTest;
                        intTest++;
                        Message msg = handler.obtainMessage(1, (String) data);
                        if (isRunning) {
                            handler.sendMessage(msg);
                        }
                    }
                } catch (Throwable t) {
                }
            }
        });
        isRunning = true;
        background.start();
    }// onStart
    public void onStop() {
        super.onStop();
        isRunning = false;
    }
}