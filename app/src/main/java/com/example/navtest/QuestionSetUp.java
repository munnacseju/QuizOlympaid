package com.example.navtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.navtest.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuestionSetUp extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    private EditText questionEditText, option1EditText, option2EditText, option3EditText, option4EditText;
    private Button submitButton, setOlympaidshedule, removeQuestionSet;
    private Spinner selectOptionSpinner, currectOptionSpinner;
    private String practice, currectOption;
    private String[] practiceList = {"practice1", "practice2", "practice3", "practice4", "practice5",
            "practice6", "practice7", "practice8", "practice9", "practice10", "practice11", "practice12", "olympaid"};
    private String[] optionList = {"A", "B", "C", "D"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set_up);

        setTitle("Question Set Up");


        removeQuestionSet = findViewById(R.id.removeQuestionSetid);
        questionEditText = findViewById(R.id.questionEditTextid);
        option1EditText = findViewById(R.id.option1EditTextid);
        option2EditText = findViewById(R.id.option2EditTextid);
        option3EditText = findViewById(R.id.option3EditTextid);
        option4EditText = findViewById(R.id.option4EditTextid);
        submitButton = findViewById(R.id.submitButtonid);
        currectOptionSpinner = findViewById(R.id.currectOptionSpinnerid);
        selectOptionSpinner = findViewById(R.id.selectSpinnerid);
        setOlympaidshedule = findViewById(R.id.setOlympaidsheduleid);


        ArrayAdapter<String> selectOptionArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, practiceList);
        selectOptionSpinner.setAdapter(selectOptionArrayAdapter);

        ArrayAdapter<String> currectOptionArayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, optionList);
        currectOptionSpinner.setAdapter(currectOptionArayAdapter);

        selectOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                practice = practiceList[i];
                Toast.makeText(getApplicationContext(), practiceList[i] + " Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "Select None", Toast.LENGTH_SHORT).show();

            }
        });

        currectOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currectOption = optionList[i];
                Toast.makeText(getApplicationContext(), optionList[i] + " Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "Select None", Toast.LENGTH_SHORT).show();

            }
        });



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String question = questionEditText.getText().toString().trim();
                String option1 = option1EditText.getText().toString().trim();
                String option2 = option2EditText.getText().toString().trim();
                String option3 = option3EditText.getText().toString().trim();
                String option4 = option4EditText.getText().toString().trim();

                databaseReference = firebaseDatabase.getReference(practice);

                String key = databaseReference.push().getKey();
                QuestionSetUpHandle questionSetUpHandle = new QuestionSetUpHandle(question, option1, option2, option3, option4, currectOption);
                databaseReference.child(key).setValue(questionSetUpHandle);


                Toast.makeText(getApplicationContext(), "Question has been Submitted!", Toast.LENGTH_SHORT).show();
            }
        });

        setOlympaidshedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OlympaidShedule.class);
                startActivity(intent);
            }
        });

        removeQuestionSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = firebaseDatabase.getReference(practice);
                databaseReference.removeValue();
                Toast.makeText(getApplicationContext(), practice+" question set is successfully removed!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
