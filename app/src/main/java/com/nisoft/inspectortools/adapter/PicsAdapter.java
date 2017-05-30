package com.nisoft.inspectortools.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.ui.base.LargePhotoFragment;
import com.nisoft.inspectortools.ui.base.UpdatePhotoMenuFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/22.
 */

public class PicsAdapter extends RecyclerView.Adapter<PicsAdapter.ViewHolder> {
    private Fragment mFragment;
    private Context mContext;
    private ArrayList<String> mPicsPath;
    private int mImageLayoutId;
    private String mRootPath;

    public PicsAdapter(Fragment fragment, ArrayList<String> picsPath, int imageLayoutId, String rootPath) {
        mFragment = fragment;
        mContext = mFragment.getActivity();
        if (picsPath == null) {
            mPicsPath = new ArrayList<>();
        } else {
            mPicsPath = picsPath;
        }
        mImageLayoutId = imageLayoutId;
        mRootPath = rootPath;
    }

    public void setPicsPath(ArrayList<String> picsPath) {
        mPicsPath = picsPath;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mImageLayoutId, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.mPicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position == getItemCount() - 1) {
                    //启动添加图片对话框（拍照或从相册选择）

                    FragmentManager manager = ((Activity) mContext).getFragmentManager();
                    UpdatePhotoMenuFragment fragment = UpdatePhotoMenuFragment.newInstance(position,mRootPath);
                    fragment.setTargetFragment(mFragment, 1);
                    fragment.show(manager, "update_menu");


                } else {
                    //查看大图，仿朋友圈查看大图
                    FragmentManager manager = ((Activity) mContext).getFragmentManager();
                    LargePhotoFragment imageFragment = LargePhotoFragment.newInstance(position);
                    imageFragment.setTargetFragment(((Activity) mContext).getFragmentManager().findFragmentById(R.id.fragment_content), 2);
                    imageFragment.show(manager, "image");
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
                    UpdatePhotoMenuFragment fragment = UpdatePhotoMenuFragment.newInstance(position,mRootPath);
                    fragment.setTargetFragment(mFragment, 1);
                    fragment.show(manager, "update_menu");
                }
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == mPicsPath.size()) {
            holder.mPicImage.setImageResource(R.drawable.add_1);
        } else {
            Glide.with(mContext).load(mPicsPath.get(position)).into(holder.mPicImage);
        }
    }

    @Override
    public int getItemCount() {
        return mPicsPath.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mPicImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mPicImage = (ImageView) itemView;
        }
    }
}
