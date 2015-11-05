package jcl.com.poker;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    private Button btn_calculate;
    private ArrayList<ArrayList> allCards = new ArrayList<ArrayList>();
    private int allCardMaxSize = 11;

    //儲存現在第幾組
    private int targetNo = 0;
    private float totalTime = 90000000;  //9千萬次失敗

    private long start;

    public float sameHand = 0;

    private TextView tv_display;
    private EditText edt_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_display = (TextView)findViewById(R.id.tv_calculate);
        tv_display.setText("10001 Thread 待命中");

        edt_num = (EditText)findViewById(R.id.edt_num);

        btn_calculate = (Button)findViewById(R.id.btn_calculate);
        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalTime = Float.parseFloat(edt_num.getText().toString());

                tv_display.setText("10001 Thred 開始運行，正在計算結果請稍後。");

                start = System.currentTimeMillis();


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            //Log.w("John", "Thread 10001啟動");
                            for (int i = 0; i < totalTime; i++) {

                                    startCalculate();
                                    Log.w("John", "第" + i + "組");
                            }

                            Message msg = new Message();
                            msg.what = 10001;
                            handler.sendMessage(msg);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });


    }

    private void startCalculate(){

        //改用float分辨種類
        int cards = 5;
        ArrayList<Float> onehand = new ArrayList<Float>();
        for (int i = 0; i < cards; i++){
            float cardNo;
            switch ((int)(Math.random() * 4)){
                case 0:
                    cardNo = (int)(Math.random() * 13)+1;
                    cardNo += 100;
                    onehand.add(cardNo);
                    //onehand.add("黑桃"+(cardNo+1));
                case 1:
                    cardNo = (int)(Math.random() * 13)+1;
                    cardNo += 200;
                    onehand.add(cardNo);
                    //onehand.add("梅花" + (cardNo + 1));
                case 2:
                    cardNo = (int)(Math.random() * 13)+1;
                    cardNo += 300;
                    onehand.add(cardNo);
                    //onehand.add("紅心" + (cardNo + 1));
                case 3:
                    cardNo = (int)(Math.random() * 13)+1;
                    cardNo += 400;
                    onehand.add(cardNo);
                    //onehand.add("方塊" + (cardNo + 1));
            }
        }

        Collections.sort(onehand);

        //Log.w("John", "長度:"+allCards.size());
        if (allCards.size() < allCardMaxSize){
            allCards.add(onehand);
        }else{
            checkSameHand();
            allCards.remove(0); //移除最前面一手
            allCards.add(onehand);
        }
    }

    private void checkSameHand(){

        //與前九組來比對
        for (int i = 0; i < 10; i++){

            //checkPart1(i);

            if ((float)allCards.get(i).get(0) == (float)allCards.get(10).get(0)){

                if ((float)allCards.get(i).get(1) == (float)allCards.get(10).get(1)){
                    //Log.w("John", i+"第二組比對成功");

                    if ((float)allCards.get(i).get(2) == (float)allCards.get(10).get(2)){
                        //Log.w("John", i+"第三組比對成功");

                        if ((float)allCards.get(i).get(3) == (float)allCards.get(10).get(3)){
                            //Log.w("John", i+"第四組比對成功");

                            if ((float)allCards.get(i).get(4) == (float)allCards.get(10).get(4)){
                                //Log.w("John", i+"第五組比對成功");
                                sameHand++;
                            }
                        }
                    }
                }
            }else{
                //Log.w("John", "比對不成功" + allCards.get(i).get(0) + " "+allCards.get(10).get(0));
            }

        }
    }

    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 10001:

                    Log.w("John", "10001 Thread跑完了");
                    Log.w("John", "偵測到有重複的組數:"+sameHand);
                    float persent = (sameHand/totalTime);
                    Log.w("John", "重複組數機率:" + persent);
                    long finishTime = (System.currentTimeMillis() - start);
                    Log.w("John", "全部時間:"+ finishTime + "ms");

                    tv_display.setText("10001 Thread 運行完畢\n");
                    tv_display.append("偵測到有重複的組數:" + sameHand + "\n");
                    tv_display.append("重複組數機率:" + persent + "\n");
                    tv_display.append("全部時間:" + finishTime + "ms");
                    break;

                case 10003:
                    break;

                case 10004:
                    break;
            }
        }
    };
}
