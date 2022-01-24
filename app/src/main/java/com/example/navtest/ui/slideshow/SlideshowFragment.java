package com.example.navtest.ui.slideshow;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.navtest.FirstScreen;
import com.example.navtest.Olympaid2;
import com.example.navtest.OlympaidQuestionSheduleHandle;
import com.example.navtest.OlympaidScoreBoard;
import com.example.navtest.Practice;
import com.example.navtest.QuestionSetUp;
import com.example.navtest.R;
import com.example.navtest.ui.slideshow.SlideshowViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    private SharedPreferences sharedPreferences;



    private Button practice1Button, practice2Button, practice3Button, practice4Button, practice5Button,
            practice6Button, practice7Button, practice8Button, practice9Button, practice10Button, practice11Button, practice12Button;
    private ImageView lock1ImageView,
            lock2ImageView,
            lock3ImageView,
            lock4ImageView,
            lock5ImageView,
            lock6ImageView,
            lock7ImageView,
            lock8ImageView,
            lock9ImageView,
            lock10ImageView,
            lock11ImageView,
            lock12ImageView;

    private ImageView[] imageViews = new ImageView[12];
    private LinearLayout quizTypeSelectorLayout, practiceLayout;
    private Button  practiceQuizeButton, getQuestionSetUpButton;
    private OlympaidQuestionSheduleHandle olympaidQuestionSheduleHandle;
    private String myEmail;

    private int level, levelEligible;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_slideshow, container, false);



        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("flag", false);
        editor.commit();


        practiceLayout = root.findViewById(R.id.practiceLayoutid);
        quizTypeSelectorLayout = root.findViewById(R.id.quizTypeSelectorLayout);

        practiceQuizeButton = root.findViewById(R.id.practiceQuizeButtonid);
        practice1Button = root.findViewById(R.id.practice1Buttonid);
        practice2Button = root.findViewById(R.id.practice2Buttonid);
        practice3Button = root.findViewById(R.id.practice3Buttonid);
        practice4Button = root.findViewById(R.id.practice4Buttonid);
        practice5Button = root.findViewById(R.id.practice5Buttonid);
        practice6Button = root.findViewById(R.id.practice6Buttonid);
        practice7Button = root.findViewById(R.id.practice7Buttonid);
        practice8Button = root.findViewById(R.id.practice8Buttonid);
        practice9Button = root.findViewById(R.id.practice9Buttonid);
        practice10Button = root.findViewById(R.id.practice10Buttonid);
        practice11Button = root.findViewById(R.id.practice11Buttonid);
        practice12Button = root.findViewById(R.id.practice12Buttonid);

        imageViews[0] = root.findViewById(R.id.lock1Imageviewid);
        imageViews[1] = root.findViewById(R.id.lock2Imageviewid);
        imageViews[2] = root.findViewById(R.id.lock3Imageviewid);
        imageViews[3] = root.findViewById(R.id.lock4Imageviewid);
        imageViews[4] = root.findViewById(R.id.lock5Imageviewid);
        imageViews[5] = root.findViewById(R.id.lock6Imageviewid);
        imageViews[6] = root.findViewById(R.id.lock7Imageviewid);
        imageViews[7] = root.findViewById(R.id.lock8Imageviewid);
        imageViews[8] = root.findViewById(R.id.lock9Imageviewid);
        imageViews[9] = root.findViewById(R.id.lock10Imageviewid);
        imageViews[10] = root.findViewById(R.id.lock11Imageviewid);
        imageViews[11] = root.findViewById(R.id.lock12Imageviewid);



        level = sharedPreferences.getInt("level", 5);
        levelEligible = level+1;
        int duration=level;
        if(level==12){
            duration--;
        }
        for(int i=0; i<=duration; i++){
            imageViews[i].setImageResource(R.drawable.unlock_foreground);
        }




        practiceQuizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                practiceLayout.setVisibility(View.VISIBLE);

            }
        });



        root.findViewById(R.id.participaieInAOlympaid2Buttonid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkTime();

            }
        });





        practice1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getActivity().finish();
                Intent intent = new Intent(getContext(), Practice.class);
                //intent.putExtra("email", "munna.cse.ju@gmail.com");

                intent.putExtra("practice", 1);
                startActivity(intent);
            }
        });



        practice2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelEligible>=2){

                    Intent intent = new Intent(getContext(), Practice.class);

                    intent.putExtra("practice", 2);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Sorry! You are not elegible For level "+2, Toast.LENGTH_SHORT).show();
                }
            }
        });

        practice3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelEligible>=3){

                    Intent intent = new Intent(getContext(), Practice.class);

                    intent.putExtra("practice", 3);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Sorry! You are not elegible For level "+3+" /n You are in Level "+level, Toast.LENGTH_SHORT).show();
                }
            }
        });

        practice4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelEligible>=4){

                    Intent intent = new Intent(getContext(), Practice.class);

                    intent.putExtra("practice", 4);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Sorry! You are not elegible For level "+4+" /n You are in Level "+level, Toast.LENGTH_SHORT).show();
                }
            }
        });

        practice5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelEligible>=5){

                    Intent intent = new Intent(getContext(), Practice.class);

                    intent.putExtra("practice", 5);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Sorry! You are not elegible For level "+5+" /n You are in Level "+level, Toast.LENGTH_SHORT).show();
                }
            }
        });
        practice6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelEligible>=6){

                    Intent intent = new Intent(getContext(), Practice.class);

                    intent.putExtra("practice", 6);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Sorry! You are not elegible For level "+6+" /n You are in Level "+level, Toast.LENGTH_SHORT).show();
                }
            }
        });
        practice7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelEligible>=7){

                    Intent intent = new Intent(getContext(), Practice.class);

                    intent.putExtra("practice", 7);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Sorry! You are not elegible For level "+7+" /n You are in Level "+level, Toast.LENGTH_SHORT).show();
                }
            }
        });

        practice8Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelEligible>=8){

                    Intent intent = new Intent(getContext(), Practice.class);

                    intent.putExtra("practice", 8);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Sorry! You are not elegible For level "+8+" /n You are in Level "+level, Toast.LENGTH_SHORT).show();
                }
            }
        });
        practice9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelEligible>=9){

                    Intent intent = new Intent(getContext(), Practice.class);

                    intent.putExtra("practice", 9);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Sorry! You are not elegible For level "+9+" /n You are in Level "+level, Toast.LENGTH_SHORT).show();
                }
            }
        });

        practice10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelEligible>=10){

                    Intent intent = new Intent(getContext(), Practice.class);

                    intent.putExtra("practice", 10);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Sorry! You are not elegible For level "+10+" /n You are in Level "+level, Toast.LENGTH_SHORT).show();
                }
            }
        });

        practice11Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelEligible>=11){

                    Intent intent = new Intent(getContext(), Practice.class);

                    intent.putExtra("practice", 11);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Sorry! You are not elegible For level "+11+" /n You are in Level "+level, Toast.LENGTH_SHORT).show();
                }
            }
        });

        practice12Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelEligible>=12){

                    Intent intent = new Intent(getContext(), Practice.class);

                    intent.putExtra("practice", 12);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Sorry! You are not elegible For level "+12+" /n You are in Level "+level, Toast.LENGTH_SHORT).show();
                }
            }
        });







        return root;
    }



    public void checkTime() {
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


                DateFormat dateFormat4 = new SimpleDateFormat("HH");
                int hour = Integer.parseInt(dateFormat4.format(date));
                DateFormat dateFormat5 = new SimpleDateFormat("mm");
                int minute = Integer.parseInt(dateFormat5.format(date));
                DateFormat dateFormat6 = new SimpleDateFormat("ss");
                int second = Integer.parseInt(dateFormat6.format(date));


                String olympaidHourString;

                int olympaidHour = olympaidQuestionSheduleHandle.getHour();
                if (olympaidHour > 12) {
                    olympaidHourString = String.valueOf(olympaidHour - 12) + ":00pm";
                } else {
                    olympaidHourString = String.valueOf(olympaidHour) + ":00am";
                }

                if (day == olympaidQuestionSheduleHandle.getDate() && month == olympaidQuestionSheduleHandle.getMonth() && year == olympaidQuestionSheduleHandle.getYear() && hour == olympaidQuestionSheduleHandle.getHour()) {
                    if (minute > 30) {
                        String olympaidScedule = "Olympaid is Finished!";
                        Intent intent = new Intent(getContext(), OlympaidScoreBoard.class);
                        startActivity(intent);

                        Toast.makeText(getContext(), olympaidScedule, Toast.LENGTH_SHORT).show();
                    }
                    else if (minute >= 5) {
                        Toast.makeText(getContext(), "Olympaid is running, But you are too late!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Olympaid is Running", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), Olympaid2.class);
                        startActivity(intent);
                    }

                } else if (day == olympaidQuestionSheduleHandle.getDate() && month == olympaidQuestionSheduleHandle.getMonth() && year == olympaidQuestionSheduleHandle.getYear()) {

                    String olympaidScedule;
                    if (hour > olympaidQuestionSheduleHandle.getHour()) {
                        olympaidScedule = "Olympaid was held today at " + olympaidHourString;
                        Intent intent = new Intent(getContext(), OlympaidScoreBoard.class);
                        startActivity(intent);

                    } else {
                        olympaidScedule = "Olympaid will held today at " + olympaidHourString;
                    }
                    Toast.makeText(getContext(), olympaidScedule, Toast.LENGTH_LONG).show();

                } else if((month>olympaidQuestionSheduleHandle.getMonth())||(month==olympaidQuestionSheduleHandle.getMonth()&&day>olympaidQuestionSheduleHandle.getDate())){

                    String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", olympaidQuestionSheduleHandle.getDate() , olympaidQuestionSheduleHandle.getMonth() ,olympaidQuestionSheduleHandle.getYear() );

                    String olympaidScedule = "Olympaid was held at " + time + "  at " + olympaidHourString;
                    Toast.makeText(getContext(), olympaidScedule, Toast.LENGTH_LONG).show();
                    //getActivity().finish();
                }

                else if (day != olympaidQuestionSheduleHandle.getDate() || month != olympaidQuestionSheduleHandle.getMonth() || year != olympaidQuestionSheduleHandle.getYear()) {

                    String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", olympaidQuestionSheduleHandle.getDate() , olympaidQuestionSheduleHandle.getMonth() , olympaidQuestionSheduleHandle.getYear() );

                    String olympaidScedule = "Olympaid will held at " + time+ "  at " + olympaidHourString;
                    Toast.makeText(getContext(), olympaidScedule, Toast.LENGTH_LONG).show();
                    //getActivity().finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("flag", false);
        editor.commit();
    }

}