package com.example.quiziac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
//import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import android.view.View;
import android.os.Environment;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quiziac.databinding.ActivityResultBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    Button button,share;
    //File imagePath;
    int points = 2;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int correctAnswers = getIntent().getIntExtra("correct",0);
        int totalQuestions = getIntent().getIntExtra("total",0);

        long scores = (long) correctAnswers *points;

        binding.score.setText(String.format("%d/%d",correctAnswers,totalQuestions));

        FirebaseFirestore database = FirebaseFirestore.getInstance(); //user login id
        database.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .update("pointsMarks", FieldValue.increment(scores));

        button = findViewById(R.id.replay);
        share = findViewById(R.id.share);


        button.setOnClickListener(view -> {
            Intent intent = new Intent(ResultActivity.this,Signup_activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        share.setOnClickListener(view -> {
         File file =  SaveImage();
         if(file!=null)
             share(file);
        });
    }

    private void share(File file) {
        Uri uri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //uri = FileProvider.getUriForFile(this,getPackageName()+".provider",file);
            uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                    BuildConfig.APPLICATION_ID + ".provider", file);
        }
        else
        {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Screenshot");
        intent.putExtra(Intent.EXTRA_TEXT,"Quiziac score");
        intent.putExtra(Intent.EXTRA_STREAM,uri);

        try{
            startActivity(Intent.createChooser(intent,"Share via"));
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            share.performClick();
        }
        else
        {
            Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private File SaveImage(){
        if(!checkPermission())
            return null;
        try {
            String path = Environment.getExternalStorageDirectory().toString()+"/Quiziac";
            File fileDir = new File(path);
            if(!fileDir.exists())
                fileDir.mkdir();

            String mPath = path+"/Screenshot_"+new Date().getTime()+".png";
            Bitmap bitmap = screenShot();
            File file = new File(mPath);
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fOut);
            fOut.flush();
            fOut.close();

            Toast.makeText(this, "Image saved successfully",Toast.LENGTH_LONG).show();
            return  file;

        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap screenShot() {
        View v = findViewById(R.id.resultPage);
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private boolean checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},1);
            return false;
        }
        return true;
    }

//task when back key is pressed
    public void onBackPressed(){
        Intent intent = new Intent(ResultActivity.this,Signup_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}