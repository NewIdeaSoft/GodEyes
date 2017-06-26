package com.nisoft.inspectortools.ui.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TimePicker;

import com.nisoft.inspectortools.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2017/6/2.
 */

public class TimePickerDialog extends DialogFragment {
    public static final String DATE_VARIABLE = "date_variable";
    public static final String CLICK_POSITION = "click_position";
    private static final String DATE_OLD = "date_old";
    private Date mDate;
    private Date mOldDate;

    public static TimePickerDialog newInstance(Date date, Date oldDate,int position) {
        TimePickerDialog timePickerDialog = new TimePickerDialog();
        Bundle args = new Bundle();
        args.putSerializable(DATE_VARIABLE, date);
        args.putSerializable(DATE_OLD,oldDate);
        args.putInt(CLICK_POSITION, position);
        timePickerDialog.setArguments(args);
        return timePickerDialog;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.time_picker_dialog, null);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        mDate = (Date) getArguments().getSerializable(DATE_VARIABLE);
        mOldDate = (Date) getArguments().getSerializable(DATE_OLD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mDate = new GregorianCalendar(year, month, day, hourOfDay, minute).getTime();
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("选择时间")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDate.equals(mOldDate)){
                            sendResult(Activity.RESULT_CANCELED);
                        }else {
                            sendResult(Activity.RESULT_OK);
                        }
                    }
                })
                .create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent i = new Intent();
        i.putExtra(DatePickerDialog.DATE_INITIALIZE, mDate);
        i.putExtra(CLICK_POSITION, getArguments().getInt(CLICK_POSITION));
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
