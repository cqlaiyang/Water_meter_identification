package com.example.laiyang.tab.target_detection;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class bean {
    public static int A = 0;
    public static int B = 0;

    public static int number = 0;
    public static int counter = 0;
    public static long runTime = 0;
    public static float start = 0;
    public static float end = 0;

    public static int reslut1 = 0;
    public static int reslut2 = 0;
    public static int jilu  = 0;
    public static int counter2 = 0;
    public static int lap = 0;
    public static boolean panduan = true;

    public static Bitmap bitmap;
    public static boolean init = true;
    public static TessBaseAPI baseApi = new TessBaseAPI();
}
