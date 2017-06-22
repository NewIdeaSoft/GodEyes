package com.nisoft.inspectortools.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.ui.base.UpdatePhotoMenuFragment;

import java.io.File;

/**
 * Created by Administrator on 2017/6/22.
 */

public class JobPicsAdapter extends RecyclerView.Adapter<JobPicsAdapter.ViewHolder> {
    private Fragment mFragment;
    private Context mContext;
    private String[] mPicsPath;
    private int mImageLayoutId;
    private String mRootPath;
    private MediaScannerConnection conn;
    private static final String FILE_TYPE = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg");
    private String scanPath;

    public JobPicsAdapter(Fragment fragment, int imageLayoutId, String rootPath) {
        mFragment = fragment;
        mContext = mFragment.getActivity();
        mImageLayoutId = imageLayoutId;
        mRootPath = rootPath;
        refreshPath();
    }

    public void refreshPath() {
        File file = new File(mRootPath);
        if (!file.exists()){
            file.mkdirs();
        }
        mPicsPath = file.list();
    }
    public String[] getPath(){
        return mPicsPath;
    }
    @Override
    public JobPicsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mImageLayoutId, parent, false);
        final JobPicsAdapter.ViewHolder holder = new JobPicsAdapter.ViewHolder(view);
        holder.mPicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position == getItemCount() - 1) {
                    //启动添加图片对话框（拍照或从相册选择）

                    FragmentManager manager = ((Activity) mContext).getFragmentManager();
                    UpdatePhotoMenuFragment fragment = UpdatePhotoMenuFragment.newInstance(position, mRootPath, false);
                    fragment.setTargetFragment(mFragment, 1);
                    fragment.show(manager, "update_menu");


                } else {
                    //查看大图，仿朋友圈查看大图
//                    FragmentManager manager = ((Activity) mContext).getFragmentManager();
//                    LargePhotoFragment imageFragment = LargePhotoFragment.newInstance(position,mPicsPath);
//                    imageFragment.setTargetFragment(((Activity) mContext).getFragmentManager().findFragmentById(R.id.fragment_content), 2);
//                    imageFragment.show(manager, "image");
//                    FileUtil.openImageFile(mPicsPath.get(position),mContext);
                    scanPath = mPicsPath[position];
                    startScan();
                }
            }
        });
        holder.mPicImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != getItemCount() - 1) {
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

    @Override
    public void onBindViewHolder(JobPicsAdapter.ViewHolder holder, int position) {
        if (position == mPicsPath.length) {
            holder.mPicImage.setImageResource(R.drawable.ic_btn_add_photo);
        } else {
            Glide.with(mContext).load(mPicsPath[position]).into(holder.mPicImage);
        }
    }

    @Override
    public int getItemCount() {
        return mPicsPath.length + 1;
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
}
