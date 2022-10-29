package com.example.quiziac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends AppCompatActivity {

    private RecyclerView videoRecycler;
    FirebaseFirestore database;
    private List<VideoData> list;
    private VideoAdapter adapter;
    String FeaturesId, catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoRecycler = findViewById(R.id.videoRecyclers);

        database = FirebaseFirestore.getInstance();

        catId = getIntent().getStringExtra("catId");
        FeaturesId = getIntent().getStringExtra("FeaturesId");
        getData();
    }

    private void getData() {
        database.collection("categories")
                .document(catId).collection("featuresList")
                .document(FeaturesId).collection("tutorialVideo").orderBy("name", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        list = new ArrayList<>();
                        for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                        {
                            VideoData data = snapshot.toObject(VideoData.class);
                            list.add(data);
                        }
                        adapter = new VideoAdapter(VideoActivity.this,list);
                        videoRecycler.setLayoutManager(new LinearLayoutManager(VideoActivity.this));
                        videoRecycler.setAdapter(adapter);
                    }
                });
    }
}