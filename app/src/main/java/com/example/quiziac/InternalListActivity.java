package com.example.quiziac;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.example.quiziac.databinding.ActivityInternalListBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InternalListActivity extends AppCompatActivity {

    ActivityInternalListBinding binding;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_internal_list);
        binding = ActivityInternalListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String cat = getIntent().getStringExtra("catId");


        database = FirebaseFirestore.getInstance();


        ArrayList<FeatureModel> featuresList = new ArrayList<>();
        //featuresList.add(new FeatureModel("","Mock","https://raw.githubusercontent.com/Yadav-Rahul-raj/Quiziac/main/logo_image/cn.jpg"));
        FeatureAdapter adapter = new FeatureAdapter(this,featuresList);
        //new FeatureAdapter(this, cat);


        //String a = String.valueOf(catId);

        database.collection("categories").document(cat).collection("featuresList")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                featuresList.clear();
                                for(DocumentSnapshot snapshot:value.getDocuments()){
                                    FeatureModel model = snapshot.toObject(FeatureModel.class);
                                    model.setFeaturesId(snapshot.getId());
                                    featuresList.add(model);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        binding.InternalList.setLayoutManager(new GridLayoutManager(this,2));
        binding.InternalList.setAdapter(adapter);

    }
}