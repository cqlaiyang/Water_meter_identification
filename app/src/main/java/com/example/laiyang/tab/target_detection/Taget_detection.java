package com.example.laiyang.tab.target_detection;

import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.laiyang.tab.R;
import com.googlecode.tesseract.android.TessBaseAPI;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Taget_detection{
    private Mat dst;
    private Mat mat1 = new Mat();
    private Bitmap Tpl_bitmap;
    private Mat tpl;
    String datapath = Environment.getExternalStorageDirectory() + "/tesseract/";
    private Point minloc;
    private Point maxloc;
    private int method = Imgproc.TM_CCOEFF_NORMED;
    private String TAG = "OpenCv";
    private static boolean flag =true;
   public void taget(Mat src){
        //定时！
        long startTime = System.currentTimeMillis();

    /*    int height = src.rows() - tpl.rows() + 1;
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
        src.copyTo(dst2);*/

      /*  Mat mat1 = new Mat();
        Rect rect = new Rect((int)matchloc.x,(int)matchloc.y,tpl.cols(),tpl.rows());*/
       Imgproc.rectangle(src,new Point(289,186),new Point(400,300),new Scalar(0,0,255),4,8,0);
       Imgproc.putText(src,Long.toString(startTime),new Point(100,200),Core.FONT_HERSHEY_PLAIN,1.0,new Scalar(255,0,0),1);
       Imgproc.putText(src,"RunTim"+Long.toString(bean.runTime)+"ms",new Point(100,250),Core.FONT_HERSHEY_PLAIN,1.0,new Scalar(255,0,0),1);
        long finishTime = System.currentTimeMillis();
        long runTime = finishTime - startTime;

       com.orhanobut.logger.Logger.d(runTime);
    }

    public Mat comparison(Mat src){
        //定时！
        long startTime = System.currentTimeMillis();

        if (bean.init){
            String datapath = Environment.getExternalStorageDirectory() + "/tesseract/";
            boolean success = bean.baseApi.init(datapath, "nums");
            bean.init = false;
        }


        /*int height = src.rows() - tpl.rows() + 1;
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


        com.orhanobut.logger.Logger.d(matchloc.x);
        com.orhanobut.logger.Logger.d(matchloc.y);
        com.orhanobut.logger.Logger.d(tpl.cols());
        com.orhanobut.logger.Logger.d(tpl.rows());

        mat_final_mat = src.submat(rect2);*/

        Imgproc.rectangle(src,new Point(289,186),new Point(400,300),new Scalar(0,0,255),4,8,0);
        Imgproc.putText(src,Long.toString(startTime),new Point(100,200),Core.FONT_HERSHEY_PLAIN,1.0,new Scalar(255,0,0),1);
        Imgproc.putText(src,"BaseNumber:"+Long.toString(bean.reslut2),new Point(100,225),Core.FONT_HERSHEY_PLAIN,1.0,new Scalar(255,0,0),1);
        Imgproc.putText(src,"Number:"+Long.toString(bean.reslut1),new Point(100,250),Core.FONT_HERSHEY_PLAIN,1.0,new Scalar(255,0,0),1);
        Imgproc.putText(src,"Lap:"+Long.toString(bean.lap),new Point(100,275),Core.FONT_HERSHEY_PLAIN,1.0,new Scalar(255,0,0),1);
        Imgproc.putText(src,"RunTim"+Long.toString(bean.runTime)+"ms",new Point(100,300),Core.FONT_HERSHEY_PLAIN,1.0,new Scalar(255,0,0),1);
        Rect rect = new Rect(289,186,111,117);
        Mat mat_final_mat = new Mat();
        mat_final_mat = src.submat(rect);
        //mat_final_mat.copyTo(src);

        Bitmap bitmap = Bitmap.createBitmap(mat_final_mat.cols(),mat_final_mat.rows(),Bitmap.Config.ARGB_8888);
        /**
         * 二值化
         */
        //Imgproc.cvtColor(mat_final_mat,mat_final_mat,Imgproc.COLOR_BGR2GRAY);
        //Imgproc.threshold(mat_final_mat,mat_final_mat,0,255,Imgproc.THRESH_BINARY|Imgproc.THRESH_OTSU);

        if (bean.A == 1){
            //todo
          //  mat_final_mat.copyTo(bean.mat1);
            Log.d("OpenCv", "comparison: "+mat1);
            Imgproc.cvtColor(mat_final_mat,mat_final_mat,Imgproc.COLOR_BGR2GRAY);
            Imgproc.threshold(mat_final_mat,mat_final_mat,0,255,Imgproc.THRESH_BINARY|Imgproc.THRESH_OTSU);
            Core.bitwise_not(mat_final_mat,mat_final_mat);

            Utils.matToBitmap(mat_final_mat,bitmap);
            bean.baseApi.setImage(bitmap);
            bean.baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_CHAR);
            String s = bean.baseApi.getUTF8Text();
            Pattern pattern = Pattern.compile("[0-9]");
            Matcher isnum = pattern.matcher(s);
            /**
             * reslut1 是一个待判断位
             * reslut2 是一个判断位
             */
            if (!s.isEmpty()&&isnum.matches()) {
                if (!bean.panduan){
                    bean.reslut1 =Integer.parseInt(s);
                }
                if (bean.panduan){
                    bean.reslut2 = Integer.parseInt(s);
                    bean.panduan = false;
                }

                /**
                 * 一个真假判断，二值循环；分给付给static的值！
                 */
              /*  if (bean.panduan){
                    bean.reslut1 = Integer.parseInt(s);
                    bean.panduan = false;
                }else {
                    bean.reslut2 = Integer.parseInt(s);
                    bean.panduan = true;
                }*/

                if (bean.reslut1==(bean.reslut2+1)){
                    bean.lap++;
                    bean.panduan = true;
                }
                if (bean.reslut1==0 && bean.reslut2==9){
                    bean.lap++;
                    bean.reslut2 = 0;
                    bean.panduan =false;
                }
            }
            long runTime  = System.currentTimeMillis() - startTime;
            bean.runTime = runTime;
            /**
             *
             * 第一次方案识别前一个数和后一个数字的差值；
             */
          /*  if ((bean.reslut1 == (bean.reslut2-1))||((bean.reslut2+1) == bean.reslut1)){
                bean.lap ++;
            }
            if ((bean.reslut1 == 9&&bean.reslut2 == 0)||(bean.reslut1 == 0&&bean.reslut2==9)){
                bean.lap ++;
            }*/
            com.orhanobut.logger.Logger.d(s);
        }

 /*       if (bean.B == 1){
            bean.mat1.copyTo(mat1);
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
           // bean.distance = (float) Imgproc.compareHist(hist,hist2,Imgproc.HISTCMP_BHATTACHARYYA);
            long finishTime = System.currentTimeMillis();
            long runTime = finishTime - startTime;
            com.orhanobut.logger.Logger.d(runTime);
        }*/
        return src;
    }




}
