package com.example.quiziac;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.quiziac.databinding.ActivitySignupBinding;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class Signup_activity extends AppCompatActivity {

    ActivitySignupBinding binding;
    //FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //database = FirebaseFirestore.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content,new HomeFragment());
        transaction.commit();

        checkConnection(); //check internet connection

        binding.bottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            switch (i)
            {
                case 0:
                    transaction1.replace(R.id.content,new HomeFragment());
                    transaction1.commit();
                    break;
                case 1:
                    transaction1.replace(R.id.content,new LeaderboardsFragment());
                    transaction1.commit();
                    break;
                case 2:
                    transaction1.replace(R.id.content,new ProfileFragment());
                    transaction1.commit();
                    break;
            }
            return false;
        });
    }
    public void checkConnection(){
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if(null==activeNetwork)
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }
}