package com.example.smartparking;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private TextView tvregister;
    private Button logbut;
    private EditText teemail;
    private EditText tepassword;
    private TextView tvforgotpassword;
    private FirebaseAuth mAuth;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvregister= findViewById(R.id.tvregister);
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });


        teemail= findViewById(R.id.teemail);



        logbut= findViewById(R.id.logbut);

        tepassword= findViewById(R.id.tepassword);


        mAuth=FirebaseAuth.getInstance();

        tvforgotpassword= findViewById(R.id.tvforgotpassword);
        tvforgotpassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, forgotpassword.class);
                intent.putExtra("email",teemail.getText().toString());
                startActivity(intent);
            }
        });

        logbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loggin();
            }
        });


    }





    private void exit() {
        finishAffinity();
        System.exit(0);
    }
    void loggin() {

        String email=teemail.getText().toString().trim();
        if(email.isEmpty()){
            teemail.setError("Enter the email");
            teemail.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            teemail.setError("Enter the correct email");
            teemail.requestFocus();
            return;}

        String password=tepassword.getText().toString().trim();
        if(password.isEmpty())
        {
            tepassword.setError("Enter the password");
            tepassword.requestFocus();
            return;
        }
        if(password.length()<8)
        {
            tepassword.setError("Password length must be >= 8");
            tepassword.requestFocus();
            return;
        }

        final Loadingalert loadingalert = new Loadingalert(MainActivity.this);
        loadingalert.startAlertDialog();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userID = user.getUid();
                    if(user!=null){
                        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Type").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                               int type=snapshot.getValue(Integer.class);
                                   if(type==1){
                                       loadingalert.closeAlertDialog();
                                       teemail.setText(null);
                                       tepassword.setText(null);
                                       tepassword.setText(null);
                                       if(user.isEmailVerified()){
                                           Intent intent = new Intent(MainActivity.this, loggedinn.class);
                                           startActivity(intent);
                                       }else {
                                           startActivity(new Intent(MainActivity.this, verify.class));
                                       }

                                   }else if(type==0){
                                       loadingalert.closeAlertDialog();
                                       teemail.setText(null);
                                       tepassword.setText(null);
                                       Intent intent = new Intent(MainActivity.this, Adminui.class);
                                       startActivity(intent);
                                   }else{
                                       loadingalert.closeAlertDialog();
                                       tepassword.setText(null);
                                       FirebaseAuth.getInstance().signOut();
                                       Toast.makeText(MainActivity.this, "USER TYPE NOT FOUND", Toast.LENGTH_LONG).show();
                                   }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                loadingalert.closeAlertDialog();
                                tepassword.setText(null);
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                }else{
                        loadingalert.closeAlertDialog();
                        Toast.makeText(MainActivity.this, "NULL USER", Toast.LENGTH_LONG).show();
                    }
                }else{
                    loadingalert.closeAlertDialog();
                    tepassword.setText(null);
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exit();
    }


}