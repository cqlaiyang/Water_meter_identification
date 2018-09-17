package com.example.laiyang.tab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laiyang.tab.camera_utils.JavaCamera;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("opencv_java3");
    }
    private static final String TAG = "ORC";
    private static final String DEFAULT_LANGUAGE = "eng";
    TessBaseAPI baseApi;
    Bitmap bitmap;

    /*-------------------------------------绑定视图---------------------------------------------*/
    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textView)
    TextView textView;
    /*-------------------------------------绑定视图---------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());
        ButterKnife.bind(this);
        String datapath = Environment.getExternalStorageDirectory()+"s";
        Logger.d("begin"+datapath,"ORC");


        /**
         * 初始化！
         */
        try {
            initTessBaseAPI();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /*-------------------------------------点击事件-----------------------------------------*/
    @OnClick(R.id.button) void start(){
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.win);
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap,mat);
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(mat,mat,0,255,Imgproc.THRESH_BINARY|Imgproc.THRESH_OTSU);
        Core.bitwise_not(mat,mat);
        Utils.matToBitmap(mat,bitmap);
        imageView.setImageBitmap(bitmap);
        baseApi.setImage(bitmap);
        String recongnizedText = baseApi.getUTF8Text();
        Logger.d(recongnizedText);
        if (!recongnizedText.isEmpty()){
            textView.append("识别结果：\n" + recongnizedText);
        }
    }


    @OnClick(R.id.button2) void startCamera(){
        Intent intent = new Intent(MainActivity.this,JavaCamera.class);
        startActivity(intent);
    }
    /*-------------------------------------点击事件-----------------------------------------*/

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
            Log.i(TAG, "load Tesseract OCR Engine successfully...");
        } else {
            Log.i(TAG, "WARNING:could not initialize Tesseract data...");
        }
    }

}
