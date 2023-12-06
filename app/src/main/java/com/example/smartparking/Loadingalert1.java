package com.example.smartparking;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class Loadingalert1 {
    private final Activity activity;
    private AlertDialog dialog;
    Loadingalert1(Activity myActivity){
        activity = myActivity;
    }

    void startAlertDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.regiserload,null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    void closeAlertDialog() {
        dialog.dismiss();
    }
}
