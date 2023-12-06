package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import android.text.Html;
import android.util.Patterns;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;


public class ChangeEmail extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private EditText emailchange, passid;


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
        setContentView(R.layout.activity_change_email);
        builder = new AlertDialog.Builder(ChangeEmail.this);
        emailchange = findViewById(R.id.emailchange);
        passid = findViewById(R.id.passid);
        Button ce  =findViewById(R.id.ce);
        ce.setOnClickListener(view -> {
            String newemail = emailchange.getText().toString().trim();
            if(newemail.isEmpty())
            {
                emailchange.setError("Enter the email");
                emailchange.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(newemail).matches()){
                emailchange.setError("Enter the correct email");
                emailchange.requestFocus();
                return;
            }else {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String oldmail = user.getEmail();
                if (oldmail.equals(newemail)) {
                    Toast.makeText(ChangeEmail.this, "Use new email to update your email", Toast.LENGTH_SHORT).show();
                } else {
                    reauth();
                    verify();
                }
            }});






    }

    private void reauth() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String oldmail = user.getEmail();
        String password= passid.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(oldmail,password );

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }else{
                            Toast.makeText(ChangeEmail.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void verify(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email=emailchange.getText().toString();
        if(user!=null) {
            user.verifyBeforeUpdateEmail(email,null).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            // Email sent.
                            builder.setTitle("Park Well");
                            builder.setIcon(R.drawable.parkinglogo);
                            builder.setMessage("Verification link has been sent to your email. Verify your email first to update your email.");
                            builder.setCancelable(false);
                            builder.setPositiveButton(Html.fromHtml("<font color='#014077'> Continue </font>"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(ChangeEmail.this, loggedinn.class);
                                    startActivity(intent);
                                }
                            });
                            builder.show();
                            // User must click the email link before the email is updated.
                        } else {
                            Toast.makeText(ChangeEmail.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });






        }
    }


}