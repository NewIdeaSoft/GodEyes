package com.nisoft.inspectortools.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.OrgInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/19.
 */

public class OrgInfoAdapter extends BaseAdapter {
    private int mMaxCount;
    private ArrayList<OrgInfo> mSelectedOrgInfos;
    private Context mContext;

    @Override
    public int getCount() {
        return mSelectedOrgInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mSelectedOrgInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.list_tem_org_item, null);
        Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner_org_item);
        if (mMaxCount > getCount()) {
            if (position == getCount() + 1) {
                TextView textView = new TextView(mContext);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText("点击选择下级单位名称");
                ((LinearLayout) convertView).removeAllViews();
                ((LinearLayout) convertView).addView(textView);
            }
        }


        return null;
    }

}
