package com.example.navtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navtest.Handle;
import com.example.navtest.QuestionSetUpHandle;
import com.example.navtest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Practice extends AppCompatActivity implements View.OnClickListener {
    private TextView question, scoreTextView, loadingTextView;
    private Button option1, option2, option3, option4, startQuiz,  nextQuiz;
    private double score=0;
    private String currectOption;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    Handle handle;

    DatabaseReference databaseReference1;
    private List<QuestionSetUpHandle> questionSetUpHandlesList;
    int questionSize, i = 0;
    ScrollView scrollView;
    LinearLayout linearLayout;
    private int level, practice;
    private String myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Practice");


        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        myEmail = sharedPreferences.getString("email", "");


        level = sharedPreferences.getInt("level", 1);
        practice = getIntent().getIntExtra("practice", 3);
        Toast.makeText(getApplicationContext(), "Level = "+level+"Practice Point = "+practice, Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplicationContext(), "You practice : "+ practice, Toast.LENGTH_SHORT).show();

        if(practice == 0){
            databaseReference = firebaseDatabase.getReference("olympaid");
        }
        else {
            String referance = "practice"+practice;
            databaseReference = firebaseDatabase.getReference(referance);
        }


        questionSetUpHandlesList = new ArrayList<>();


        linearLayout = findViewById(R.id.linearLayoutid);
        scrollView = findViewById(R.id.scrolViewId);
        loadingTextView = findViewById(R.id.loadingTextViewId);

        question = findViewById(R.id.questionTextViewid);
        scoreTextView = findViewById(R.id.scoreTextViewid);


        option1 = findViewById(R.id.option1Buttonid);
        option2 = findViewById(R.id.option2Buttonid);
        option3 = findViewById(R.id.option3Buttonid);
        option4 = findViewById(R.id.option4Buttonid);



        nextQuiz = findViewById(R.id.nextQuizid);
        startQuiz = findViewById(R.id.startQuizid);

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


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    QuestionSetUpHandle questionSetUpHandle = dataSnapshot1.getValue(QuestionSetUpHandle.class);
                    questionSetUpHandlesList.add(questionSetUpHandle);
                }
                loadingTextView.setText("এখন আপনি প্রাকটিস শুরু করতে পারেন");
                //Toast.makeText(getApplicationContext(), "Your question is loaded compleatly", Toast.LENGTH_SHORT).show();

                startQuiz.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
        startQuiz.setOnClickListener(this);
        nextQuiz.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.startQuizid){
            linearLayout.setVisibility(View.GONE);
            startQuiz.setVisibility(View.GONE);
            questionSize = questionSetUpHandlesList.size();
            scrollView.setVisibility(View.VISIBLE);
            nextQuiz.setVisibility(View.VISIBLE);
            next();
        }

        else if(view.getId()==R.id.nextQuizid){
            next();
        }

        else if(view.getId()==R.id.option1Buttonid){
            if(currectOption.equals("a")||currectOption.equals("A")){
                Toast.makeText(getApplicationContext(), "Congrass! You are Currect", Toast.LENGTH_SHORT).show();
                score++;
                scoreTextView.setText("Score: "+score);
                next();
            }
            else {
                Toast.makeText(getApplicationContext(), "Wrong answer!\nCurrect Option "+currectOption, Toast.LENGTH_SHORT).show();
                next();
            }
        }
        else if(view.getId()==R.id.option2Buttonid){
            if(currectOption.equals("b")||currectOption.equals("B")){
                Toast.makeText(getApplicationContext(), "Congrass! You are Currect", Toast.LENGTH_SHORT).show();
                score++;
                scoreTextView.setText("Score: "+score);
                next();
            }
            else {
                Toast.makeText(getApplicationContext(), "Wrong answer!\nCurrect Option "+currectOption, Toast.LENGTH_SHORT).show();
                next();
            }
        }
        else if(view.getId()==R.id.option3Buttonid){
            if(currectOption.equals("c")||currectOption.equals("C")){
                Toast.makeText(getApplicationContext(), "Congrass! You are Currect", Toast.LENGTH_SHORT).show();
                score++;
                scoreTextView.setText("Score: "+score);
                next();
            }
            else {
                Toast.makeText(getApplicationContext(), "Wrong answer! \nCurrect Option "+currectOption, Toast.LENGTH_SHORT).show();
                next();
            }
        }
        else if(view.getId()==R.id.option4Buttonid){
            if(currectOption.equals("d")||currectOption.equals("D")){
                Toast.makeText(getApplicationContext(), "Congrass! You are Currect", Toast.LENGTH_SHORT).show();
                score++;
                scoreTextView.setText("Score: "+score);
                next();
            }
            else {
                Toast.makeText(getApplicationContext(), "Wrong answer!\nCurrect Option "+currectOption, Toast.LENGTH_SHORT).show();
                next();
            }
        }
    }

    public void next(){
        if(i==questionSize){
            Toast.makeText(getApplicationContext(), "Compleate Quiz", Toast.LENGTH_SHORT).show();
            updatePracticePoint();
            finish();

            /*
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            intent.putExtra("email", getIntent().getStringExtra("email"));
            startActivity(intent);

             */
        }

        if(i<questionSize){
            question.setText(questionSetUpHandlesList.get(i).getQuestion()+"এটা এডিশনাল, পরিক্ষা মুলক ভাবে ব্যবহার করা হচ্ছে।");
            option1.setText(questionSetUpHandlesList.get(i).getOption1());
            option2.setText(questionSetUpHandlesList.get(i).getOption2());
            option3.setText(questionSetUpHandlesList.get(i).getOption3());
            option4.setText(questionSetUpHandlesList.get(i).getOption4());
            currectOption = questionSetUpHandlesList.get(i).getCurretOption();
            i++;
        }
    }

    public void updatePracticePoint(){
        double x = handle.getPracticePoint()+score;
        handle.setPracticePoint(x);

        int neededScore = (3*i)/4;


        if(practice==12&&level==12){
            Toast.makeText(getApplicationContext(), "Already You are in last level "+level, Toast.LENGTH_SHORT).show();
        }
        else if(score>=(neededScore)&&(level+1)==practice){
            level = practice;
            handle.setLevel(level);
            Toast.makeText(getApplicationContext(), "You level is promoted! "+level, Toast.LENGTH_SHORT).show();
        }
        else if(score<neededScore) {
            Toast.makeText(getApplicationContext(), "Sorry, You didn't get enough score: "+neededScore, Toast.LENGTH_SHORT).show();
        }
        databaseReference1.setValue(handle);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}



