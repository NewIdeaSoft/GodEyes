package com.nisoft.inspectortools.ui.typeinspect;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.inspect.InspectRecodePics;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/5/12.
 */

public class LargePhotoFragment extends DialogFragment {
    private static final String SELECTED_POSITION = "selected_position";
    private InspectRecodePics mRecodePics;
    private ArrayList<Fragment> mFragments;
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    public static LargePhotoFragment newInstance(int position){
        Bundle args = new Bundle();
        args.putInt(SELECTED_POSITION,position);
        LargePhotoFragment fragment = new LargePhotoFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        return fragment;
    }
    public LargePhotoFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_large_photo,container);
        mRecodePics = WorkingFragment.getsRecodePics();
        ArrayList<String> photosPath = mRecodePics.getPicPath();
        mFragments = new ArrayList<>();
        for(int i = 0; i < photosPath.size(); i++) {
            mFragments.add(PhotoFragment.newInstance(photosPath.get(i)));
        }
        mViewPager = (ViewPager) view.findViewById(R.id.pager_large_photo);
        mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(getArguments().getInt(SELECTED_POSITION));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPagerAdapter.notifyDataSetChanged();
    }

    public ArrayList<Fragment> getFragments() {
        return mFragments;
    }



    class FragmentPagerAdapter extends FragmentStatePagerAdapter{

        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
