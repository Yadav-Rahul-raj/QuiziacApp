package com.example.quiziac;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {


    FirebaseFirestore database;
    CircleImageView profilePic;
    TextView username;
    TextView email;
    TextView logout,aboutUs;
    SwitchCompat theme;

    //extra features
    TextView edit_profile;
    FirebaseUser user;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        profilePic = view.findViewById(R.id.profilePic);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        edit_profile = view.findViewById(R.id.edit_profile);
        theme = view.findViewById(R.id.theme);
        aboutUs = view.findViewById(R.id.aboutUs);
        logout = view.findViewById(R.id.logout);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("save", Context.MODE_PRIVATE);
        theme.setChecked(sharedPreferences.getBoolean("value",false));



        edit_profile.setOnClickListener(view1 -> {
            EditProfileFragment editProfileFragment = new EditProfileFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content,editProfileFragment,null)
                    .addToBackStack(null).commit();
        });

        aboutUs.setOnClickListener(view12 -> {
            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content,aboutUsFragment,null)
                    .addToBackStack(null).commit();
        });


        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(theme.isChecked())
                {

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("save",Context.MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    theme.setChecked(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else
                {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("save",Context.MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    theme.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


        logout.setOnClickListener(view13 -> {
            if(theme.isChecked())
            {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("save",Context.MODE_PRIVATE).edit();
                editor.putBoolean("value",false);
                editor.apply();
                theme.setChecked(false);
            }
            FirebaseAuth.getInstance().signOut();moveToLoginActivity();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(getActivity(), "Logout Successfully", Toast.LENGTH_SHORT).show();
        });

        


        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        database.collection("users").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists())
                {
                    User users = value.toObject(User.class);
                    username.setText(users.getUsername());
                    email.setText(users.getEmail());
                    if(users.getProfileImage().equals("default")){
                        profilePic.setImageResource(R.drawable.ic_user);
                    }
                    else
                    {
                        Activity activity = getActivity();
                        if(activity != null){

                            Glide.with(requireActivity().getApplicationContext()).load(users.getProfileImage()).into(profilePic);
                        }
                    }
                }
            }
        });

        return view;
    }

    private void moveToLoginActivity() {Intent i = new Intent(getActivity(), login_activity.class);i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);startActivity(i);((Activity) getActivity()).overridePendingTransition(0, 0);
    }

}