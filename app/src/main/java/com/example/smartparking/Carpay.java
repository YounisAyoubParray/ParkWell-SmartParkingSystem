package com.example.smartparking;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.text.Html;
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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Carpay extends AppCompatActivity {

    private TextView pname, sln, cname, cemail,cnumber, cdate, centry, cexit, cvt, cvn, cpay, cinvoice, cdt;
    private Button paycon, dnbutton;
    private FirebaseUser user;
    private String uid;
    private Bitmap bitmap;
    private AlertDialog.Builder builder;
    private ConstraintLayout pdfview;
    String[] permission ={READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>()
    {
        @Override
        public void onActivityResult(ActivityResult result){
            if(result.getResultCode()==Activity.RESULT_OK){
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
        setContentView(R.layout.activity_carpay);
        pdfview = findViewById(R.id.pdfview);
        builder = new AlertDialog.Builder(Carpay.this);
        paycon=findViewById(R.id.paycon);
        cdt=findViewById(R.id.cdt);
        dnbutton=findViewById(R.id.dnbutton);
        cname=findViewById(R.id.cname);
        cemail=findViewById(R.id.cemail);
        cnumber=findViewById(R.id.cnumber);
        cdate=findViewById(R.id.cdate);
        centry=findViewById(R.id.centry);
        cexit=findViewById(R.id.cexit);
        cvt=findViewById(R.id.cvt);
        cvn=findViewById(R.id.cvn);
        cpay=findViewById(R.id.cpay);
        cinvoice=findViewById(R.id.cinvoice);
        Intent intent = getIntent();
        Long invoice = intent.getLongExtra("invoice",-1L);
        pname=findViewById(R.id.pname);
        String parkingname = intent.getStringExtra("parkingname");
        String date =intent.getStringExtra("date");
        pname.setText(parkingname);
        sln=findViewById(R.id.sln);

        FirebaseDatabase.getInstance().getReference("booking").child(parkingname).child(date).child(String.valueOf(invoice)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Bookingdetail book = snapshot.getValue(Bookingdetail.class);
                if(book!=null)
                {
                    String sl= book.SlotNumber;
                    String vn= book.VehcileNumber;
                    String en= book.Entry;
                    String ex= book.Exit;
                    String vt= book.VehcileType;
                    String uiid= book.UserId;
                    String dt= book.Date;
                    String pay= book.Payment;
                    String current= book.Bookingtime;
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    String em=user.getEmail();
                    uid=user.getUid();
                    if(uid.equals(uiid)){
                        FirebaseDatabase.getInstance().getReference("Users").child(uiid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User pd = snapshot.getValue(User.class);
                                if(pd!=null){
                                    String fn=pd.FullName;
                                    String ph=pd.Phone;
                                    sln.setText(sl);
                                    cname.setText(fn);
                                    cdt.setText(current);
                                    cemail.setText(em);
                                    cnumber.setText(ph);
                                    cinvoice.setText(String.valueOf(invoice));
                                    cdate.setText(dt);
                                    centry.setText(en);
                                    cexit.setText(ex);
                                    cvt.setText(vt);
                                    cvn.setText(vn);
                                    cpay.setText(pay);
                                }
                                else{
                                    Toast.makeText(Carpay.this, "Something wrong happened", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Carpay.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(Carpay.this, "Slot booked by another user", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Carpay.this, "Something wrong happened", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Carpay.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        dnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()){
                bitmap = LoadBitmap(pdfview,pdfview.getWidth(), pdfview.getHeight());
                createpdf();
                }else{
                    requestPermission();
                }
            }
        });

        paycon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invoicealert();
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
            ActivityCompat.requestPermissions(Carpay.this,permission,30);
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
            File dir= new File(Environment.getExternalStoragePublicDirectory("Download"),cinvoice.getText().toString()+".pdf");
            document.writeTo(new FileOutputStream(dir));
            Toast.makeText(this,"Successfully Downloaded",Toast.LENGTH_SHORT).show();
        }catch(IOException e){
            Toast.makeText(this,"Something wrong happened "+e.toString(),Toast.LENGTH_SHORT).show();
        }

        document.close();

    }

    @Override
    public void onBackPressed() {
        invoicealert();


    }

    private void invoicealert() {
        String a=  cinvoice.getText().toString() ;
        builder.setTitle("Park Well");
        builder.setIcon(R.drawable.parkinglogo);
        builder.setMessage("Note down your invoice number " +a+ " or Make sure you downloaded the pdf for future references");
        builder.setCancelable(false);
        builder.setNegativeButton(Html.fromHtml("<font color='#8f0404'> Continue </font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Carpay.this, loggedinn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setPositiveButton(Html.fromHtml("<font color='#014077'> Cancel </font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


}