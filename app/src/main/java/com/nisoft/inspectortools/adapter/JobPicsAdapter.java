package com.nisoft.inspectortools.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.inspect.MaterialInspectRecode;
import com.nisoft.inspectortools.bean.org.UserLab;
import com.nisoft.inspectortools.ui.base.UpdatePhotoMenuFragment;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.HttpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/22.
 */

public class JobPicsAdapter extends RecyclerView.Adapter<JobPicsAdapter.ViewHolder> {
    public static final String RECODE_FOLDER_ADDRESS = "http://47.93.191.62:8080/InspectorToolsServer/recode/";
    private Fragment mFragment;
    private Context mContext;

    private ArrayList<String> mPicsPath;
    private int mImageLayoutId;
    private String mRootPath;
    private MaterialInspectRecode mRecode;
    private ArrayList<String> mImagesName;
    private boolean editable;

    private MediaScannerConnection conn;
    private static final String FILE_TYPE = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg");
    private String scanPath;
    private ProgressDialog mProgressDialog;

    public JobPicsAdapter(Fragment fragment, int imageLayoutId, String rootPath) {
        mFragment = fragment;
        mContext = mFragment.getActivity();
        mImageLayoutId = imageLayoutId;
        mRootPath = rootPath;
        refreshPath();
    }

    public JobPicsAdapter(Fragment fragment, int imageLayoutId, MaterialInspectRecode recode, String rootPath) {
        mFragment = fragment;
        mContext = mFragment.getActivity();
        mImageLayoutId = imageLayoutId;
        mRootPath = rootPath;
        mRecode = recode;
        mImagesName = recode.getImagesName();
        resetPath();
    }

    public void setRecode(MaterialInspectRecode recode) {
        mRecode = recode;
        mImagesName = recode.getImagesName();
        resetPath();
    }

    public void refreshPath() {
        mPicsPath = new ArrayList<>();
        if (mRootPath==null||mRootPath==""){
            return;
        }
        File file = new File(mRootPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        String[] picsName = file.list();
        for (int i = 0; i < picsName.length; i++) {
            String[] strings = picsName[i].split("\\.");
            String type = strings[strings.length - 1];
            if (type.equals("jpg") || type.equals("bmp")||type.equals("png")) {
                mPicsPath.add(mRootPath + picsName[i]);
            }
        }
    }

    public void resetPath() {
        File dir = new File(mRootPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        ArrayList<String> localImageFilesName = new ArrayList<>();
        String[] picsName = dir.list();
        for (int i = 0; i < picsName.length; i++) {
            String[] strings = picsName[i].split("\\.");
            String type = strings[strings.length - 1];
            if (type.equals("jpg") || type.equals("bmp")) {
                localImageFilesName.add(picsName[i]);
            }
        }
        ArrayList<String> imagesUrl = new ArrayList<>();
        Log.e("mImagesName", mImagesName.toString());
        for (String name : mImagesName) {
            if (localImageFilesName.indexOf(name) >= 0) {
                imagesUrl.add(mRootPath + name);
            } else {
                imagesUrl.add(RECODE_FOLDER_ADDRESS +
                        UserLab.getUserLab(mContext).getEmployee().getCompanyId() + "/" +
                        mRecode.getType() + "/" +
                        mRecode.getJobNum() + "/"
                        + name);
            }
        }
        for (String name : localImageFilesName) {
            if (mImagesName.indexOf(name) < 0) {
                imagesUrl.add(mRootPath + name);
            }
        }
        mPicsPath = imagesUrl;
        Log.e("imagePath", mPicsPath.toString());
    }

    public ArrayList<String> getPath() {
        return mPicsPath;
    }

    @Override
    public JobPicsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mImageLayoutId, parent, false);
        final JobPicsAdapter.ViewHolder holder = new JobPicsAdapter.ViewHolder(view);
        holder.mPicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                if (position == getItemCount() - 1 && editable) {
                    //启动添加图片对话框（拍照或从相册选择）
                    FragmentManager manager = ((Activity) mContext).getFragmentManager();
                    UpdatePhotoMenuFragment fragment = UpdatePhotoMenuFragment.newInstance(position, mRootPath, false);
                    fragment.setTargetFragment(mFragment, 1);
                    fragment.show(manager, "update_menu");
                } else if (position != getItemCount() - 1) {
                    //查看大图，仿朋友圈查看大图
//                    FragmentManager manager = ((Activity) mContext).getFragmentManager();
//                    LargePhotoFragment imageFragment = LargePhotoFragment.newInstance(position,mPicsPath);
//                    imageFragment.setTargetFragment(((Activity) mContext).getFragmentManager().findFragmentById(R.id.fragment_content), 2);
//                    imageFragment.show(manager, "image");
//                    FileUtil.openImageFile(mPicsPath.get(position),mContext);
                    if (isHttpUrl(mPicsPath.get(position))) {
                        if (mProgressDialog == null) {
                            mProgressDialog = new ProgressDialog(mContext);
                        }
                        mProgressDialog.setMessage("正在下载图片...");
                        mProgressDialog.show();
                        HttpUtil.sendGetRequest(mPicsPath.get(position), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        mProgressDialog.dismiss();
                                        Toast.makeText(mContext, "网络连接失败！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                InputStream is = null;
                                FileOutputStream fos = null;
                                final String downloadPath = mRootPath + getImageName(mPicsPath.get(position));
                                File file = new File(downloadPath);
                                byte[] buffer = new byte[1024];
                                int len = 0;

                                try {
                                    is = response.body().byteStream();
                                    fos = new FileOutputStream(file);
                                    while ((len = is.read(buffer)) != -1) {
                                        fos.write(buffer, 0, len);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    fos.close();
                                    is.close();
                                }
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressDialog.dismiss();
                                        Toast.makeText(mContext, "下载完成！", Toast.LENGTH_SHORT).show();
                                        mPicsPath.set(position, downloadPath);
                                        scanPath = mPicsPath.get(position);
                                        startScan();
                                    }
                                });

                            }
                        });
                    } else {
                        scanPath = mPicsPath.get(position);
                        startScan();
                    }
                }
            }
        });

        holder.mPicImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != getItemCount() - 1 && editable) {
                    //启动添加图片对话框（拍照或从相册选择）
                    FragmentManager manager = ((Activity) mContext).getFragmentManager();
                    UpdatePhotoMenuFragment fragment = UpdatePhotoMenuFragment.newInstance(position, mRootPath, true);
                    fragment.setTargetFragment(mFragment, 1);
                    fragment.show(manager, "update_menu");
                }
                return true;
            }
        });


        return holder;
    }

    /***
     *
     * @param url 文件的路径或网址
     * @return 文件名
     */
    private String getImageName(String url) {
        String[] strings = url.split("/");
        return strings[strings.length - 1];
    }

    @Override
    public void onBindViewHolder(JobPicsAdapter.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            holder.mPicImage.setImageResource(R.drawable.ic_btn_add_photo);
        } else {
            Glide.with(mContext).load(mPicsPath.get(position)).into(holder.mPicImage);
        }
    }

    @Override
    public int getItemCount() {
        if (mPicsPath == null) {
            return 1;
        }
        return mPicsPath.size() + 1;
    }


    private void startScan() {
        if (conn != null) {
            conn.disconnect();
        }
        conn = new MediaScannerConnection(mContext, new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                conn.scanFile(scanPath, FILE_TYPE);
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                try {
                    if (uri != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        mContext.startActivity(intent);
                    }
                } finally {
                    conn.disconnect();
                    conn = null;
                }
            }
        });
        conn.connect();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mPicImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mPicImage = (ImageView) itemView;
        }
    }

    private boolean isHttpUrl(String url) {
        if (url.startsWith("http")) {
            return true;
        }
        return false;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
