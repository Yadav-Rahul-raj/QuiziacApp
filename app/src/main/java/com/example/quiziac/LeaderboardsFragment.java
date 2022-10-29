package com.example.quiziac;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.quiziac.databinding.FragmentLeaderboardsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LeaderboardsFragment extends Fragment {
    FirebaseUser user;


    public LeaderboardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentLeaderboardsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        binding = FragmentLeaderboardsBinding.inflate(inflater,container,false);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        ArrayList<User> users = new ArrayList<>();
        LeaderboardsAdapter adapter = new LeaderboardsAdapter(getContext(),users);
        
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        user = FirebaseAuth.getInstance().getCurrentUser();

        database.collection("users").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists())
                {
                    User users = value.toObject(User.class);
                    binding.Myname.setText(users.getUsername());
                   binding.score.setText(Integer.toString(users.getPointsMarks()));
                    if(users.getProfileImage().equals("default"))
                    {
                        binding.MyprofilePhoto.setImageResource(R.drawable.ic_user);
                    }
                    else
                    {
                        Activity activity = getActivity();
                        if(activity !=null)
                        {
                            Glide.with(requireActivity().getApplicationContext()).load(users.getProfileImage()).into(binding.MyprofilePhoto);
                        }
                    }
                }
            }
        });
        
        database.collection("users").orderBy("pointsMarks", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                            {
                                User user  = snapshot.toObject(User.class);
                                users.add(user);
                            }
                            adapter.notifyDataSetChanged();
                    }
                });

        return binding.getRoot();
    }
}