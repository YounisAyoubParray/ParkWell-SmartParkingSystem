package com.example.smartparking;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.FirebaseDatabase;


public class CustomDialog extends Dialog {
    private Button okButton;
    private EditText getty;
    private CustomDialogListener dialogListener;

    public CustomDialog(Context context) {
        super(context);
    }
    public void setDialogListener(CustomDialogListener listener) {
        this.dialogListener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        setContentView(dialogView);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            window.setAttributes(layoutParams);
        }
        getty=dialogView.findViewById(R.id.cpttv);
        okButton = dialogView.findViewById(R.id.endbtvv);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String moneey=getty.getText().toString();
                if(moneey.isEmpty()){
                    Toast.makeText(getContext().getApplicationContext(), "Enter the new parking charges", Toast.LENGTH_SHORT).show();
                }else{
                    long money=Long.parseLong(moneey);
                    if(money>0l){
                        FirebaseDatabase.getInstance().getReference("cost").child("Srinagar").setValue(money).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext().getApplicationContext(), "Parking charges upgraded successfully", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(), "Some error occurred try again later", Toast.LENGTH_SHORT).show();
                                }
                                dismiss();
                            }
                        });
                    }
                }

            }
        });
    }

    @Override
    public void dismiss() {
        if (dialogListener != null) {
            dialogListener.onDialogDismissed();
        }

        super.dismiss();
    }


}
