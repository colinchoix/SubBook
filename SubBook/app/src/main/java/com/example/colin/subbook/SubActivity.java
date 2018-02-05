/*SubActivity
 *
 * Version 1.0
 *
 * Feb 05, 2018
 *
 * Copyright 2018 Colin Choi, CMPUT301, University of Alberta - All Rights Reserved
 *
 */

package com.example.colin.subbook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Represents the sub activity for the app
 * This is where information for a new subscription is passed in and validated
 *
 * @author cechoi
 *
 * @version 1.0
 */
public class SubActivity extends AppCompatActivity {

    private EditText subName;
    private EditText subPrice;
    private EditText subComment;
    private String name;
    private Date startDate;
    private String price;
    private String comment;
    private Context context = this;
    private Button subDate;
    private Calendar myCalendar = Calendar.getInstance();
    private String dateFormat = "yyyy-MM-dd";
    private DatePickerDialog.OnDateSetListener date;
    private SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CANADA);

    /**
     * Called when activity is first created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        subDate = (Button) findViewById(R.id.subDate);
        subName = (EditText) findViewById(R.id.subName);
        subPrice = (EditText) findViewById(R.id.subPrice);
        subComment = (EditText) findViewById(R.id.subComment);

        // This code is for when the user is editing a subscription
        Intent intent = getIntent();
        final String index = intent.getStringExtra("index");
        //Checks if an index was pass in, meaning that user is trying to edit something
        if (index != null) {
            //Fills in the fields with the prexisting information
            String name = intent.getStringExtra("name");
            String date = intent.getStringExtra("date");
            String comment = intent.getStringExtra("comment");
            String price = intent.getStringExtra("price");
            subName.setText(name);
            subPrice.setText(price);
            subComment.setText(comment);
            subDate.setText(date);
        }

        // Taken from https://gist.github.com/gaara87/3607765
        // 2018-02-04
        // Limits the number of deimals allowed in input
        subPrice.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 5, afterDecimal = 2;
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = subPrice.getText() + source.toString();
                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }
                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });

        // Taken from http://www.moo-code.me/en/2017/04/16/how-to-popup-datepicker-calendar/
        // 2018-02-04
        // Makes a popup calendar when a button is clicked for the user to choose a date
        long currentdate = System.currentTimeMillis();
        String dateString = sdf.format(currentdate);
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };

        // onclick - popup datepicker
        subDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Return to previous activity and putExtra information when save button is clicked
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Taken from https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
                // 2018-02-04
                // Returns information needed to make a new subscription in the main activity
                int valid = validateSubscription();
                if (valid == 1) {
                    Intent returnIntent = new Intent();
                    name = subName.getText().toString();
                    price = subPrice.getText().toString();
                    comment = subComment.getText().toString();
                    returnIntent.putExtra("name", name);
                    returnIntent.putExtra("price", price);
                    returnIntent.putExtra("date", sdf.format(myCalendar.getTime()));
                    returnIntent.putExtra("comment", comment);
                    returnIntent.putExtra("index", index);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        // Return to previous activity when cancel button is clicked
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

    }

    private void updateDate() {
        subDate.setText(sdf.format(myCalendar.getTime()));
        subDate.setError(null);
    }

    /**
     * Validates all input fields are filled out correctly.
     * Sets an error if incorrect
     *
     * @return 1 if valid
     */
    private int validateSubscription() {
        if (!TextUtils.isEmpty(subName.getText()) &&
                !TextUtils.isEmpty(subPrice.getText()) &&
                !subDate.getText().toString().equals("Date") &&
                (TextUtils.getTrimmedLength(subName.getText()) <= 20) &&
                (TextUtils.getTrimmedLength(subComment.getText()) <= 30)) {
            return 1;
        } else {
            if (TextUtils.getTrimmedLength(subName.getText()) > 20) {
                subName.setError("Name must be less than 20 characters");
            }
            if (TextUtils.getTrimmedLength(subComment.getText()) > 30) {
                subComment.setError("Comment must be less than 30 characters");
            }
            if (TextUtils.isEmpty(subName.getText())) {
                subName.setError("Fields can not be left empty");
            }
            if (TextUtils.isEmpty(subPrice.getText())) {
                subPrice.setError("Fields can not be left empty");
            }
            if (subDate.getText().toString().equals("Date")) {

                subDate.setError("Enter a Date");
            } else {
                subDate.setError(null);
            }
        }
        return 0;
    }
}
