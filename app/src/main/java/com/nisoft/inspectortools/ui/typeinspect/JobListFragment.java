package com.nisoft.inspectortools.ui.typeinspect;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.inspect.InspectRecodePics;
import com.nisoft.inspectortools.bean.inspect.PicsLab;
import com.nisoft.inspectortools.db.inspect.PicsCursorWrapper;
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
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private String mJobType;

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
        setJobNumList();
        mAdapter = new JobListAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.job_list_fragment, container, false);
        mJobType = getArguments().getString(ChooseRecodeTypeFragment.INSPECT_TYPE);
        if (mJobType!=null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mJobType);
        }
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        mSearchView.setQueryHint("搜索"+mJobType);
        mSearchAutoComplete.setThreshold(1);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                PicsCursorWrapper cursor = PicsLab.getPicsLab(getActivity())
                        .queryPicsByTwo(mJobType,newText);
                ArrayList<InspectRecodePics> pics = PicsLab.getPicsLab(getActivity()).getAllPics(cursor);
                ArrayList<String> jobsNum = new ArrayList<String>();
                for (InspectRecodePics pic : pics){
                    jobsNum.add(pic.getJobNum());
                }
                mJobNumList = jobsNum;
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        setJobNumList();
        mAdapter.notifyDataSetChanged();
    }

    private void setJobNumList(){
        mPics = PicsLab.getPicsLab(getActivity()).getAllPics();
        mJobNumList = new ArrayList<>();
        String jobType = getArguments().getString(ChooseRecodeTypeFragment.INSPECT_TYPE);
        for (InspectRecodePics pic : mPics){
            if(pic.getType()==null) {
                if(pic.getJobNum().split("-").length==2) {
                    pic.setType("原材料检验");
                }else {
                    pic.setType("外购件检验");
                }
                PicsLab.getPicsLab(getActivity()).updatePics(pic,pic.getJobNum());
            }
            if(pic.getType().equals(jobType)) {
                mJobNumList.add(pic.getJobNum());
            }
        }
    }
}
