package com.example.android.alarmapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import java.util.Calendar;

/**
 * Created by Afnan A. A. Abed on 9/8/2018.
 */

public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDatePickListener mCallback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return new DatePickerDialog(getContext(), this, year, month+1, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCallback.onDateSelected(dayOfMonth, month, year);
    }

    public interface OnDatePickListener {
        void onDateSelected(int day, int month, int year);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnDatePickListener) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException("must implement the OnDatePickListener interface");
        }
    }
}
