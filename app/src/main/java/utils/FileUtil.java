package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/5/19.
 */

public class FileUtil {
    /***
     *  复制源文件到目标文件，删除源文件
     * @param resPath 源文件路径
     * @param targetPath 目标路径
     */
    public static void moveFile(String resPath,String targetPath){
        try {
            File file = new File(resPath);
            File targetFile = new File(targetPath);
            if (targetFile.exists()){
                targetFile.delete();
            }

            if (file.exists()){
                int byteSum = 0;
                int byteRead = 0;
                InputStream in = new FileInputStream(file);
                OutputStream out = new FileOutputStream(targetPath);
                byte[] buffer = new byte[1024];
                while((byteRead = in.read(buffer))!=-1){
                    byteSum += byteRead;
                    out.write(buffer,0,byteRead);
                }

                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
