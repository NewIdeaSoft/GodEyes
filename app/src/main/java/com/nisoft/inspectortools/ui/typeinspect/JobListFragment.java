package com.nisoft.inspectortools.ui.typeinspect;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.inspect.InspectRecodePics;
import com.nisoft.inspectortools.bean.inspect.PicsLab;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/22.
 */

public class JobListFragment extends Fragment {
    private ListView jobListView;
    private ArrayList<String> mJobNumList;
    private JobListAdapter mAdapter;
    private FloatingActionButton mFloatButton;
    private ArrayList<InspectRecodePics> mPics;

    public static JobListFragment newInstance(String inspectType){
        JobListFragment fragment = new JobListFragment();
        Bundle args  = new Bundle();
        args.putString(ChooseRecodeTypeFragment.INSPECT_TYPE,inspectType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mPics = PicsLab.getPicsLab(getActivity()).getAllPics();
        Log.e("TAG",mPics.size()+"");
        mJobNumList = new ArrayList<>();
        for (InspectRecodePics pic : mPics){
            mJobNumList.add(pic.getJobNum());

        }
        Log.e("TAG",mJobNumList.size()+"");
        mAdapter = new JobListAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.job_list_fragment, container, false);
        jobListView = (ListView) view.findViewById(R.id.job_list);
        jobListView.setAdapter(mAdapter);
        jobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),WorkingActivity.class);
                intent.putExtra("job_num",mJobNumList.get(position));
                intent.putExtra(ChooseRecodeTypeFragment.INSPECT_TYPE,getArguments().getString(ChooseRecodeTypeFragment.INSPECT_TYPE));
                startActivity(intent);
            }
        });
        mFloatButton = (FloatingActionButton) view.findViewById(R.id.new_problem_fab);
        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),WorkingActivity.class);
                intent.putExtra(ChooseRecodeTypeFragment.INSPECT_TYPE,getArguments().getString(ChooseRecodeTypeFragment.INSPECT_TYPE));
                startActivity(intent);
            }
        });
        return view;
    }

    public class JobListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mJobNumList.size();
        }

        @Override
        public Object getItem(int position) {
            return mJobNumList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.job_list_item, null);
                holder = new ViewHolder();
                holder.mJobNumText = (TextView) convertView.findViewById(R.id.job_num_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mJobNumText.setText(mJobNumList.get(position));

            return convertView;
        }

        class ViewHolder {
            TextView mJobNumText;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPics = PicsLab.getPicsLab(getActivity()).getAllPics();
        mJobNumList = new ArrayList<>();
        for (InspectRecodePics pic : mPics){
            mJobNumList.add(pic.getJobNum());
        }
        mAdapter.notifyDataSetChanged();
    }
}
