package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartparking.databinding.ActivityLoggedinnBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Adminui extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView textView;
    FirebaseUser user;
    String uid;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminui);
        drawerLayout=findViewById(R.id.sidedrawerlayout);
        navigationView=findViewById(R.id.navview);
        View headerView = navigationView.getHeaderView(0);
        textView= headerView.findViewById(R.id.textView52);
        toolbar=findViewById(R.id.toolbar);
        builder = new AlertDialog.Builder(Adminui.this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String a=snapshot.getValue(String.class);
                if(a!=null){
                    textView.setText(a);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setTitle("Dashboard");
        replaceFragment(new Dashboard());
        navigationView.getMenu().getItem(0).setChecked(true);
        setnav();
    }

    private void setnav() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int a= item.getItemId();
                switch (item.getItemId()) {
                    case R.id.Dashboard:
                        navigationView.setCheckedItem(R.id.Dashboard);
                        getSupportActionBar().setTitle("Dashboard");
                        replaceFragment(new Dashboard());
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.Ban:
                        navigationView.setCheckedItem(R.id.Ban);
                        getSupportActionBar().setTitle("Off-Limits");
                        replaceFragment(new Offlimits());
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.Adsettings:
                        navigationView.setCheckedItem(R.id.Settings);
                        getSupportActionBar().setTitle("Settings");
                        replaceFragment(new Adminsettings());
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout1,fragment);
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