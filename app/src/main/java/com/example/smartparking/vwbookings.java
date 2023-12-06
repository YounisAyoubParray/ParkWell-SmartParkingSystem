package com.example.smartparking;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class vwbookings extends AppCompatActivity {

    private TextView pname1, sln1, cname1, cemail1,cnumber1, cdate1, centry1, cexit1, cvt1, cvn1, cdt1, cpay1, cinvoice1;
    private Button paycon1, dnbutton1;
    private Bitmap bitmap;
    private FirebaseUser user;
    private ConstraintLayout pdfview1;
    String[] permission ={READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>()
    {
        @Override
        public void onActivityResult(ActivityResult result){
            if(result.getResultCode()== Activity.RESULT_OK){
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                    if(Environment.isExternalStorageManager()){
                        Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vwbookings);
        pdfview1 = findViewById(R.id.pdfview1);
        paycon1=findViewById(R.id.paycon1);
        dnbutton1=findViewById(R.id.dnbutton1);
        cname1=findViewById(R.id.cname1);
        cemail1=findViewById(R.id.cemail1);
        cnumber1=findViewById(R.id.cnumber1);
        cdate1=findViewById(R.id.cdate1);
        centry1=findViewById(R.id.centry1);
        cdt1=findViewById(R.id.cdt1);
        cexit1=findViewById(R.id.cexit1);
        cvt1=findViewById(R.id.cvt1);
        cvn1=findViewById(R.id.cvn1);
        cpay1=findViewById(R.id.cpay1);
        cinvoice1=findViewById(R.id.cinvoice1);
        Intent intent = getIntent();
        String invoice = intent.getStringExtra("inv");
        pname1=findViewById(R.id.pname1);
        String parkingname = intent.getStringExtra("name");
        String date =intent.getStringExtra("date");
        pname1.setText(parkingname);
        sln1=findViewById(R.id.sln1);
        FirebaseDatabase.getInstance().getReference("booking").child(parkingname).child(date).child(String.valueOf(invoice)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String agt="";
                Bookingdetail book = snapshot.getValue(Bookingdetail.class);
                if(snapshot.hasChild("Relocated")){
                    String am=snapshot.child("Relocated").getValue(String.class);
                    agt=" relocated to "+am;
                }
                if(book!=null)
                {
                    String sl= book.SlotNumber+agt;
                    String vn= book.VehcileNumber;
                    String en= book.Entry;
                    String ex= book.Exit;
                    String vt= book.VehcileType;
                    String uiid= book.UserId;
                    String dt= book.Date;
                    String pay= book.Payment;
                    String current=book.Bookingtime;
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    String em=user.getEmail();

                        FirebaseDatabase.getInstance().getReference("Users").child(uiid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User pd = snapshot.getValue(User.class);
                                if (pd != null) {
                                    String fn = pd.FullName;
                                    String ph = pd.Phone;
                                    sln1.setText(sl);
                                    cname1.setText(fn);
                                    cemail1.setText(em);
                                    cnumber1.setText(ph);
                                    cinvoice1.setText(String.valueOf(invoice));
                                    cdate1.setText(dt);
                                    centry1.setText(en);
                                    cexit1.setText(ex);
                                    cvt1.setText(vt);
                                    cdt1.setText(current);
                                    cvn1.setText(vn);
                                    cpay1.setText(pay);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(vwbookings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(vwbookings.this, "Oops error occurred", Toast.LENGTH_SHORT).show();
                    }

                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(vwbookings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        dnbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()){
                    bitmap = LoadBitmap(pdfview1,pdfview1.getWidth(), pdfview1.getHeight());
                    createpdf();
                }else{
                    requestPermission();
                }
            }
        });

        paycon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent intent = new Intent(vwbookings.this, loggedinn.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
            }
        });


    }

    void requestPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            try{
                Intent intent=new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",new Object[]{getApplicationContext().getPackageName()})));
                activityResultLauncher.launch(intent);
            }catch(Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activityResultLauncher.launch(intent);
            }
        }else{
            ActivityCompat.requestPermissions(vwbookings.this,permission,30);
        }
    }


    boolean checkPermission(){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }else{
            int rc= ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE);
            int wc= ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
            return rc== PackageManager.PERMISSION_GRANTED && wc ==PackageManager.PERMISSION_GRANTED;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch(requestCode){
            case 30:
                if(grantResults.length>0){
                    boolean rc=grantResults[0]== getPackageManager().PERMISSION_GRANTED;
                    boolean wc=grantResults[1]== getPackageManager().PERMISSION_GRANTED;
                    if(rc && wc){
                        Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Bitmap LoadBitmap(View v,int width, int height){
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
    private void createpdf(){

        DisplayMetrics displayMetrics=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float width=displayMetrics.widthPixels;
        float height= displayMetrics.heightPixels;
        int convertWidth=(int)width,convertheight=(int)height;
        PdfDocument document=new PdfDocument();
        PdfDocument.PageInfo pageInfo =new PdfDocument.PageInfo.Builder(convertWidth,convertheight,1).create();
        PdfDocument.Page page =document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint=new Paint();
        canvas.drawPaint(paint);
        bitmap=Bitmap.createScaledBitmap(bitmap,convertWidth,convertheight,false);
        canvas.drawBitmap(bitmap,0,0,null);
        document.finishPage(page);

        try{
            File dir= new File(Environment.getExternalStoragePublicDirectory("Download"),cinvoice1.getText().toString()+".pdf");
            document.writeTo(new FileOutputStream(dir));
            Toast.makeText(this,"Successfully Downloaded",Toast.LENGTH_SHORT).show();
        }catch(IOException e){
            Toast.makeText(this,"Something wrong happened "+e.toString(),Toast.LENGTH_SHORT).show();
        }

        document.close();

    }


}