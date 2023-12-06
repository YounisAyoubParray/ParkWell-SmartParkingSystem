package com.example.smartparking;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button rregister;
    private AlertDialog.Builder builder;
    private EditText rname, remail, rphone, raddress, rpassword,rrrpassword;

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
        setContentView(R.layout.activity_register);
        builder = new AlertDialog.Builder(Register.this);
        mAuth = FirebaseAuth.getInstance();

        rregister = findViewById(R.id.rregister);
        rregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registeruser();
            }
        });



        rname = findViewById(R.id.rname);
        remail = findViewById(R.id.remail);
        raddress = findViewById(R.id.raddress);
        rphone = findViewById(R.id.rphone);
        rpassword = findViewById(R.id.rpassword);
        rrrpassword = findViewById(R.id.rrrpassword);



    }



    public void registeruser(){
        String mail=remail.getText().toString().trim();
        String name=rname.getText().toString().toUpperCase(Locale.ROOT).trim();
        String address=raddress.getText().toString().trim();
        String phone=rphone.getText().toString().trim();
        String password=rpassword.getText().toString().trim();
        String rpass=rrrpassword.getText().toString().trim();

        if(name.isEmpty())
        {
            rname.setError("Enter the name");
            rname.requestFocus();
            return;
        }
        if(mail.isEmpty())
        {
            remail.setError("Enter the email");
            remail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            remail.setError("Enter the correct email");
            remail.requestFocus();
            return;
        }
        if(address.isEmpty())
        {
            raddress.setError("Enter the address");
            raddress.requestFocus();
            return;
        }
        if(phone.isEmpty())
        {
            rphone.setError("Enter the phone");
            rphone.requestFocus();
            return;
        }
        if(phone.length()!=10)
        {
            rphone.setError("Enter the correct phone number");
            rphone.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            rpassword.setError("Enter the password");
            rpassword.requestFocus();
            return;
        }

        if(password.length()<8)
        {
            rpassword.setError("Password length must be >= 8");
            rpassword.requestFocus();
            return;
        }
        if(rpass.isEmpty()){
            rrrpassword.setError("Confirm the password");
            rrrpassword.requestFocus();
            return;
        }
        if(!password.equals(rpass)){
            rrrpassword.setError("Password doesn't match");
            rrrpassword.requestFocus();
            return;
        }
        final Loadingalert1 loadingalert = new Loadingalert1(Register.this);
        loadingalert.startAlertDialog();
        mAuth.createUserWithEmailAndPassword(mail,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user=new User(name,mail,phone,address,1);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                user.sendEmailVerification();
                                                Toast.makeText(Register.this,"Please verify your email to login",Toast.LENGTH_LONG).show();
                                                loadingalert.closeAlertDialog();
                                                startActivity(new Intent(Register.this, MainActivity.class));
                                            }
                                            else
                                                Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            loadingalert.closeAlertDialog();
                                        }
                                    });
                        }
                        else
                            Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        loadingalert.closeAlertDialog();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        warning();
    }

    private void warning() {
        builder.setTitle("Park Well");
        builder.setIcon(R.drawable.parkinglogo);
        builder.setMessage("Do you want to cancel the registration?");
        builder.setCancelable(false);
        builder.setPositiveButton(Html.fromHtml("<font color='#014077'> No </font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(Html.fromHtml("<font color='#ba1e13'> Yes </font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.show();
    }
}