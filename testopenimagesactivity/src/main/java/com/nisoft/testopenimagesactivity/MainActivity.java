package com.nisoft.testopenimagesactivity;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements MediaScannerConnection.MediaScannerConnectionClient{
    public String[] allFiles;
    private static final String SCAN_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/工作相册/质量问题/1234_1/";
    private static final String FILE_TYPE="image/*";

    private MediaScannerConnection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.text);
        File folder = new File(SCAN_PATH);
        allFiles = folder.list();
        //   uriAllFiles= new Uri[allFiles.length];
        for(int i=0;i<allFiles.length;i++)
        {
            Log.d("all file path"+i, allFiles[i]+allFiles.length);
        }
        //  Uri uri= Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString()+"/yourfoldername/"+allFiles[0]));


        String scanPath= SCAN_PATH+allFiles[1];
        System.out.println(" SCAN_PATH  " +scanPath);

        Log.d("SCAN PATH", "Scan Path " + scanPath);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });
    }
    private void startScan()
    {
        Log.d("Connected","success"+conn);
        if(conn!=null)
        {
            conn.disconnect();
        }
        conn = new MediaScannerConnection(this,this);
        conn.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        Log.d("onMediaScannerConnected","success"+conn);
        conn.scanFile(SCAN_PATH, FILE_TYPE);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        try {
            Log.d("onScanCompleted",uri + "success"+conn);
            System.out.println("URI " + uri);
            if (uri != null)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }
        } finally
        {
            conn.disconnect();
            conn = null;
        }
    }

}
/**
 * import java.io.File;
 import android.app.Activity;
 import android.content.Intent;
 import android.media.MediaScannerConnection;
 import android.media.MediaScannerConnection.MediaScannerConnectionClient;
 import android.net.Uri;
 import android.os.Bundle;
 import android.os.Environment;
 import android.util.Log;
 import android.view.View;
 import android.view.View.OnClickListener;
 import android.widget.Button;

 /**
 欢迎关注我的微博 http://weibo.com/lixiaodaoaaa
 http://t.qq.com/lixiaodaoaaa
 */

//public class SDCARD123Activity extends Activity implements MediaScannerConnectionClient{
//    public String[] allFiles;
//    private String SCAN_PATH ;
//    private static final String FILE_TYPE="image/*";
//
//    private MediaScannerConnection conn;
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        File folder = new File("/sdcard/Photo/");
//        allFiles = folder.list();
//        //   uriAllFiles= new Uri[allFiles.length];
//        for(int i=0;i<allFiles.length;i++)
//        {
//            Log.d("all file path"+i, allFiles[i]+allFiles.length);
//        }
//        //  Uri uri= Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString()+"/yourfoldername/"+allFiles[0]));
//
//
//        SCAN_PATH=Environment.getExternalStorageDirectory().toString()+"/Photo/"+allFiles[0];
//        System.out.println(" SCAN_PATH  " +SCAN_PATH);
//
//        Log.d("SCAN PATH", "Scan Path " + SCAN_PATH);
//        Button scanBtn = (Button)findViewById(R.id.scanBtn);
//        scanBtn.setOnClickListener(new OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                startScan();
//            }});
//    }
//    private void startScan()
//    {
//        Log.d("Connected","success"+conn);
//        if(conn!=null)
//        {
//            conn.disconnect();
//        }
//        conn = new MediaScannerConnection(this,this);
//        conn.connect();
//    }
//    @Override
//    public void onMediaScannerConnected() {
//        Log.d("onMediaScannerConnected","success"+conn);
//        conn.scanFile(SCAN_PATH, FILE_TYPE);
//    }
//    @Override
//    public void onScanCompleted(String path, Uri uri) {
//        try {
//            Log.d("onScanCompleted",uri + "success"+conn);
//            System.out.println("URI " + uri);
//            if (uri != null)
//            {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(uri);
//                startActivity(intent);
//            }
//        } finally
//        {
//            conn.disconnect();
//            conn = null;
//        }
//    }
//}
// */
