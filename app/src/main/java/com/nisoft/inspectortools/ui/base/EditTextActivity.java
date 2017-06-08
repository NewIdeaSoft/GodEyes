package com.nisoft.inspectortools.ui.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.nisoft.inspectortools.utils.ImageToStringUtil;
import com.nisoft.inspectortools.utils.JsonParser;

import java.io.File;
import java.io.IOException;

import static com.nisoft.inspectortools.ui.base.UpdatePhotoMenuFragment.CHOOSE_PHOTO;
import static com.nisoft.inspectortools.utils.ImageToStringUtil.DEFAULT_LANGUAGE;
import static com.nisoft.inspectortools.utils.ImageToStringUtil.TESS_BASE_PATH;

public class EditTextActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 13;
    private EditText mAuthorEdit;
    private ImageButton mSpeechButton;
    private ImageButton mCameraButton;
    private static final String APPID = "59363ca2";
    private RecognizerDialogListener mRecognizerDialogListener;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        mAuthorEdit = (EditText) findViewById(R.id.edit_text_author);
        mSpeechButton = (ImageButton) findViewById(R.id.input_speech);
        mCameraButton = (ImageButton) findViewById(R.id.input_camera);
        SpeechUtility.createUtility(EditTextActivity.this, SpeechConstant.APPID + "=" + APPID);
        String initText = getIntent().getStringExtra("initText");
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
                    new AsyncTask<Void, Void, Boolean>() {
                        @Override
                        protected void onPreExecute() {
                            Toast.makeText(EditTextActivity.this, "开始加载语言包", Toast.LENGTH_SHORT).show();
                            showProgressDialog("正在传输数据...");
                        }
                        @Override
                        protected Boolean doInBackground(Void... params) {
                            ImageToStringUtil.ResourceToFile(EditTextActivity.this, R.raw.chi_sim, TESS_BASE_PATH + "tessdata/",
                                    DEFAULT_LANGUAGE + ".traineddata");
                            return true;
                        }

                        @Override
                        protected void onPostExecute(Boolean result) {
                            mDialog.dismiss();
                        }
                    }.execute();
                }
                openAlbum();
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 12);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 14);
        }

    }

    @TargetApi(23)
    private void requestPermissionSettings() {
        if (!Settings.System.canWrite(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
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
            if (!contentEdit.equals("")) {
                Intent intent = new Intent();
                intent.putExtra("content_edit", contentEdit);
                int content_position = getIntent().getIntExtra("content_position", -1);
                if (content_position > -1) {
                    intent.putExtra("content_position", content_position);
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                setResult(Activity.RESULT_CANCELED);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    private String photoPath;

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
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected void onPreExecute() {
                        Toast.makeText(EditTextActivity.this, "开始识别文字", Toast.LENGTH_SHORT).show();
                        showProgressDialog("正在识别文字...");
                    }
                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            return ImageToStringUtil.parseImageToString(photoPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return "出现错误啦！";
                    }
                    @Override
                    protected void onPostExecute(String result) {
                        mDialog.dismiss();
                        mAuthorEdit.append(result);
                    }
                }.execute();
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

    /***
     *  调用系统图片编辑进行裁剪
     * @param uri
     * @param imagePath
     * @param requestCode
     */
    public void startPhotoCrop(Uri uri,String imagePath,int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(imagePath, "temp_cropped.jpg")));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    public void chooseAndCropPhoto(String imagePath,int requestCode){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(imagePath, "temp_cropped.jpg")));
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }
}
