package com.example.navtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OlympaidShedule extends AppCompatActivity {

    private int date, month, year, hour;

    private Spinner dateSpinner, monthSpinner, yearSpinner, hourSpinner;
    private Button submitButton, goOlympaidButton;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference olympaidQuestionScheduleDatabaseReference = firebaseDatabase.getReference("questionSchedule");
    private DatabaseReference databaseReferenceForOlympaidPointTable = firebaseDatabase.getReference("olympaidPointTable");
    private ProgressBar progressBar;
    private OlympaidQuestionSheduleHandle olympaidQuestionSheduleHandle1;
    private TextView olympaidScheduleShowTextView;


    String[] dateList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11", "12", "13", "14", "15", "16", "17", "18", "19", "20","21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    String[] yearList = {"20","21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40","41", "42", "43", "44", "45", "46", "47", "48", "49", "50"};
    String[] monthList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12"};
    String[] hourList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11", "12", "13", "14", "15", "16", "17", "18", "19", "20","21", "22", "23", "24"};

    String dateListString = String.valueOf(dateList);

    //yearListString = yearList.toString(), monthListString = monthList.toString(), hourListString = hourList.toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olympaid_shedule);


        setTitle("Olympaid Schedule");

        olympaidScheduleShowTextView = findViewById(R.id.olympaidScheduleShowTextViewid);
        progressBar = findViewById(R.id.progressBarid);
        dateSpinner =  findViewById(R.id.selectdateSpinnerid);
        monthSpinner = findViewById(R.id.selectMontSpinnerid);
        yearSpinner = findViewById(R.id.selectYearSpinnerid);
        hourSpinner = findViewById(R.id.selectHourSpinnerid);
        submitButton = findViewById(R.id.submitButtonid);
        goOlympaidButton = findViewById(R.id.goOlympaidButtonid);

        ArrayAdapter<String> dateListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateList);
        dateSpinner.setAdapter(dateListAdapter);

        final ArrayAdapter<String> monthListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthList);
        monthSpinner.setAdapter(monthListAdapter);

        final ArrayAdapter<String> yearListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearSpinner.setAdapter(yearListAdapter);

        ArrayAdapter<String> hourListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hourList);
        hourSpinner.setAdapter(hourListAdapter);



        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                date = Integer.parseInt(dateList[i]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month = Integer.parseInt(monthList[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = Integer.parseInt(yearList[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hour = Integer.parseInt(hourList[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final OlympaidQuestionSheduleHandle olympaidQuestionSheduleHandle = new OlympaidQuestionSheduleHandle(date, month, year, hour);
                olympaidQuestionScheduleDatabaseReference.setValue(olympaidQuestionSheduleHandle);
                databaseReferenceForOlympaidPointTable.removeValue();
                progressBar.setVisibility(View.GONE);
            }
        });

        goOlympaidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("email", "nothing");
                Intent intent = new Intent(getApplicationContext(), Olympaid2.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();

            }
        });
    }

}
