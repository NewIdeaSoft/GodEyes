package com.nisoft.inspectortools.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ImageToStringUtil {
    public static final String TESS_BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/tesseract/";
    public static final String DEFAULT_LANGUAGE = "chi_sim";
    private static final String TAG = "ResourceFileTag：";
    public static String parseImageToString(String imagePath) throws IOException {
        if(imagePath==null||imagePath.equals("")){
            return "错误：图片不存在";
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);
        int rotate = 0;
        ExifInterface exif = new ExifInterface(imagePath);
        int imageOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
        switch (imageOrientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            default:
                break;
        }
        int with = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.preRotate(rotate);
        bitmap = Bitmap.createBitmap(bitmap,0,0,with,height,mtx,false);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Bitmap data = handleBlackWitheBitmap(bitmap);
        TessBaseAPI baseAPI = new TessBaseAPI();
        baseAPI.init(TESS_BASE_PATH,DEFAULT_LANGUAGE);
        baseAPI.setImage(bitmap);
        String recognizedText = baseAPI.getUTF8Text();
        baseAPI.clear();
        baseAPI.end();
        return recognizedText;
    }

    public static Bitmap handleBlackWitheBitmap(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        final int tmp = 180;
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
        // 设定二值化的域值，默认值为100
        //tmp = 180;
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                // 分离三原色
                alpha = ((grey & 0xFF000000) >> 24);
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                if (red > tmp) {
                    red = 255;
                } else {
                    red = 0;
                }
                if (blue > tmp) {
                    blue = 255;
                } else {
                    blue = 0;
                }
                if (green > tmp) {
                    green = 255;
                } else {
                    green = 0;
                }
                pixels[width * i + j] = alpha << 24 | red << 16 | green << 8
                        | blue;
                if (pixels[width * i + j] == -1) {
                    pixels[width * i + j] = -1;
                } else {
                    pixels[width * i + j] = -16777216;
                }
            }
        }
        // 新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, width, height);
        return resizeBmp;
    }
//    public static void resourceToFile(Context context){
//        if (!new File(TESS_BASE_PATH + "tessdata/",
//                DEFAULT_LANGUAGE +".traineddata").exists())
//            //推送字库到SD卡
//            ResourceToFile(context, R.raw.chi_sim, TESS_BASE_PATH + "tessdata/",
//                    DEFAULT_LANGUAGE +".traineddata");
//    }
    public static boolean ResourceToFile(Context context, int ResourceId,
                                         String filePath, String fileName) {
        InputStream is = null;
        FileOutputStream fs = null;
        try {
            File dir  = new File(filePath);
            if (!dir.exists()){
                dir.mkdirs();
            }

            is = context.getResources().openRawResource(ResourceId);
            File file = new File(filePath + File.separator + fileName);
            fs = new FileOutputStream(file);
            while (is.available() > 0) {
                byte[] b = new byte[is.available()];
                is.read(b);
                fs.write(b);
            }

            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            try {
                if (fs != null)
                    fs.close();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}
