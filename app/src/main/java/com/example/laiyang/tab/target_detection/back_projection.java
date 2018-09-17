package com.example.laiyang.tab.target_detection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.orhanobut.logger.Logger;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class back_projection {
    private Mat dst;
    private Mat mat1 = new Mat();
    private Bitmap Tpl_bitmap;
    private Mat tpl;
    private Point minloc;
    private Point maxloc;
    private int method = Imgproc.TM_CCOEFF_NORMED;
    private String TAG = "OpenCv";
    private static boolean flag =true;

   public Mat taget(Mat src,Mat tpl){
        //定时！
        long startTime = System.currentTimeMillis();
       /**
        * @auther 下面注释为模板匹配算法！
        * @deprecated 已经被启用
        * 原因是效率太低平均耗时1s左右才能找到识别区域！
        */
      /*  int height = src.rows() - tpl.rows() + 1;
        int width = src.cols() - tpl.cols() + 1;

        Mat result = new Mat(height,width, CvType.CV_32FC1);

        //模板匹配
        Imgproc.matchTemplate(src,tpl,result,method);

        com.orhanobut.logger.Logger.d(result);

        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);

             maxloc = minMaxLocResult.maxLoc;
             minloc = minMaxLocResult.minLoc;

        Point matchloc = null;
        if (method == Imgproc.TM_SQDIFF||method == Imgproc.TM_SQDIFF_NORMED){
            matchloc = minloc;
        }else {
            matchloc = maxloc;
        }

        Mat dst2 = new Mat();
        src.copyTo(dst2);

      *//*  Mat mat1 = new Mat();
        Rect rect = new Rect((int)matchloc.x,(int)matchloc.y,tpl.cols(),tpl.rows());*//*
        Imgproc.rectangle(src,matchloc,new Point(matchloc.x + tpl.cols(),matchloc.y + tpl.rows()),new Scalar(255,255,255),4,8,0);*/
       Mat hsv = new Mat();
       tpl.copyTo(hsv);
       Mat mask = Mat.ones(hsv.size(),CvType.CV_8UC1);
       Mat mHist = new Mat();
       Imgproc.calcHist(Arrays.asList(hsv),new MatOfInt(0,1),mask,mHist,new MatOfInt(30,32),new MatOfFloat(0,179,0,255));

       Logger.d(mHist.rows());
       Logger.d(mHist.cols());

       dst  = new Mat();
       Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2HSV);
       Logger.d(mHist);
       Logger.d(dst);
       Imgproc.calcBackProject(Arrays.asList(src),new MatOfInt(0,1),mHist,src,new MatOfFloat(0,179,0,255),1);
       Core.normalize(src,src,0,255,Core.NORM_MINMAX);
       Imgproc.cvtColor(src,src,Imgproc.COLOR_GRAY2BGR);

        long finishTime = System.currentTimeMillis();
        long runTime = finishTime - startTime;
       com.orhanobut.logger.Logger.d(runTime);
        return src;
    }

    public Mat comparison(Mat src,Mat tpl){
        //定时！
        long startTime = System.currentTimeMillis();

        int height = src.rows() - tpl.rows() + 1;
        int width = src.cols() - tpl.cols() + 1;

        Mat result = new Mat(height,width, CvType.CV_32FC1);

        //模板匹配
        Imgproc.matchTemplate(src,tpl,result,method);

        com.orhanobut.logger.Logger.d(result);

        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);

        maxloc = minMaxLocResult.maxLoc;
        minloc = minMaxLocResult.minLoc;

        Point matchloc = null;
        if (method == Imgproc.TM_SQDIFF||method == Imgproc.TM_SQDIFF_NORMED){
            matchloc = minloc;
        }else {
            matchloc = maxloc;
        }
        Mat mat_final_mat = new Mat();
        Rect rect2 =new Rect((int)matchloc.x,(int)matchloc.y,tpl.cols(),tpl.rows());
        mat_final_mat = src.submat(rect2);
        Imgproc.rectangle(src,matchloc,new Point(matchloc.x + tpl.cols(),matchloc.y + tpl.rows()),new Scalar(255,255,255),4,8,0);
        if (bean.A == 1){

            mat1 = mat_final_mat;
            Log.d("OpenCv", "comparison: "+mat1);
            bean.A = 0;
        }

        if (bean.B == 1){
            if (flag){
                flag =false;
                Imgproc.cvtColor(mat1,mat1,Imgproc.COLOR_RGBA2GRAY);
            }

            List<Mat> images = new ArrayList<>();
            images.add(mat1);
            //第一个直方图
            Mat mask = Mat.ones(mat1.size(),CvType.CV_8UC1);
            Mat hist = new Mat();
            Imgproc.calcHist(images,new MatOfInt(0),mask,hist,new MatOfInt(256),new MatOfFloat(0,255));

            Core.normalize(hist,hist,0,255,Core.NORM_MINMAX);

            com.orhanobut.logger.Logger.d(hist);

            //第二个直方图
            images.clear();
            images.add(mat_final_mat);
            Mat hist2 = new Mat();
            Imgproc.calcHist(images,new MatOfInt(0),mask,hist2,new MatOfInt(256),new MatOfFloat(0,255));

            Core.normalize(hist2,hist2,0,255,Core.NORM_MINMAX);

            com.orhanobut.logger.Logger.d(hist2);
        //    bean.distance = (float) Imgproc.compareHist(hist,hist2,Imgproc.HISTCMP_CORREL);
            long finishTime = System.currentTimeMillis();
            long runTime = finishTime - startTime;
            com.orhanobut.logger.Logger.d(runTime);
        }
        return src;
    }
}
