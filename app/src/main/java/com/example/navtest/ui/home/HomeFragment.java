package com.example.navtest.ui.home;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.navtest.FirstScreen;
import com.example.navtest.Handle;
import com.example.navtest.MainActivity;
import com.example.navtest.Olympaid2;
import com.example.navtest.OlympaidQuestionSheduleHandle;
import com.example.navtest.OlympaidScoreBoard;
import com.example.navtest.R;
import com.example.navtest.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference, databaseReferenceOlympaidSchedule;
    TextView nameTextView, emailTextView, phoneNoTextView, institutionTextView, classTextView, olympaidPointTextView, practicePointTextView, levelTextView;
    ImageView imageView;
    String imageDownloadUri;
    ProgressBar profileProgressBar;
    Handle handle;
    AlertDialog.Builder alertDialogBuilder;
    LinearLayout verifyLinearLayout;
    Button verifyButton;
    FirebaseAuth firebaseAuth;
    private OlympaidQuestionSheduleHandle olympaidQuestionSheduleHandle;
    private TextView olympaidRunningTextView;
    private String myEmail, myClass, myName;
    private int  myPracticePoint, myOlympaidPoint;
    private int myLevel;
    private SharedPreferences sharedPreferences;
    private View root;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);


        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        myEmail = sharedPreferences.getString("email", "");



        olympaidRunningTextView = root.findViewById(R.id.olympaidRunningTextViewId);
        verifyButton = root.findViewById(R.id.verifyButtonid);
        verifyLinearLayout = root.findViewById(R.id.verifyLinearLayoutid);
        levelTextView = root.findViewById(R.id.levelTextViewid);
        practicePointTextView = root.findViewById(R.id.practicePointTextViewId);
        olympaidPointTextView = root.findViewById(R.id.olympaidPointTextViewId);
        profileProgressBar = root.findViewById(R.id.profileProgressBarid);
        imageView = root.findViewById(R.id.imageViewId);
        nameTextView = root.findViewById(R.id.nameTextViewId);
        emailTextView = root.findViewById(R.id.emailTextViewId);
        phoneNoTextView = root.findViewById(R.id.phoneNoTextViewId);
        institutionTextView = root.findViewById(R.id.institutionTextViewId);
        classTextView = root.findViewById(R.id.classTextViewId);


        isOlympaidRunning();



        firebaseAuth = FirebaseAuth.getInstance();


        alertDialogBuilder = new AlertDialog.Builder(getActivity());

        emailTextView.setText(myEmail);

        databaseReference = firebaseDatabase.getReference(myEmail.replace(".","rm"));



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                handle = dataSnapshot.getValue(Handle.class);

                //Toast.makeText(getApplicationContext(), handle.getName(), Toast.LENGTH_SHORT).toString();

                myName = handle.getName();
                myClass = handle.getClas();
                myPracticePoint = (int) handle.getPracticePoint();
                myOlympaidPoint = (int)(handle.getOlympaidPoint());
                myLevel = (int)(handle.getLevel());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", myName);
                editor.putString("class", myClass);
                editor.putInt("practicePoint", myPracticePoint);
                editor.putInt("olympaidPoint",  myOlympaidPoint);
                editor.putInt("level", myLevel);
                editor.putBoolean("flag", true);
                editor.commit();

                //Toast.makeText(getContext(), "My Practice Point is = " + myLevel, Toast.LENGTH_SHORT).show();

                nameTextView.setText(handle.getName().toString().trim());
                institutionTextView.setText( handle.getInstitution());
                phoneNoTextView.setText( handle.getPhone());
                classTextView.setText(handle.getClas());
                practicePointTextView.setText(handle.getPracticePoint()+"");
                olympaidPointTextView.setText(handle.getOlympaidPoint()+"");
                levelTextView.setText(handle.getLevel()+"");


                imageDownloadUri = handle.getImageDounloadUri();
                setIamge();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Sorry! Error connection", Toast.LENGTH_SHORT).show();
            }
        });


        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user.isEmailVerified()){
            verifyLinearLayout.setVisibility(View.GONE);
        }



        olympaidRunningTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTime();
                isOlympaidRunning();
            }
        });



        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressBar progressBar = root.findViewById(R.id.verifyProgressBarid);
                progressBar.setVisibility(View.VISIBLE);
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Verification link has been sent", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Please check your internet connection or use a valid email", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return root;
    }


    public void setIamge(){
        Picasso.with(getContext())
                .load(imageDownloadUri)
                .placeholder(R.drawable.loading_image)
                .fit()
                .centerCrop()
                .into(imageView);
        profileProgressBar.setVisibility(View.GONE);
    }


    public void checkTime(){
        DatabaseReference databaseReferenceOlympaidSchedule = FirebaseDatabase.getInstance().getReference("questionSchedule");
        databaseReferenceOlympaidSchedule.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                olympaidQuestionSheduleHandle = dataSnapshot.getValue(OlympaidQuestionSheduleHandle.class);

                Date date = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("dd");
                int day = Integer.parseInt(dateFormat1.format(date));
                DateFormat dateFormat2 = new SimpleDateFormat("MM");
                int month = Integer.parseInt(dateFormat2.format(date));
                DateFormat dateFormat3 = new SimpleDateFormat("yy");
                int year = Integer.parseInt(dateFormat3.format(date));


                DateFormat dateFormat4 =  new SimpleDateFormat("HH");
                int hour = Integer.parseInt(dateFormat4.format(date));
                DateFormat dateFormat5 = new SimpleDateFormat("mm");
                int minute = Integer.parseInt(dateFormat5.format(date));
                DateFormat dateFormat6 = new SimpleDateFormat("ss");
                int second = Integer.parseInt(dateFormat6.format(date));


                String olympaidHourString;

                int olympaidHour = olympaidQuestionSheduleHandle.getHour();
                if(olympaidHour>12){
                    olympaidHourString = String.valueOf(olympaidHour-12)+":00pm";
                }
                else {
                    olympaidHourString = String.valueOf(olympaidHour)+":00am";
                }

                if(day==olympaidQuestionSheduleHandle.getDate() && month==olympaidQuestionSheduleHandle.getMonth() && year==olympaidQuestionSheduleHandle.getYear() && hour==olympaidQuestionSheduleHandle.getHour()){
                    if(minute>30){
                        String olympaidScedule = "Olympaid is closed!";
                        Intent intent = new Intent(getContext(), OlympaidScoreBoard.class);
                        startActivity(intent);
                        Toast.makeText(getContext(), olympaidScedule, Toast.LENGTH_SHORT).show();
                    }
                    else if(minute>=5){
                        Toast.makeText(getContext(), "Olympaid is running, But you are too late!", Toast.LENGTH_LONG).show();
                    }

                    else {
                        Toast.makeText(getContext(), "Olympaid is running, Just wait a moment", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), Olympaid2.class);
                        intent.putExtra("email", myEmail);
                        startActivity(intent);
                    }

                }

                else if(day==olympaidQuestionSheduleHandle.getDate() && month==olympaidQuestionSheduleHandle.getMonth() && year==olympaidQuestionSheduleHandle.getYear()){

                    String olympaidScedule;
                    if(hour>olympaidQuestionSheduleHandle.getHour()){
                        olympaidScedule = "Olympaid was held today at "+olympaidHourString;
                        Intent intent = new Intent(getContext(),OlympaidScoreBoard.class);
                        startActivity(intent);
                    }
                    else {
                        olympaidScedule = "Olympaid will held today at "+olympaidHourString;
                    }
                    Toast.makeText(getContext(), olympaidScedule, Toast.LENGTH_LONG).show();
                    //finish();
                }

                else if(day!=olympaidQuestionSheduleHandle.getDate() || month!=olympaidQuestionSheduleHandle.getMonth() || year!=olympaidQuestionSheduleHandle.getYear()){

                    String olympaidScedule = "Olympaid will held at "+olympaidQuestionSheduleHandle.getDate()+":"+olympaidQuestionSheduleHandle.getMonth()+":"+olympaidQuestionSheduleHandle.getYear()+"  at "+olympaidHourString;
                    Toast.makeText(getContext(), olympaidScedule, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void isOlympaidRunning() {
        databaseReferenceOlympaidSchedule = firebaseDatabase.getReference("questionSchedule");
        databaseReferenceOlympaidSchedule.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                olympaidQuestionSheduleHandle = dataSnapshot.getValue(OlympaidQuestionSheduleHandle.class);

                 Date date = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("dd");
                final int day = Integer.parseInt(dateFormat1.format(date));
                DateFormat dateFormat2 = new SimpleDateFormat("MM");
                final int month = Integer.parseInt(dateFormat2.format(date));
                DateFormat dateFormat3 = new SimpleDateFormat("yy");
                final int year = Integer.parseInt(dateFormat3.format(date));


                final DateFormat dateFormat4 = new SimpleDateFormat("HH");
                final int hour = Integer.parseInt(dateFormat4.format(date));
                DateFormat dateFormat5 = new SimpleDateFormat("mm");
                final int minute = Integer.parseInt(dateFormat5.format(date));

                DateFormat dateFormat6 = new SimpleDateFormat("ss");
                final int second = Integer.parseInt(dateFormat6.format(date));

                   if (day == olympaidQuestionSheduleHandle.getDate() && month == olympaidQuestionSheduleHandle.getMonth() && year == olympaidQuestionSheduleHandle.getYear() && hour <= olympaidQuestionSheduleHandle.getHour()) {
                    final int olympaidHour = olympaidQuestionSheduleHandle.getHour();
                    olympaidRunningTextView.setVisibility(View.VISIBLE);

                    final Handler handler = new Handler();
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            Date date = new Date();
                            DateFormat dateFormat4 = new SimpleDateFormat("HH");
                            int hour = Integer.parseInt(dateFormat4.format(date));
                            DateFormat dateFormat5 = new SimpleDateFormat("mm");
                            int minute = Integer.parseInt(dateFormat5.format(date));

                            DateFormat dateFormat6 = new SimpleDateFormat("ss");
                            int second = Integer.parseInt(dateFormat6.format(date));

                            hour = olympaidHour-(hour+1);
                            minute = 60 - minute;
                            second = 60 - second;

                            String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
                            olympaidRunningTextView.setText("Comming Olympaid "+ time);
                            handler.postDelayed(this, 1000);

                            if (hour ==-1) {
                                handler.removeCallbacks(this);
                                olympaidRunningTextView.setText("Olympaid is Running");
                            }
                        }


                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}