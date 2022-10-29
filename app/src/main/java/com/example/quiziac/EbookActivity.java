package com.example.quiziac;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class EbookActivity extends AppCompatActivity {

    private RecyclerView ebookRecycler;
    FirebaseFirestore database;
    private List<EbookData> list;
    private EbookAdapter adapter;
    String FeaturesId,catId;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook);

        ebookRecycler = findViewById(R.id.ebookRecyclers);

        database = FirebaseFirestore.getInstance();

        catId = getIntent().getStringExtra("catId");
        FeaturesId = getIntent().getStringExtra("FeaturesId");
        getData();
    }

    private void getData() {
        database.collection("categories")
                .document(catId).collection("featuresList")
                .document(FeaturesId).collection("notesPDF").orderBy("name", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    list = new ArrayList<>();
                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                        EbookData data = snapshot.toObject(EbookData.class);
                        list.add(data);
                    }
                    adapter = new EbookAdapter(EbookActivity.this,list);
                    ebookRecycler.setLayoutManager(new LinearLayoutManager(EbookActivity.this));
                    ebookRecycler.setAdapter(adapter);
                });
    }
}