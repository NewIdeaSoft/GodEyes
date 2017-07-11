package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.nisoft.inspectortools.ui.strings.RecodeTypesStrings;

/**
 * Created by Administrator on 2017/7/10.
 */

public class ProblemTypeDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("选择问题类型")
                .setItems(RecodeTypesStrings.PROBLEM_TYPE_CHI, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectItem = RecodeTypesStrings.PROBLEM_TYPE_CHI[which];
                        Intent intent = new Intent();
                        intent.putExtra("type", selectItem);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .create();
    }


}
