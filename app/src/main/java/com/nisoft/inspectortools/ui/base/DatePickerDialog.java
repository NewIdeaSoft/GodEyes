package com.nisoft.inspectortools.ui.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.nisoft.inspectortools.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2017/5/22.
 */

public class DatePickerDialog extends DialogFragment {
    public static final String REQUEST_POSITION = "request_position";
    public static final String DATE_INITIALIZE = "date_initialize";
    private Date mDate;

    public static DatePickerDialog newInstance(int position,Date date) {
        Bundle args = new Bundle();
        args.putInt(REQUEST_POSITION, position);
        args.putSerializable(DATE_INITIALIZE,date);
        DatePickerDialog datePickerDialog = new DatePickerDialog();
        datePickerDialog.setArguments(args);
        return datePickerDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.date_picker_dialog, null);
        mDate = (Date) getArguments().getSerializable(DATE_INITIALIZE);
        final int position=getArguments().getInt(REQUEST_POSITION);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("选择日期")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        sendResult(Activity.RESULT_OK);
                        FragmentManager fm = getFragmentManager();

                        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(mDate,position);
                        timePickerDialog.setTargetFragment(getTargetFragment(),getTargetRequestCode());
                        timePickerDialog.show(fm,"time_picker");
                    }
                })
                .create();
    }
}
