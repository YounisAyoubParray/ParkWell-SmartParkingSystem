package com.example.smartparking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class home extends Fragment {

    private TextView myprofile;
    private TextView dir;
    private TextView button;
    private TextView book,unbook,reportts;
    private FirebaseUser user;
    private TextView date44;
    private String userID;
    private DatabaseReference reference;
    View view;
    Animation homebtn,homebtn2;
    private int time = 200;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_home,container, false);
        myprofile=view.findViewById(R.id.myprofile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();

        compact();
        date44=view.findViewById(R.id.date44);
        datenow();

        unbook=view.findViewById(R.id.unbook);
        unbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homebtn= AnimationUtils.loadAnimation(getActivity(),R.anim.homebtn);
                homebtn2= AnimationUtils.loadAnimation(getActivity(),R.anim.homebtn2);
                unbook.startAnimation(homebtn);
                unbook.startAnimation(homebtn2);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unbook.clearAnimation();
                        vubook();
                    }
                },time);
            }
        });


        reportts=view.findViewById(R.id.reportts);
        reportts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homebtn= AnimationUtils.loadAnimation(getActivity(),R.anim.homebtn);
                homebtn2= AnimationUtils.loadAnimation(getActivity(),R.anim.homebtn2);
                reportts.startAnimation(homebtn);
                reportts.startAnimation(homebtn2);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reportts.clearAnimation();
                        report();
                    }
                },time);
            }
        });


        button=view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homebtn= AnimationUtils.loadAnimation(getActivity(),R.anim.homebtn);
                homebtn2= AnimationUtils.loadAnimation(getActivity(),R.anim.homebtn2);
                button.startAnimation(homebtn);
                button.startAnimation(homebtn2);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.clearAnimation();
                        vbook();
                    }
                },time);


            }
        });

        book= view.findViewById(R.id.book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homebtn= AnimationUtils.loadAnimation(getActivity(),R.anim.homebtn);
                homebtn2= AnimationUtils.loadAnimation(getActivity(),R.anim.homebtn2);
                book.startAnimation(homebtn);
                book.startAnimation(homebtn2);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        book.clearAnimation();
                        dobook();
                    }
                },time);

            }
        });

        dir= view.findViewById(R.id.dir);
        dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homebtn= AnimationUtils.loadAnimation(getActivity(),R.anim.homebtn);
                homebtn2= AnimationUtils.loadAnimation(getActivity(),R.anim.homebtn2);
                dir.startAnimation(homebtn);
                dir.startAnimation(homebtn2);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dir.clearAnimation();
                        direction();
                    }
                },time);

            }
        });
    return view;
    }

    private void report() {
        startActivity(new Intent(getActivity(),parkinglots2.class));
    }

    private void vubook() {
        startActivity(new Intent(getActivity(),Unbantime.class));
    }


    private void datenow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String currentDateandTime = sdf.format(new Date());
        date44.setText(currentDateandTime);
    }

    private void direction() {
        startActivity(new Intent(getActivity(),MapsActivity.class));
    }

    private void dobook() {
        Intent intent= new Intent(getActivity(),parkinglots.class);
        startActivity(intent);
    }



    private void vbook() {
        Intent intent = new Intent(getActivity(), parkinglots1.class);
        startActivity(intent);
    }

    private void compact() {
        reference.child(userID).child("FullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String FullName=snapshot.getValue(String.class);
                if(FullName!=null) {
                    myprofile.setText(FullName);
                }else{
                    myprofile.setText("My Profile");
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            compact();
                        }
                    }, 100);
                }
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                myprofile.setText("My Profile");
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        compact();
                    }
                }, 100);
            }
        });
    }


    }
