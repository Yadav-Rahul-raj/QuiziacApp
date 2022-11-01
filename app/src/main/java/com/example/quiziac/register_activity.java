package com.example.quiziac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiziac.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class register_activity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore database;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

//        setContentView(R.layout.activity_register);
            setContentView(binding.getRoot());

            auth = FirebaseAuth.getInstance();
            database = FirebaseFirestore.getInstance();

            dialog = new ProgressDialog(this);
            dialog.setMessage("Creating Account...");

            binding.Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email,pass,username,profileImage = "https://firebasestorage.googleapis.com/v0/b/quiziac.appspot.com/o/users%2Fuser%20(1).png?alt=media&token=d722122c-d3eb-4a98-9628-1fc6a7dd65de";
                    int pointsMarks = 5;
                    username = binding.username.getText().toString();
                    email = binding.email.getText().toString();
                    pass = binding.password.getText().toString();
                    String cpass = binding.cPassword.getText().toString();



                    if(username.length() == 0)
                    {
                        binding.username.setError("Username is required.");
                        binding.username.requestFocus();
                        return;
                    }
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
                    if(!pass.equals(cpass))
                    {
                        Toast.makeText(register_activity.this, "Password not matching", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    User user = new User(username,email,pass, pointsMarks, profileImage);
                    dialog.show();

                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                           {
                               String uid = task.getResult().getUser().getUid();
                               database.collection("users").document(uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful())
                                               {
                                                   dialog.dismiss();
                                                   Intent intent = new Intent(register_activity.this,Signup_activity.class);
                                                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                           Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                           Intent.FLAG_ACTIVITY_NEW_TASK);
                                                   startActivity(intent);
                                                   finish();
                                               }
                                               else{
                                                   Toast.makeText(register_activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });
                               Toast.makeText(register_activity.this, "Thanks for filling out our form!", Toast.LENGTH_SHORT).show();

                           }
                           else{
                               dialog.dismiss();
                               Toast.makeText(register_activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                           }
                        }
                    });
                }
            });
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register_activity.this,login_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}