package com.example.navtest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Welcome to our application", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.sendMenuItemid){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String subject = "My Point Table of Quiz";
            String text = "Hello\n I use Math Olympaid Apps\n";
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(intent, "share"));
            Toast.makeText(getApplicationContext(), "Send", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.shareMenuItemid){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String subject = "Math Olympaid Apps help you very much";
            String text = "package com.example.jusc";
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(intent, "share"));
            Toast.makeText(getApplicationContext(), "Share Your Friend About Quiz", Toast.LENGTH_SHORT).show();
        }

        else if(item.getItemId()==R.id.feedBackMenuItemid){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String subject = "My Feed Back";
            String text = "Math Olympaid Apps help me very much";
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"munna.cse.ju@gmail.com"});
            startActivity(Intent.createChooser(intent, "share"));
            Toast.makeText(getApplicationContext(), "Send Feed Back into email", Toast.LENGTH_SHORT).show();        }
        else if(item.getItemId()==R.id.rulesMenuItemid){
            rules();
        }
        else if(item.getItemId()==R.id.aboutMenuItemid){
            aboutUs();
        }
        else if(item.getItemId()==R.id.logOutMenuItemid){
            SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", "");
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        else if(item.getItemId()==R.id.questionSetUpMenuItemId){
            Intent intent = new Intent(getApplicationContext(), QuestionSetUp.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void aboutUs() {
        Intent intent = new Intent(getApplicationContext(), AboutUs.class);
        startActivity(intent);

    }

    private void rules(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.rules)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Math Olympaid Rules");
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {

        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        boolean flag = sharedPreferences.getBoolean("flag", false);
        if(flag==false){
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else {
            askToBack();
        }
    }

    public void askToBack(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are You sure you want to exit?")
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
        alertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("flag", false);
        editor.commit();
    }
}
