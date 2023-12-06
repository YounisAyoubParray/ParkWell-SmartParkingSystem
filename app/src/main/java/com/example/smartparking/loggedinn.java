package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.AlertDialog;
import android.content.DialogInterface;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;


import com.example.smartparking.databinding.ActivityLoggedinnBinding;


public class loggedinn extends AppCompatActivity {
    private AlertDialog.Builder builder;
    ActivityLoggedinnBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(loggedinn.this);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#EDEDED"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setTitle(Html.fromHtml("<h3><font color='#014077'> Home  </font></h3>"));
        binding =ActivityLoggedinnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new home());
        binding.bottomNavigationView.setOnItemSelectedListener(item->{

            switch (item.getItemId()){

                case R.id.Home:
                    getSupportActionBar().setTitle(Html.fromHtml("<h3><font color='#014077'> Home  </font></h3>"));
                    replaceFragment(new home());
                    break;
                case R.id.Profile:
                    getSupportActionBar().setTitle(Html.fromHtml("<h3><font color='#014077'> Profile  </font></h3>"));
                    replaceFragment(new profile());
                    break;
                case R.id.Settings:
                    getSupportActionBar().setTitle(Html.fromHtml("<h3><font color='#014077'> Settings  </font></h3>"));
                    replaceFragment(new settings());
                    break;
            }
            return true;
        });



    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        builder.setTitle("Park Well");
        builder.setIcon(R.drawable.parkinglogo);
        builder.setMessage("You want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton(Html.fromHtml("<font color='#014077'> No </font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(Html.fromHtml("<font color='#ba1e13'> Yes </font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
                System.exit(0);
            }
        });
        builder.show();
    }



}