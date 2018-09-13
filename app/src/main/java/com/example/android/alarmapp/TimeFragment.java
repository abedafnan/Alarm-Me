package com.example.android.alarmapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import java.util.Calendar;

/**
 * Created by Afnan A. A. Abed on 9/8/2018.
 */

public class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    OnTimePickListener mCallback;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getContext(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCallback.onTimeSelected(hourOfDay, minute);
    }

    public interface OnTimePickListener {
        void onTimeSelected(int hour, int minute);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnTimePickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement the OnDatePickListener interface");
        }
    }
}
