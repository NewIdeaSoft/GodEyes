package com.nisoft.inspectortools.ui.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.utils.HttpUtil;
import com.nisoft.inspectortools.utils.ImageToStringUtil;
import com.nisoft.inspectortools.utils.JsonParser;

import java.io.File;
import java.io.IOException;

import static com.nisoft.inspectortools.utils.ImageToStringUtil.DEFAULT_LANGUAGE;
import static com.nisoft.inspectortools.utils.ImageToStringUtil.TESS_BASE_PATH;

public class EditTextActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 13;
    private static final int CHOOSE_PHOTO = 1;
    private static final int TAKE_PHOTO = 2;
    private static final int CROP_PHOTO = 3;
    private static final String APPID = "59363ca2";
    private static final String CROP_CACHE_DIR =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/工作相册/cache";
    private EditText mAuthorEdit;
    private ImageButton mSpeechButton;
    private ImageButton mCameraButton;
    private RecognizerDialogListener mRecognizerDialogListener;
    private ProgressDialog mDialog;
    private DownloadManagerReceiver mReceiver;
    private long lanDownloadId;
    private String initText;
    private String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        mAuthorEdit = (EditText) findViewById(R.id.edit_text_author);
        mSpeechButton = (ImageButton) findViewById(R.id.input_speech);
        mCameraButton = (ImageButton) findViewById(R.id.input_camera);
        SpeechUtility.createUtility(EditTextActivity.this, SpeechConstant.APPID + "=" + APPID);
        initText = getIntent().getStringExtra("initText");
        mAuthorEdit.setText(initText);
        if (initText != null) {
            mAuthorEdit.setSelection(initText.length());
        } else {
            mAuthorEdit.setSelection(0);
        }
        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitListener initListener = null;
                RecognizerDialog recognizerDialog = new RecognizerDialog(EditTextActivity.this, initListener);
                recognizerDialog.setParameter(SpeechConstant.DOMAIN, "iat");
                recognizerDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                recognizerDialog.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
                recognizerDialog.setParameter(SpeechConstant.VAD_EOS, "1000");
                recognizerDialog.setListener(mRecognizerDialogListener);
                recognizerDialog.show();
                mRecognizerDialogListener = new RecognizerDialogListener() {
                    @Override
                    public void onResult(RecognizerResult recognizerResult, boolean b) {
                        String text = JsonParser.parseIatResult(recognizerResult.getResultString());
                        mAuthorEdit.append(text);
                        mAuthorEdit.setSelection(mAuthorEdit.length());
                    }

                    @Override
                    public void onError(SpeechError speechError) {
                        Toast.makeText(EditTextActivity.this, speechError.getErrorCode() + "", Toast.LENGTH_SHORT).show();
                        Log.e("ErrorCode:", speechError.getErrorCode() + "");
                    }
                };
            }
        });
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照或从相册选择图片，转换成文字显示在文本框
                if (!new File(TESS_BASE_PATH + "tessdata/",
                        DEFAULT_LANGUAGE + ".traineddata").exists()) {
                    showLanDownloadDialog();
                } else {
                    showCameraDialog();
                }
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 12);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 14);
        }

    }

    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 12:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "未获得权限，无法进行语音输入！", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String contentEdit = mAuthorEdit.getText().toString();
            if (!contentEdit.equals(initText) && !contentEdit.equals("")) {
                Intent intent = new Intent();
                intent.putExtra("content_edit", contentEdit);
                int content_position = getIntent().getIntExtra("content_position", -1);
                if (content_position > -1) {
                    intent.putExtra("content_position", content_position);
                }
                setResult(Activity.RESULT_OK, intent);
            } else {
                setResult(Activity.RESULT_CANCELED);
            }

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CHOOSE_PHOTO:
                Uri uri = data.getData();
                if (DocumentsContract.isDocumentUri(this, uri)) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                        String id = docId.split(":")[1];
                        String selection = MediaStore.Images.Media._ID + "=" + id;
                        photoPath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                    } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                        photoPath = getImagePath(contentUri, null);
                    }
                } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                    photoPath = getImagePath(uri, null);
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    photoPath = uri.getPath();
                } else {
                    photoPath = null;
                }
                if (photoPath != null) {
                    Uri imageUri = Uri.fromFile(new File(photoPath));
                    cropImageUri(imageUri, CROP_PHOTO);
                }
                break;
            case TAKE_PHOTO:
                Uri uriCamera = Uri.fromFile(new File(photoPath));
                cropImageUri(uriCamera, CROP_PHOTO);
                break;
            case CROP_PHOTO:
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected void onPreExecute() {
                        showProgressDialog("正在识别数据...");
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            return ImageToStringUtil.parseImageToString(CROP_CACHE_DIR + "/temp.jpg");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        mDialog.dismiss();
                        if (result == null) {
                            Toast.makeText(EditTextActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                        } else {
                            mAuthorEdit.append(result);
                        }
                    }
                }.execute();
                break;
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(message);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.show();
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void showCameraDialog() {
        new AlertDialog.Builder(this)
                .setItems(new String[]{"拍摄照片", "从相册选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openCamera();
                                break;
                            case 1:
                                openAlbum();
                                break;
                        }
                    }
                }).show();
    }

    private void showLanDownloadDialog() {
        new AlertDialog.Builder(this)
                .setTitle("下载语言包")
                .setMessage("没有文字识别所需的语言包，约50M，是否开始下载？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mReceiver = new DownloadManagerReceiver();
                        registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        mCameraButton.setClickable(false);
                        lanDownloadId = downloadLan();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void openCamera() {
        //调用系统相机拍摄照片
        //照片文件存储路径
        Uri uri = null;
        File dir = new File(CROP_CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File outputImage = new File(dir, "image.jpg");
        if (outputImage.exists()) {
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
            photoPath = outputImage.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, "com.nisoft.inspectortools.fileprovider", outputImage);
        } else {
            uri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void cropImageUri(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        File dir = new File(CROP_CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(CROP_CACHE_DIR, "temp.jpg")));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    private long downloadLan() {
        String address = HttpUtil.ADDRESS_MAIN + "chi_sim.traineddata";
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(address));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("下载语言包");
        File file = new File(TESS_BASE_PATH + "tessdata/",
                DEFAULT_LANGUAGE + ".traineddata");
        request.setDestinationUri(Uri.fromFile(file));
        long downloadId = downloadManager.enqueue(request);
        return downloadId;
    }

    class DownloadManagerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (completeDownloadId == lanDownloadId) {
                mCameraButton.setClickable(true);
                if (mReceiver != null) {
                    unregisterReceiver(mReceiver);
                }
            }
//            String action = intent.getAction();
//            if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
//                Intent downloadIntent = new Intent();
//                startActivity(intent);
//            }
        }
    }
}
