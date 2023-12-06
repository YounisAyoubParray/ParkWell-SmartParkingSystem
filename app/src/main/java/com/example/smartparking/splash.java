package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class splash extends AppCompatActivity {

    private static final int splashtime = 2000;
    private ImageView imageView;
    Animation splashh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView=findViewById(R.id.imageView);
        splashh= AnimationUtils.loadAnimation(splash.this,R.anim.spanim);
        imageView.startAnimation(splashh);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser firebaseuserr = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseuserr != null && firebaseuserr.isEmailVerified()) {
                            firebaseuserr.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(splash.this, loggedinn.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        imageView.clearAnimation();
                                        startActivity(intent);
                                        finish();
                                    }else if(!task.isSuccessful()){
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(splash.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }else if(firebaseuserr != null){
                    String userID = firebaseuserr.getUid();
                    FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Type").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int type=snapshot.getValue(Integer.class);
                            if(type==0) {
                                Intent intent = new Intent(splash.this, Adminui.class);
                                startActivity(intent);
                            }else{
                                Intent splash = new Intent(splash.this, MainActivity.class);
                                startActivity(splash);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Intent splash = new Intent(splash.this, MainActivity.class);
                            startActivity(splash);
                            finish();
                        }
                    });
                }

                 else {
                    Intent splash = new Intent(splash.this, MainActivity.class);
                    startActivity(splash);
                    finish();
                }
            }
        }, splashtime);
    }
}