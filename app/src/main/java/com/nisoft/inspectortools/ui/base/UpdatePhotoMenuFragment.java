package com.nisoft.inspectortools.ui.base;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.ui.typeinspect.WorkingFragment;
import com.nisoft.inspectortools.utils.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by NewIdeaSoft on 2017/5/3.
 */

public class UpdatePhotoMenuFragment extends DialogFragment {
    public static final int TAKE_PHOTO = 2;
    public static final int CHOOSE_PHOTO = 3;
    public static String IMAGE_POSITION = "image_position";
    public static String IMAGE_ROOTPATH = "image_rootpath";


    public static UpdatePhotoMenuFragment newInstance(int position,String rootPath) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_POSITION, position);
        args.putString(IMAGE_ROOTPATH,rootPath);
        UpdatePhotoMenuFragment fragment = new UpdatePhotoMenuFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return fragment;
    }

    private Button mMakePhoto;
    private Button mChoosePhoto;
    private String path;
    private int position;
    private String jobNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container);
        position = getArguments().getInt(IMAGE_POSITION);
        jobNum = WorkingFragment.getsRecodePics().getJobNum();
        mMakePhoto = (Button) view.findViewById(R.id.make_picture);
        mChoosePhoto = (Button) view.findViewById(R.id.choose_picture);

        mMakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();

            }
        });
        mChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });
        return view;
    }

    private void openCamera() {
        //调用系统相机拍摄照片
        //照片文件存储路径
        Uri uri = null;
        File dir = new File(getArguments().getString(IMAGE_ROOTPATH));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File outputImage = new File(dir, position + ".jpg");
        path = null;
        if (outputImage.exists()) {
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
            path = outputImage.getAbsolutePath();
            Log.d("PhotoPath", path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(getActivity(), "com.nisoft.inspectortools.fileprovider", outputImage);
        } else {
            uri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 打开相册
     */
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

        if (getTargetFragment() == null) {
            return;
        }
        String photoPath = null;
        String targetPhotoPath = null;
        switch (requestCode) {
            case TAKE_PHOTO:
                targetPhotoPath = path;
                break;
            case CHOOSE_PHOTO:
                Uri uri = data.getData();
                if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
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
                }
                File dir = new File(getArguments().getString(IMAGE_ROOTPATH));
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                targetPhotoPath = getArguments().getString(IMAGE_ROOTPATH) + position + ".jpg";
                FileUtil.moveFile(photoPath, targetPhotoPath);
                photoPath = targetPhotoPath;
        }
        Intent i = new Intent();
        if (new File(targetPhotoPath).exists()){
            i.putExtra("PhotoPath", targetPhotoPath);
            i.putExtra("resourcePhotoPath",photoPath);
        }

        i.putExtra("position",position);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
        this.dismiss();
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }

        return path;
    }
}
