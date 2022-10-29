package com.example.quiziac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiziac.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login_activity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();


        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in...");

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,pass;
                email = binding.email.getText().toString();
                pass = binding.password.getText().toString();

                if(email.length() == 0)
                {
                    binding.email.setError("Email is required.");
                    binding.email.requestFocus();
                    return;
                }
                if(pass.length() == 0)
                {
                    binding.password.setError("Password is required.");
                    binding.password.requestFocus();
                    return;
                }

                dialog.show();
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if(task.isSuccessful())
                        {
                            startActivity(new Intent(login_activity.this,Signup_activity.class));
                            finish();
                            }
                        else{
                            Toast.makeText(login_activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        binding.forgetPass.setOnClickListener(view -> {
            Intent intent = new Intent(login_activity.this, ForgetActivity.class);
            startActivity(intent);
        });

        binding.Register.setOnClickListener(view -> {
            Intent intent = new Intent(login_activity.this, register_activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        if(user!=null)
        {
            startActivity(new Intent(login_activity.this,Signup_activity.class));
            finish();
        }
        super.onStart();
    }
}