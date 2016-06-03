package com.ericsson.project.tescheduler.Tools;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import com.ericsson.project.tescheduler.Interfaces.GetUpdatedDate;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private GetUpdatedDate dateListener = null;
    private int callerViewID;
    private final Calendar temp = Calendar.getInstance();

    public void registerListener (GetUpdatedDate listener) {
        dateListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        callerViewID = this.getArguments().getInt("callerViewID");
        temp.setTime(new Date(this.getArguments().getLong("selectedTime")));
        int year = temp.get(Calendar.YEAR);
        int month = temp.get(Calendar.MONTH);
        int day = temp.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        //Minimum date to avoid selecting a past date
        dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        temp.set(Calendar.YEAR, year);
        temp.set(Calendar.MONTH, month);
        temp.set(Calendar.DAY_OF_MONTH, day);
        if (dateListener != null) {
            dateListener.updateDate(callerViewID, temp);
        }
    }
}
