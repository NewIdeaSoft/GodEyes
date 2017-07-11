package com.nisoft.inspectortools.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nisoft.inspectortools.R;

/**
 * Created by Administrator on 2017/5/22.
 */

public class PhotoFragment extends Fragment {
    private static final String PHOTO_PATH = "photo_path";

    public static Fragment newInstance(String s) {
        Fragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString(PHOTO_PATH, s);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_pager, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_large_photo);
        Button button = (Button) view.findViewById(R.id.delete_photo);
        Glide.with(getActivity()).load(getArguments().getString(PHOTO_PATH)).into(imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
