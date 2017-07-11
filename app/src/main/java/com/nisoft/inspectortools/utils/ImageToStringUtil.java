package com.nisoft.inspectortools.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
            + "/tesseract/";
    public static final String DEFAULT_LANGUAGE = "chi_sim";
    private static final String TAG = "ResourceFileTag：";

    public static String parseImageToString(String imagePath) throws IOException {
        Log.e("imagePath:", imagePath);
        if (imagePath == null || imagePath.equals("")) {

            return "错误：图片不存在";
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        int rotate = 0;
        ExifInterface exif = new ExifInterface(imagePath);
        int imageOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (imageOrientation) {
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
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, with, height, mtx, false);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        bitmap = ImageFilter.convertToBlackWhite(bitmap);
        bitmap = ImageFilter.bitmap2Gray(bitmap);
        TessBaseAPI baseAPI = new TessBaseAPI();
        baseAPI.init(TESS_BASE_PATH, DEFAULT_LANGUAGE);
        baseAPI.setImage(bitmap);
        String recognizedText = baseAPI.getUTF8Text();
        baseAPI.clear();
        baseAPI.end();
        return recognizedText;
    }

    public static boolean ResourceToFile(Context context, int ResourceId,
                                         String filePath, String fileName) {
        InputStream is = null;
        FileOutputStream fs = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
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
