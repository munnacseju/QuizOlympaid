package com.example.navtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.navtest.OlympaidPointTableHandle;
import com.example.navtest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OlympaidScoreBoard extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private ListView listView;
    private List<OlympaidPointTableHandle>olympaidPointTableHandleList;
    private OlympaidScoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olympaid_score_board);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setTitle("Score Board");

        databaseReference = firebaseDatabase.getReference("olympaidPointTable");
        listView = findViewById(R.id.olympaidScoreBoardListViewid);
        olympaidPointTableHandleList = new ArrayList<>();

        adapter = new OlympaidScoreAdapter(this, olympaidPointTableHandleList);

    }

    @Override
    protected void onStart() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                olympaidPointTableHandleList.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    OlympaidPointTableHandle olympaidPointTableHandle = dataSnapshot1.getValue(OlympaidPointTableHandle.class);
                    olympaidPointTableHandleList.add(olympaidPointTableHandle);
                }

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
