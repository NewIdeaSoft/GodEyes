package com.nisoft.inspectortools.ui.choosetype;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.ui.typeinspect.JobListActivity;
import com.nisoft.inspectortools.ui.typeproblem.ProblemListActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/29.
 */

public class ChooseRecodeTypeFragment extends Fragment {
    public static final String INSPECT_TYPE = "inspect_type";
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mTypes;
    private ArrayList<String> mTypes_eng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_job_type, container, false);
        mListView = (ListView) view.findViewById(R.id.recode_type_list);
        setTypes();
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mTypes);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent inspectIntent = new Intent(getActivity(), JobListActivity.class);
                Intent qualityProblemIntent = new Intent(getActivity(), ProblemListActivity.class);
                switch (position) {
                    case 0:
                        inspectIntent.putExtra(INSPECT_TYPE, mTypes_eng.get(0));
                        startActivity(inspectIntent);
                        break;
                    case 1:
                        inspectIntent.putExtra(INSPECT_TYPE, mTypes_eng.get(1));
                        startActivity(inspectIntent);
                        break;
                    case 2:
                        inspectIntent.putExtra(INSPECT_TYPE, mTypes_eng.get(2));
                        startActivity(inspectIntent);
                        break;
                    case 3:
                        startActivity(qualityProblemIntent);
                        break;
                    default:
                        break;

                }
            }
        });
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
        }
        return view;
    }

    private void setTypes() {
        mTypes = new ArrayList<>();
        mTypes_eng = new ArrayList<>();
        mTypes.add("金属材料");
        mTypes_eng.add("material/metal");
        mTypes.add("非金属材料");
        mTypes_eng.add("material/nonmetal");
        mTypes.add("外购件");
        mTypes_eng.add("material/purchased_parts");
        mTypes.add("质量问题");
        mTypes_eng.add("problem");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 11:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getActivity(), "未获得权限，应用无法正常使用", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
