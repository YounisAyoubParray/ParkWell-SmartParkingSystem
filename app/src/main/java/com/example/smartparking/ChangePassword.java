package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private EditText passchange, cpassid,passchangec;


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
        setContentView(R.layout.activity_change_password);
        passchange=findViewById(R.id.passchange);
        passchangec=findViewById(R.id.passchangec);
        cpassid=findViewById(R.id.cpassid);

        Button cce=findViewById(R.id.cce);
        cce.setOnClickListener(view -> {
            String password=cpassid.getText().toString().trim();
            if(password.isEmpty())
            {
                cpassid.setError("Enter the password");
                cpassid.requestFocus();
                return;
            }
            if(password.length()<8)
            {
                cpassid.setError("Password length must be >= 8");
                cpassid.requestFocus();
                return;
            }else{
                reauth();
            }

        });
    }

    private void reauth() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mail = user.getEmail();
        String password= cpassid.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(mail,password );

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                        newpass();
                        }else{
                            Toast.makeText(ChangePassword.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void newpass() {
        String password= passchange.getText().toString();
        String cpassword=passchangec.getText().toString();
        if(password.isEmpty())
        {
            cpassid.setError("Enter the password");
            cpassid.requestFocus();
            return;
        }
        if(password.length()<8)
        {
            cpassid.setError("Password length must be >= 8");
            cpassid.requestFocus();
            return;}
        if(!password.equals(cpassword)){
            passchange.requestFocus();
            passchangec.setError("Password doesn't match");
            passchangec.requestFocus();
            return;
        }else{

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            user.updatePassword(cpassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(ChangePassword.this, loggedinn.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(ChangePassword.this,"Oops some error occured",Toast.LENGTH_SHORT).show();
                        }
                    });


        }


    }

}