package com.example.quiziac;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quiziac.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    FirebaseFirestore database;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentHomeBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();
        // Inflate the layout for this fragment
        final ArrayList<CategoryModel> categories = new ArrayList<>();
        final CategoryAdapter adapter  = new CategoryAdapter( getContext(),categories);
        database.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categories.clear();
                        for(DocumentSnapshot snapshot: value.getDocuments()){
                            CategoryModel model = snapshot.toObject(CategoryModel.class);
                            model.setCategoryId(snapshot.getId());
                            categories.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
/*
       //categories.add(new CategoryModel("","Previous Paper","https://raw.githubusercontent.com/Yadav-Rahul-raj/Quiziac/main/logo_image/paperrecord.jpg"));
//        //categories.add(new CategoryModel("","Star Question","https://raw.githubusercontent.com/Yadav-Rahul-raj/Quiziac/main/logo_image/bk.jpg"));
//        //categories.add(new CategoryModel("","Tutorial Video Link","https://raw.githubusercontent.com/Yadav-Rahul-raj/Quiziac/main/logo_image/linkvideo.jpg"));
//        categories.add(new CategoryModel("","Operating System","https://raw.githubusercontent.com/Yadav-Rahul-raj/Quiziac/main/logo_image/os.jpg"));
//        categories.add(new CategoryModel("","DBMS","https://raw.githubusercontent.com/Yadav-Rahul-raj/Quiziac/main/logo_image/db.jpg"));
//        categories.add(new CategoryModel("","CN","https://raw.githubusercontent.com/Yadav-Rahul-raj/Quiziac/main/logo_image/cn.jpg"));
//        categories.add(new CategoryModel("","Math","https://raw.githubusercontent.com/Yadav-Rahul-raj/Quiziac/main/logo_image/math.jpg"));
//        //categories.add(new CategoryModel("","Mock test","https://raw.githubusercontent.com/Yadav-Rahul-raj/Quiziac/main/logo_image/mocktest.jpg"));


*/

        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoryList.setAdapter(adapter);
        return binding.getRoot();
    }
}