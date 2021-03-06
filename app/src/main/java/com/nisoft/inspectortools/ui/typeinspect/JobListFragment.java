package com.nisoft.inspectortools.ui.typeinspect;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.inspect.MaterialInspectRecode;
import com.nisoft.inspectortools.bean.inspect.PicsLab;
import com.nisoft.inspectortools.db.inspect.PicsCursorWrapper;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.GsonUtil;
import com.nisoft.inspectortools.utils.HttpUtil;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.nisoft.inspectortools.ui.strings.RecodeTypesStrings.KEY_SELECTED_TYPE;
import static com.nisoft.inspectortools.ui.strings.RecodeTypesStrings.RECODE_TYPE_CHI;
import static com.nisoft.inspectortools.ui.strings.RecodeTypesStrings.RECODE_TYPE_ENG;

/**
 * Created by Administrator on 2017/5/22.
 */

public class JobListFragment extends Fragment {
    private ListView jobListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<String> mJobNumList;
    private JobListAdapter mAdapter;
    private FloatingActionButton mFloatButton;
    private ProgressDialog mDialog;
    private ArrayList<MaterialInspectRecode> mPics;
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private String mJobType;
    private int mWhichType = -1;

    public static JobListFragment newInstance(int whichType) {
        JobListFragment fragment = new JobListFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_SELECTED_TYPE, whichType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mWhichType = getArguments().getInt(KEY_SELECTED_TYPE, -1);
        setJobNumList();
        mAdapter = new JobListAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.job_list_fragment, container, false);
        mJobType = RECODE_TYPE_ENG[mWhichType];
        if (mJobType != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(RECODE_TYPE_CHI[mWhichType]);
        }
        setHasOptionsMenu(true);
        jobListView = (ListView) view.findViewById(R.id.job_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllRecodeFromServer();
            }
        });
        jobListView.setAdapter(mAdapter);
        jobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WorkingActivity.class);
                intent.putExtra("job_num", mJobNumList.get(position));
                intent.putExtra(KEY_SELECTED_TYPE, mWhichType);
                startActivity(intent);
            }
        });
        mFloatButton = (FloatingActionButton) view.findViewById(R.id.new_problem_fab);
        mDialog = new ProgressDialog(getActivity());
        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody body = new FormBody.Builder()
                        .add("intent", "new")
                        .add("type", mJobType)
                        .build();
                DialogUtil.showProgressDialog(getActivity(), mDialog, "正在获取新的检验编号...");
                HttpUtil.sendPostRequest(HttpUtil.SERVLET_MATERIAL_RECODE, body, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String jobNum = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Intent intent = new Intent(getActivity(), WorkingActivity.class);
                                intent.putExtra(KEY_SELECTED_TYPE, mWhichType);
                                intent.putExtra("job_num", jobNum);
                                intent.putExtra("isNewJob", true);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
        getAllRecodeFromServer();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        mSearchView.setQueryHint("搜索" + RECODE_TYPE_CHI[mWhichType]);
        mSearchAutoComplete.setThreshold(1);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                PicsCursorWrapper cursor = PicsLab.getPicsLab(getActivity())
                        .queryPicsByTwo(mJobType, newText);
                ArrayList<MaterialInspectRecode> pics = PicsLab.getPicsLab(getActivity()).getAllPics(cursor);
                ArrayList<String> jobsNum = new ArrayList<>();
                for (MaterialInspectRecode pic : pics) {
                    jobsNum.add(pic.getJobNum());
                }
                mJobNumList = jobsNum;
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            getAllRecodeFromServer();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void getAllRecodeFromServer() {
        RequestBody body = new FormBody.Builder()
                .add("type", mJobType)
                .add("intent", "query")
                .build();
        DialogUtil.showProgressDialog(getActivity(), mDialog, "正在同步列表...");
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_MATERIAL_RECODE, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(getActivity(), "连接网络失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("listJson:", result);
                final ArrayList<String> newNumList = StringFormatUtil.getStrings(result);
                for (String s : newNumList) {
                    if (mJobNumList.indexOf(s) < 0) {
                        MaterialInspectRecode pics = new MaterialInspectRecode();
                        pics.setJobNum(s);
                        pics.setType(mJobType);
                        PicsLab.getPicsLab(getActivity()).insertPics(pics);
                        mJobNumList.add(s);
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        for (String s : mJobNumList) {
                            if (newNumList.indexOf(s) < 0) {
                                MaterialInspectRecode pics = new MaterialInspectRecode();
                                pics.setJobNum(s);
                                pics.setType(mJobType);
                                uploadJob(pics);
                            }
                        }
                        Collections.sort(mJobNumList, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                String[] strings = new String[]{o1, o2};
                                Arrays.sort(strings);
                                if (strings[0] == o1) {
                                    return 1;
                                }
                                return 0;
                            }
                        });
                        mAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "同步完成！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setJobNumList();
        mAdapter.notifyDataSetChanged();
    }

    private void setJobNumList() {
        mPics = PicsLab.getPicsLab(getActivity()).getAllPics();
        mJobNumList = new ArrayList<>();
        String jobType = mJobType;
        for (MaterialInspectRecode pic : mPics) {
            if (pic.getType().equals(jobType)) {
                mJobNumList.add(pic.getJobNum());
            }
        }
    }

    private void uploadJob(MaterialInspectRecode recode) {
        PicsLab.getPicsLab(getActivity()).updatePics(recode, recode.getJobNum());
        Gson gson = GsonUtil.getDateFormatGson();
        String jobJson = gson.toJson(recode);
        RequestBody body = new FormBody.Builder()
                .add("intent", "upload")
                .add("job_json", jobJson)
                .build();

        DialogUtil.showProgressDialog(getActivity(), mDialog, "正在上传数据...");
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_MATERIAL_RECODE, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(getActivity(), "获取网络连接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        if (result.equals("OK")) {
                            Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
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

}
