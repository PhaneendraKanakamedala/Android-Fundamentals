/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.DateTimePickers;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass for the date picker.
 * Sets the current date for the picker using Calendar.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    /**
     * Creates the date picker dialog with the current date from Calendar.
     * @param savedInstanceState    Saved instance
     * @return DatePickerDialog     The date picker dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * Grabs the date and converts it to a string to pass
     * to the Main Activity in order to show it with onFinishDateDialog().
     * @param view  The date picker view
     * @param year  The year chosen
     * @param month The month chosen
     * @param day   The day chosen
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Convert the date elements to strings.
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        // Assign the concatenated strings to dateMessage.
        String dateMessage = (month_string + "/" + day_string + "/" + year_string);
        // Set the activity to the Main Activity.
        MainActivity activity = (MainActivity) getActivity();
        // Pass the dateMessage to the Main Activity's onFinishDateDialog() method.
        activity.onFinishDateDialog(dateMessage);
    }

}
