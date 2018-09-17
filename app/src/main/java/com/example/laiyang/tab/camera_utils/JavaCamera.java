package com.example.laiyang.tab.camera_utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laiyang.tab.R;
import com.example.laiyang.tab.camera_utils.MyCvCameraView;
import com.example.laiyang.tab.target_detection.Taget_detection;
import com.example.laiyang.tab.target_detection.back_projection;
import com.example.laiyang.tab.target_detection.bean;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.orhanobut.logger.AndroidLogAdapter;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JavaCamera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private MyCvCameraView mOpenCvCameraView;
    private IntentFilter intentFilter;
    public static  int COMPARISIB = 0;
    private Bitmap Tpl_bitmap;
    private Mat tpl = new Mat();
    private int option = 0;
    private Taget_detection taget_detection;
    private back_projection back_projection;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;
    private TessBaseAPI baseApi;
    //--------------------------------------绑定视图-------------------------------------------
    @BindView(R.id.result)
    EditText editText;

    @BindView(R.id.input1)
    EditText editText2;
    //----------------------------------------------------------------------------------------


    //--------------------------------------点击事件-------------------------------------------
    @OnClick(R.id.start) void start(){
        option = 1;
    }

    @OnClick(R.id.startCount) void count(){
        option = 2;
        int input = Integer.parseInt(editText2.getText().toString());
        bean.start = input;

    }

    @OnClick(R.id.end) void end(){
        option = 3;
        bean.A= 0;

        final EditText editText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(JavaCamera.this);
        builder.setTitle("请输入末尾值");
        builder.setIcon(R.drawable.tpl);
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                bean.end = Float.parseFloat(editText.getText().toString());

                //嵌套
                final float src = bean.lap + ((10-bean.start)+(10-bean.end))/10;
                editText2.setText("结果"+src);
                final TextView textView = new TextView(JavaCamera.this);
                textView.setText("       识别结果为："+Float.toString(src));
                AlertDialog.Builder builder1  = new AlertDialog.Builder(JavaCamera.this);
                builder1.setTitle("结果");
                builder1.setIcon(R.drawable.tpl);
                builder1.setView(textView);
                builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton("退出",null).show();
            }
        }).setNegativeButton("取消",null).show();


    }

    //----------------------------------------------------------------------------------------


    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            editText.append("当前数字: "+bean.reslut1+"               "+"圈数:"+bean.lap+"                   ");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);

        ButterKnife.bind(this);


        try {
            initTessBaseAPI();
        } catch (IOException e) {
            e.printStackTrace();
        }

        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter());

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = (MyCvCameraView) findViewById(R.id.cv_camera_id);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.enableFpsMeter();
        // mOpenCvCameraView.setCameraIndex(1);
        mOpenCvCameraView.enableView();


        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.laiyang.tab.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }


    private void process(Mat frame){
        switch (option){
            case 1:{
                taget_detection = new Taget_detection();
                //获取模板待识别区域
               /* Tpl_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tpl4);
                Utils.bitmapToMat(Tpl_bitmap,tpl);*/
                taget_detection.taget(frame);

                /**
                 * 直方图反射投影
                 */
               /* back_projection = new back_projection();

                //获取模板待识别区域
                Tpl_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.samsung);
                Utils.bitmapToMat(Tpl_bitmap,tpl);
                back_projection.taget(frame,tpl);*/
                break;
            }
            case 2:{
                bean.A = 1;
                taget_detection = new Taget_detection();
                //获取模板待识别区域
                //Tpl_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tpl4);
                //Utils.bitmapToMat(Tpl_bitmap,tpl);
                taget_detection.comparison(frame);
                Intent intent = new Intent("com.example.laiyang.tab.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent);

                break;
            }
            case 3:{
                bean.A= 0;


             /*   double res = bean.distance;
                textView.append("相似度结果"+res);*/
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.begin:
                option = 1;

                break;
            case R.id.stop:{
                option = 2;
                final EditText editText = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(JavaCamera.this);
                builder.setTitle("请输入初始值");
                builder.setIcon(R.drawable.tpl);
                builder.setView(editText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        bean.start = Float.parseFloat(editText.getText().toString());
                    }
                }).setNegativeButton("取消",null).show();
                break;
            }

            case R.id.finish:{


                option = 3;
                final EditText editText = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(JavaCamera.this);
                builder.setTitle("请输入末尾值");
                builder.setIcon(R.drawable.tpl);
                builder.setView(editText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        bean.end = Float.parseFloat(editText.getText().toString());

                        //嵌套
                        float src = bean.lap + 9-((bean.start+bean.end))/10;
                        final TextView textView = new TextView(JavaCamera.this);
                        textView.setText(Float.toString(src));
                        AlertDialog.Builder builder1  = new AlertDialog.Builder(JavaCamera.this);
                        builder1.setTitle("结果");
                        builder1.setIcon(R.drawable.tpl);
                        builder1.setView(textView);
                        builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNegativeButton("退出",null).show();
                    }
                }).setNegativeButton("取消",null).show();



                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
       final Mat frame = inputFrame.rgba();
        if (this.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {

        }
       /* new Thread(new Runnable() {
            @Override
            public void run() {


            }
        }).start();*/
        process(frame);
        Core.flip(frame,frame,180);

        Core.flip(frame, frame, 1);

        return frame;
    }


    private void initTessBaseAPI() throws IOException {
        baseApi = new TessBaseAPI();
        String datapath = Environment.getExternalStorageDirectory() + "/tesseract/";
        File dir = new File(datapath + "tessdata/");
        if (!dir.exists()) {
            dir.mkdirs();
            InputStream input = getResources().openRawResource(R.raw.eng);
            File file2 = new File(dir,"eng.traineddata");
            FileOutputStream outputStream = new FileOutputStream(file2);

            byte[] buff = new byte[1024];
            int len = 0;
            while((len = input.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
            }
            input.close();
            outputStream.close();
        }
        boolean success = baseApi.init(datapath, "nums");
        if(success){
            Log.i("OpenCv", "load Tesseract OCR Engine successfully...");
        } else {
            Log.i("OpenCv", "WARNING:could not initialize Tesseract data...");
        }
    }

}
