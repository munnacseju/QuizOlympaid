package com.example.navtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navtest.Handle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Olympaid2 extends AppCompatActivity implements View.OnClickListener {
    private TextView question,  timeTextView, timeLimitTextView;
    private Button option1, option2, option3, option4,   nextQuiz;
    private int time=0;
    private double score=0;
    private String currectOption;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference, databaseReferenceForOlympaidPointTable, databaseReferenceForEmail;
    private SharedPreferences sharedPreferences;
    private int seconds = 0;
    private int fixedTime=30;


    Handle handle;

    DatabaseReference databaseReference1;
    private List<QuestionSetUpHandle> questionSetUpHandlesList;
    int questionSize, i = 0;
    private int level, practice;
    private String myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olympaid2);

        setTitle("Olympaid");

        databaseReferenceForOlympaidPointTable = firebaseDatabase.getReference("olympaidPointTable");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        myEmail = sharedPreferences.getString("email", "");


        level = sharedPreferences.getInt("level", 1);
        practice = getIntent().getIntExtra("practice", 3);
        //Toast.makeText(getApplicationContext(), "Level = "+level+"Practice Point = "+practice, Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplicationContext(), "You practice : "+ practice, Toast.LENGTH_SHORT).show();

        if(practice == 0){
            databaseReference = firebaseDatabase.getReference("olympaid");
        }
        else {
            String referance = "practice"+practice;
            databaseReference = firebaseDatabase.getReference("olympaid");
        }


        questionSetUpHandlesList = new ArrayList<>();



        question = findViewById(R.id.questionTextViewid);
        timeTextView = findViewById(R.id.timeTextViewid);
        timeLimitTextView = findViewById(R.id.timeLimitTextViewid);

        option1 = findViewById(R.id.option1Buttonid);
        option2 = findViewById(R.id.option2Buttonid);
        option3 = findViewById(R.id.option3Buttonid);
        option4 = findViewById(R.id.option4Buttonid);




        nextQuiz = findViewById(R.id.nextQuizid);

        databaseReference1 = firebaseDatabase.getReference(myEmail.replace(".", "rm"));
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                handle = dataSnapshot.getValue(Handle.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReferenceForEmail = firebaseDatabase.getReference(myEmail.replace(".", "rm"));
        databaseReferenceForEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                handle = dataSnapshot.getValue(Handle.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    QuestionSetUpHandle questionSetUpHandle = dataSnapshot1.getValue(QuestionSetUpHandle.class);
                    questionSetUpHandlesList.add(questionSetUpHandle);
                }
                //Toast.makeText(getApplicationContext(), "Your question is loaded compleatly", Toast.LENGTH_SHORT).show();
                findViewById(R.id.startOlympaidButtonId).setVisibility(View.VISIBLE);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
        nextQuiz.setOnClickListener(this);
        findViewById(R.id.seeScoreBoardButtonId).setOnClickListener(this);
        findViewById(R.id.startOlympaidButtonId).setOnClickListener(this);

    }


    public void run(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);

                fixedTime--;

                timeTextView.setText(time);
                // Set the text view text.
                timeLimitTextView.setText(fixedTime+"seconds remains");


                handler.postDelayed(this, 1000);


                if(fixedTime==0){
                    fixedTime=30;
                    next();
                    if(questionSetUpHandlesList.size()+1==i){
                        handler.removeCallbacks(this);
                    }
                   // Toast.makeText(getApplicationContext(), questionSetUpHandlesList.size()+" "+i, Toast.LENGTH_SHORT).show();
                }

                seconds++;
                //Toast.makeText(getApplicationContext(), i, Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.nextQuizid){
            next();
        }

        else if(view.getId()==R.id.option1Buttonid){
            if(currectOption.equals("a")||currectOption.equals("A")){
                //Toast.makeText(getApplicationContext(), "Congrass! You are Currect", Toast.LENGTH_SHORT).show();
                score++;
                next();
            }
            else {
                //Toast.makeText(getApplicationContext(), "Wrong answer!\nCurrect Option "+currectOption, Toast.LENGTH_SHORT).show();
                next();
            }
        }
        else if(view.getId()==R.id.option2Buttonid){
            if(currectOption.equals("b")||currectOption.equals("B")){
                //Toast.makeText(getApplicationContext(), "Congrass! You are Currect", Toast.LENGTH_SHORT).show();
                score++;
                next();
            }
            else {
                //Toast.makeText(getApplicationContext(), "Wrong answer!\nCurrect Option "+currectOption, Toast.LENGTH_SHORT).show();
                next();
            }
        }
        else if(view.getId()==R.id.option3Buttonid){
            if(currectOption.equals("c")||currectOption.equals("C")){
                //Toast.makeText(getApplicationContext(), "Congrass! You are Currect", Toast.LENGTH_SHORT).show();
                score++;
                next();
            }
            else {
                //Toast.makeText(getApplicationContext(), "Wrong answer! \nCurrect Option "+currectOption, Toast.LENGTH_SHORT).show();
                next();
            }
        }
        else if(view.getId()==R.id.option4Buttonid){
            if(currectOption.equals("d")||currectOption.equals("D")){
                //Toast.makeText(getApplicationContext(), "Congrass! You are Currect", Toast.LENGTH_SHORT).show();
                score++;
                next();
            }
            else {
                //Toast.makeText(getApplicationContext(), "Wrong answer!\nCurrect Option "+currectOption, Toast.LENGTH_SHORT).show();
                next();
            }
        }

        else if(view.getId()==R.id.seeScoreBoardButtonId){
            Intent intent = new Intent(getApplicationContext(), OlympaidScoreBoard.class);
            startActivity(intent);
            finish();
        }

        else if(view.getId()==R.id.startOlympaidButtonId){
            findViewById(R.id.rulesOlympaidLinearLayoutId).setVisibility(View.GONE);


            findViewById(R.id.olympaidStartLinierLayoutId).setVisibility(View.VISIBLE);
            run();
            questionSize = questionSetUpHandlesList.size();
            nextQuiz.setVisibility(View.VISIBLE);
            next();

        }
    }

    public void next(){
        fixedTime = 30;
        if(i==questionSize){
            updateOlympaidPoint();


            /*
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            intent.putExtra("email", getIntent().getStringExtra("email"));
            startActivity(intent);

             */
        }

        if(i<questionSize){
            String questionIs = i+1+") "+questionSetUpHandlesList.get(i).getQuestion();
            if(question.length()<32){
                questionIs+="\n";
            }
            question.setText(questionIs);
            option1.setText(questionSetUpHandlesList.get(i).getOption1());
            option2.setText(questionSetUpHandlesList.get(i).getOption2());
            option3.setText(questionSetUpHandlesList.get(i).getOption3());
            option4.setText(questionSetUpHandlesList.get(i).getOption4());
            currectOption = questionSetUpHandlesList.get(i).getCurretOption();
            i++;
        }
    }


    public void updateOlympaidPoint(){
        String key = databaseReferenceForOlympaidPointTable.push().getKey();
        OlympaidPointTableHandle olympaidPointTableHandle = new OlympaidPointTableHandle(myEmail.trim(), score);
        databaseReferenceForOlympaidPointTable.child(key).setValue(olympaidPointTableHandle);
        double x = handle.getOlympaidPoint()+score;
        handle.setOlympaidPoint(x);
        handle.setCurrentPoint(score);
        databaseReferenceForEmail.setValue(handle);
        findViewById(R.id.seeScoreBoardButtonId).setVisibility(View.VISIBLE);
        findViewById(R.id.olympaidStartLinierLayoutId).setVisibility(View.GONE);
        i+=1;
/*
        Intent intent = new Intent(getApplicationContext(),OlympaidScoreBoard.class);
        startActivity(intent);
        finish();
 */
    }

    @Override
    public void onBackPressed() {
        alertDialogCall();
    }

    public void alertDialogCall(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to finish the Olympaid?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Exit!");
        alertDialog.setIcon(R.drawable.math_olympaid_logo);
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            alertDialogCall();
        }

        return super.onOptionsItemSelected(item);
    }




}



