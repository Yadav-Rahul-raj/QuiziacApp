package com.example.quiziac;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {
    
    EditText email;
    Button resetPass;
    TextView registerPage;
    FirebaseAuth auth;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        
        email = findViewById(R.id.email);
        resetPass = findViewById(R.id.ResetPass);
        registerPage = findViewById(R.id.Register);
        
        auth = FirebaseAuth.getInstance();


    
        
        resetPass.setOnClickListener(view -> resetPassword());

        registerPage.setOnClickListener(view -> {
            Intent intent = new Intent(ForgetActivity.this,register_activity.class);
            startActivity(intent);
        });
    }

    private void resetPassword() {
        
        String emailAdd = email.getText().toString().trim();
        
        if(!Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches()){
            email.setError("Please enter valid email..");
            email.requestFocus();
            return;
        }
        auth.sendPasswordResetEmail(emailAdd).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                Toast.makeText(ForgetActivity.this, "Check Email for password reset", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgetActivity.this,login_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(ForgetActivity.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}