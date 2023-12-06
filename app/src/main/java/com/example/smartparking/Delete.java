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

public class Delete extends AppCompatActivity {
    private EditText  dcpassid;


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
        setContentView(R.layout.activity_delete);
        dcpassid=findViewById(R.id.dcpassid);
        Button dcce=findViewById(R.id.dcce);
        dcce.setOnClickListener(view -> {
            String password=dcpassid.getText().toString().trim();
            if(password.isEmpty())
            {
                dcpassid.setError("Enter the password");
                dcpassid.requestFocus();
                return;
            }
            if(password.length()<8)
            {
                dcpassid.setError("Password length must be >= 8");
                dcpassid.requestFocus();
                return;
            }else{
                reauth();
            }
        });
    }


    private void reauth() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mail = user.getEmail();
        String password= dcpassid.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(mail,password );

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        delete();
                    }else{
                        Toast.makeText(Delete.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void delete() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User Deleted Successfully",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Delete.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Delete.this, loggedinn.class);
                        startActivity(intent);
                    }
                });
    }


}